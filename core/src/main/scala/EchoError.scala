/**
  * Trait for storing non-fatal errors
  */

trait EchoError{val message: String}

case class ParseError(message: String) extends EchoError
case class IntegrationError(message: String, exception: Throwable) extends EchoError

