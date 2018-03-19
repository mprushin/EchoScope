import scala.util.Properties

object Environment {
  val BUCKET_NAME = Properties.envOrNone("BUCKET_NAME")
  val AWS_ACCESS_KEY =Properties.envOrNone("AWS_ACCESS_KEY")
  val AWS_SECRET_KEY =Properties.envOrNone("AWS_SECRET_KEY")
  val PROXY_HOST =Properties.envOrNone("PROXY_HOST")
  val PROXY_PORT =Properties.envOrNone("PROXY_PORT")
}
