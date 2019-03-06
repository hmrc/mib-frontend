package exceptions

final case class MibException(message: String) extends RuntimeException(message)

