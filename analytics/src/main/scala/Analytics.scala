import io.circe.generic.auto._
import io.circe.parser.decode
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.{Either, Left}

object Analytics extends Utils {

  @transient lazy val conf: SparkConf = new SparkConf().setMaster("local").setAppName("Observatory")
  @transient lazy val sc: SparkContext = new SparkContext(conf)

  sc.setLogLevel("WARN")


  def computeWordsCount(): Unit = {
    val data = sc.parallelize(AwsS3Integration.getLoadedArticleIds)
      .map(AwsS3Integration.getObject(_))
      .map(decode[Article](_))
      .map { case Right(article) => article }
      .flatMap(_.text.filter(!".,".contains(_)).split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)
      .filter(pair => pair._1.length > 3)
      .sortBy(pair => pair._2, false)
      .saveAsTextFile("c:/analytics_spark")

  }


  def main(args: Array[String]): Unit = {
    println("Start")
    computeWordsCount

    println("Done")
  }


}
