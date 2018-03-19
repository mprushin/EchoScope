import org.scalatest.FunSuite

trait EchoRssReaderTest extends FunSuite {
  test("load files correctly test") {
    val articles = EchoRssReader.readRss(Environment.ECHO_RSS_URL)
    assert(articles.isRight)
  }

  test("load files with error") {
    val articles = EchoRssReader.readRss("incorrect url string")
    assert(articles.isLeft)
  }

}
