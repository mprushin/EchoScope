import io.circe.generic.auto._
import io.circe.syntax._

object FileUploader extends Utils {

  def uploadFile(article: Article): Either[EchoError, String] = {
    val jsonObjectString = article.asJson.spaces2

    val putResult = AwsS3Integration.putObject(article.id, jsonObjectString)
    if(putResult.isRight) println(article.id + " loaded to S3")
    putResult
  }
}
