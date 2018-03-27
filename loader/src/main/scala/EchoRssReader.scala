import java.text.SimpleDateFormat
import java.util.Locale

import org.htmlcleaner.HtmlCleaner

import scala.util.control.NonFatal
import scala.xml.XML
import scalaj.http.Http

object EchoRssReader extends App {

  /**
    * Reads Rss feed, loads articles and provides list of loaded articles
    * @param rssUrl
    * @return List of Article
    */
  def readRss(rssUrl: String): Either[EchoError, List[Article]] = {
    try {
      val xml = XML.load(rssUrl)

      val articles =
        for {
          item <- xml \\ "item"
        } yield ((item \ "link").text.split('/').last, (item \ "title").text, (item \ "pubDate").text, (item \ "link").text)

      articles.map(pair => (pair._1, pair._2, {
        val date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(pair._3)
        new SimpleDateFormat("dd.MM.yyyy").format(date)
      }, pair._4)) // Reformat date

      Right(articles
        .filter(!_._3.isEmpty) //Link is not empty
        .map(pair => Article(pair._1, pair._2, readArticleText(pair._4).right.get, pair._3, pair._4))
        .filter(!_.text.isEmpty).toList)
    }
    catch {
      case NonFatal(e) => Left(IntegrationError("Error during reading RSS", e))
    }
  }

  /**
    * Reads text of article using url.
    * @param url
    * @return Article text
    */
  def readArticleText(url: String): Either[EchoError, String] = {
    try {
      val content = Http(url).asString
      if (content.isError) Left(new Throwable("HTTP Error"))

      val rootNode = (new HtmlCleaner).clean(content.body)
      val elements = rootNode.getElementsByName("div", true)

      val article = elements.toList
        .filter(elem => elem.hasAttribute("class") && elem.getAttributeByName("class").equalsIgnoreCase("typical dialog _ga1_on_ contextualizable include-relap-widget"))
        .map(elem => elem.getText.toString).foldRight[String]("")((a, b) => a + b)

      Right(article)
    }
    catch {
      case NonFatal(e) => Left(IntegrationError("Error during reading article text", e))
    }
  }

}
