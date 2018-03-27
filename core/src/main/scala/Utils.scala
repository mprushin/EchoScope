import scala.util.Either

/**
  * Extension for Either to provide map and flatMap functions
  */
trait Utils {
  implicit class EitherMap[A, B](e: Either[A, B]) {
    def map[B1](f: B => B1): Either[A, B1] = e match {
      case Right(a) => Right(f(a))
      case _       => e.asInstanceOf[Either[A, B1]]
    }

    def flatMap[A1>:A, B1](f: B => Either[A1, B1]): Either[A1, B1] = e match {
      case Right(a) => f(a)
      case _ =>e.asInstanceOf[Either[A1, B1]]
    }
  }

}
