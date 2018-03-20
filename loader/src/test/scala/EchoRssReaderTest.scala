import org.scalatest.FunSuite

trait EchoRssReaderTest extends FunSuite {
  test("load rss correctly test") {
    val articles = EchoRssReader.readRss(Environment.ECHO_RSS_URL)
    assert(articles.isRight)
  }

  test("load rss with error") {
    val articles = EchoRssReader.readRss("incorrect url string")
    assert(articles.isLeft)
  }

  test("load aarticle correctly") {
    val content = EchoRssReader.loadArticleText("https://echo.msk.ru/programs/personalnovash/2164668-echo/")
    assert(content.isRight)
  }

  test("load article fault") {
    val content = EchoRssReader.loadArticleText("incorrect url string")
    assert(content.isLeft)
  }

}
