package in

import errors.MapError

trait ForValidatingMap:
  def validateAndStoreMap(dataLines: String): Either[MapError, Unit]
