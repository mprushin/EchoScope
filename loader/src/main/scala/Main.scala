object Main extends App {

  val articles = EchoRssReader.readRss("https://echo.msk.ru/interview/rss-fulltext.xml")
  val notYetUploadedArticles = articles.right.get.filter(article => !AwsS3Integration.getLoadedArticles.contains(article.id))
  notYetUploadedArticles.foreach(FileUploader.uploadFile)
}
