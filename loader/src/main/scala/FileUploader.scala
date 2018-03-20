import io.circe.generic.auto._
import io.circe.syntax._

object FileUploader {

  def uploadFile(article: Article): Unit = {
    val jsonObjectString = article.asJson.spaces2

    AwsS3Integration.putObject(article.id, jsonObjectString)
    println(article.id + " loaded to S3")
  }
}
