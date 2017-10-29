package services

import javax.inject.Inject

import model.Coordinates
import play.api.Logger
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OpenStreetMapService @Inject()(ws: WSClient) {

  private val baseUrl = "http://nominatim.openstreetmap.org/?format=json&city=krakow&country=poland&limit=1"

  private val logger = Logger(this.getClass)

  def resolveCoordinates(key: String): Future[Coordinates] = {
    ws.url(baseUrl)
      .addQueryStringParameters("street" -> key)
      .get().map {
      response => {
        val coordinates = (response.json \ "lat" \ "lon").as[Coordinates]
        logger.debug(s"Resoled coordinates: $coordinates")
        coordinates
      }
    }
  }
}
