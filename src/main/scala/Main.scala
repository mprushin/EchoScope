object Main extends App {
  val articles = EchoRssReader.readRss("https://echo.msk.ru/interview/rss-fulltext.xml")
  val notYetUploadedArticles = articles.filter(article => !AwsS3Integration.getListing().contains(article.id))
  notYetUploadedArticles.foreach(FileUploader.uploadFile)
}
