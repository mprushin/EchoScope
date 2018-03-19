import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.{ClientConfiguration, Protocol}
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils

import scala.collection.JavaConverters._

object AwsS3Integration {
  val yourAWSCredentials = new BasicAWSCredentials(Environment.AWS_ACCESS_KEY.getOrElse(""), Environment.AWS_SECRET_KEY.getOrElse(""))

  val clientCfg = new ClientConfiguration
  clientCfg.setProtocol(Protocol.HTTP)
  if(Environment.PROXY_HOST.isDefined && Environment.PROXY_PORT.isDefined){
    clientCfg.setProxyHost(Environment.PROXY_HOST.getOrElse(""))
    clientCfg.setProxyPort(Environment.PROXY_PORT.getOrElse("").toInt)
  }

  val amazonS3Client = new AmazonS3Client(yourAWSCredentials, clientCfg)
  //amazonS3Client.setEndpoint("s3.eu-central-1.amazonaws.com")
  //System.setProperty("com.amazonaws.services.s3.enableV4", "true")

  val region: Region = Region.getRegion(Regions.EU_CENTRAL_1)
  amazonS3Client.setRegion(region)

  def getClient(): AmazonS3Client = {
    amazonS3Client
  }

  def putObject(key: String, input: String): Unit = {
    val dataStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))

    val resultByte = DigestUtils.md5(input)
    val streamMD5 = new String(Base64.encodeBase64(resultByte))
    val metadata = new ObjectMetadata
    metadata.setContentMD5(streamMD5)
    metadata.setContentLength(input.getBytes(StandardCharsets.UTF_8).length);

    amazonS3Client.putObject(Environment.BUCKET_NAME.getOrElse(""), key, dataStream, metadata)
    dataStream.close()
  }

  def getListing(): List[String] = {
    val listOfObjects = amazonS3Client.listObjects(Environment.BUCKET_NAME.getOrElse("")).getObjectSummaries
    listOfObjects.asScala.toList.map(_.getKey)
  }
}
