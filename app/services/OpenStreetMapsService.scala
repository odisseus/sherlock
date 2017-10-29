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
        Thread.sleep(500)
        val foo = (response.json)(0)
        val lat = foo \ "lat"
        val lon = foo \ "lon"
        val coordinates = foo.as[Coordinates]
        logger.debug(s"Resolved coordinates: $coordinates")
        coordinates
      }
    }
  }
}
