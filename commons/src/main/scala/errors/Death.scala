package errors

case class Death() extends Throwable:
  override def getMessage: String = "You DIED !"
