package model

import play.api.libs.json._

case class Coordinates(lat: String, long: String)

object Coordinates {
  implicit val readsCoordinates: Reads[Coordinates] = (json: JsValue) => {
    for {
      lat <- (json \ "lat").validate[String]
      long <- (json \ "lon").validate[String]
    } yield Coordinates(lat, long)
  }
}