/**
  * Case class for storing article data
  * @param id - identifyer, example echo-13213
  * @param title - title of article
  * @param text - text of article
  * @param date - articel publcation date
  * @param link - articl URL
  */

case class Article(id: String, title: String, text: String, date: String, link: String)
