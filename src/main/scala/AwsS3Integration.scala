import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets

import com.amazonaws.{ClientConfiguration, Protocol}
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import scala.collection.JavaConverters._

object AwsS3Integration {
  val bucketName = "echoscope"
  val AWS_ACCESS_KEY = "AKIAIRZSBLBWRM6RQFFA"
  val AWS_SECRET_KEY = "Fq3sPTo+kdk+J8XQH1BLYbuCpuC+RpbsajxNtYBg"

  val yourAWSCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)

  val clientCfg = new ClientConfiguration
  clientCfg.setProtocol(Protocol.HTTP)
  clientCfg.setProxyHost("2.196.28.107")
  clientCfg.setProxyPort(8082)

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

    amazonS3Client.putObject(bucketName, key, dataStream, metadata)
    dataStream.close()
  }

  def getListing(): List[String] = {
    val listOfObjects = amazonS3Client.listObjects(bucketName).getObjectSummaries
    listOfObjects.asScala.toList.map(_.getKey)
  }
}
