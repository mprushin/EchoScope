import java.io.{BufferedReader, ByteArrayInputStream, InputStreamReader}
import java.nio.charset.StandardCharsets

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.{ClientConfiguration, Protocol}
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils

import scala.collection.JavaConverters._
import scala.util.control.NonFatal


object AwsS3Integration {
  val yourAWSCredentials = new BasicAWSCredentials(Environment.AWS_ACCESS_KEY.getOrElse("AWS ACCESS KEY Should be set up"), Environment.AWS_SECRET_KEY.getOrElse("AWS SECRET KEY Should be set up"))

  val clientCfg = new ClientConfiguration
  clientCfg.setProtocol(Protocol.HTTP)
  if (Environment.PROXY_HOST.isDefined && Environment.PROXY_PORT.isDefined) {
    clientCfg.setProxyHost(Environment.PROXY_HOST.get)
    clientCfg.setProxyPort(Environment.PROXY_PORT.get.toInt)
  }

  val amazonS3Client = new AmazonS3Client(yourAWSCredentials, clientCfg)

  val region: Region = Region.getRegion(Regions.EU_CENTRAL_1)
  amazonS3Client.setRegion(region)

  def putObject(key: String, input: String): Either[EchoError, String] = {
    val dataStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))

    val resultByte = DigestUtils.md5(input)
    val streamMD5 = new String(Base64.encodeBase64(resultByte))
    val metadata = new ObjectMetadata
    metadata.setContentMD5(streamMD5)
    metadata.setContentLength(input.getBytes(StandardCharsets.UTF_8).length)

    try {
      amazonS3Client.putObject(Environment.BUCKET_NAME.getOrElse(""), key, dataStream, metadata)
      Right(key)
    }
    catch {
      case NonFatal(e) => Left(IntegrationError(e.getMessage, e))
    }
    finally {
      dataStream.close()
    }
  }

  def getObject(key: String): Either[EchoError, String] = {
    try {
      val stream = amazonS3Client.getObject(Environment.BUCKET_NAME.getOrElse(""), key).getObjectContent
      val bufferedReader = new BufferedReader(new InputStreamReader(stream))
      val text = bufferedReader.lines.iterator().asScala
      Right(text.reduce(_ + " " + _))
    }
    catch {
      case NonFatal(e) => Left(IntegrationError(e.getMessage, e))
    }
  }

  def getLoadedArticleIds: Either[EchoError, List[String]] = {
    try {
      val listOfObjects = amazonS3Client.listObjects(Environment.BUCKET_NAME.getOrElse("")).getObjectSummaries
      Right(listOfObjects.asScala.toList.map(_.getKey))
    }
    catch {
      case NonFatal(e) => Left(IntegrationError(e.getMessage, e))
    }
  }
}
