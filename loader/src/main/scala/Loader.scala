object Loader extends App {
  val articles = EchoRssReader.readRss("https://echo.msk.ru/interview/rss-fulltext.xml")
  /*val notYetUploadedArticles = articles.map(_.filter(article => !AwsS3Integration.getLoadedArticleIds.contains(article.id)))
  notYetUploadedArticles.map(_.foreach(FileUploader.uploadFile))*/
  if (articles.isLeft) {
    println(articles.left.get.toString)
  }
  else {
    val notYetUploadedArticles = articles.right.get.filter(article => !AwsS3Integration.getLoadedArticleIds.contains(article.id))
    notYetUploadedArticles.foreach(FileUploader.uploadFile)
  }
}
