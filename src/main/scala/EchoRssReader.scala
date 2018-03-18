import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

import org.htmlcleaner.HtmlCleaner

import scala.collection.mutable.ListBuffer
import scala.xml.XML


object EchoRssReader extends App {

  def readRss(rssUrl: String): Seq[Article] = {
    val xml = XML.load(rssUrl)

    val articles =
      for {
        item <- xml \\ "item"
      } yield ((item \ "link").text.split('/').last, (item \ "title").text, (item \ "pubDate").text, (item \ "link").text)

    articles.map(pair => (pair._1, pair._2, {
      val date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(pair._3)
      new SimpleDateFormat("dd.MM.yyyy").format(date)
    }, pair._4)) // Reformat date

    articles
      .filter(!_._3.isEmpty) //Link is not empty
      .map(pair => Article(pair._1, pair._2, loadArticleText(pair._4), pair._3, pair._4))
      .filter(!_.text.isEmpty)
  }

  def loadArticleText(url: String): String = {
    var stories = new ListBuffer[String]
    val cleaner = new HtmlCleaner
    val rootNode = cleaner.clean(new URL(url))
    val elements = rootNode.getElementsByName("div", true)

    elements.toList
      .filter(elem => elem.hasAttribute("class") && elem.getAttributeByName("class").equalsIgnoreCase("typical dialog _ga1_on_ contextualizable include-relap-widget"))
      .map(elem => elem.getText.toString).foldRight[String]("")((a, b) => a + b)
  }

}
