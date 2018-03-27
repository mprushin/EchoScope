import org.scalatest.FunSuite

trait EchoRssReaderTest extends FunSuite {
  test("read rss correctly test") {
    val articles = EchoRssReader.readRss(Environment.ECHO_RSS_URL)
    assert(articles.isRight)
  }

  test("read rss with error") {
    val articles = EchoRssReader.readRss("incorrect url string")
    assert(articles.isLeft)
  }

  test("read article correctly") {
    val content = EchoRssReader.readArticleText("https://echo.msk.ru/programs/personalnovash/2164668-echo/")
    assert(content.isRight)
  }

  test("read article fault") {
    val content = EchoRssReader.readArticleText("incorrect url string")
    assert(content.isLeft)
  }

}
