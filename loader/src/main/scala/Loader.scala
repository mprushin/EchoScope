/**
  * Object which reads articles from RSS and loads them to S3 storage
  */
object Loader extends Utils with App {
  val readResult = EchoRssReader.readRss("https://echo.msk.ru/interview/rss-fulltext.xml")
  val loadedArticles = AwsS3Integration.getLoadedArticleIds.right.getOrElse(Nil)

  readResult.map(articles => articles.filter(article => !loadedArticles.contains(article.id)))
    .map(_.foreach(FileUploader.uploadFile))
}
