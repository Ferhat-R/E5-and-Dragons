package errors

object MapError:
  final case class IllegalMapFormat() extends MapError
sealed trait MapError extends Throwable
