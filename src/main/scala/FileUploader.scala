import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.{ClientConfiguration, Protocol}
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import com.amazonaws.regions.{Region, Regions}

case class Article(id: String, title: String, text: String, date: String, link: String)

object FileUploader {

  def uploadFile(article: Article): Unit = {
    val jsonObjectString = makeJsonObject(article).spaces2

    AwsS3Integration.putObject(article.id, jsonObjectString)
    println(article.id + " loaded to S3")

  }

  def makeJsonObject(article: Article): Json = {
    article.asJson
  }
}
