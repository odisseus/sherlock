package services

import javax.inject.Inject

import model.RichAddress
import play.api.Logger

class AddressResolutionService @Inject() (
  richAddressDictionary: RichAddressDictionary
) {

  private val logger = Logger(this.getClass)

  def resolveAddresses(
    inputCsv: List[Map[String, String]],
    selectedColumns: List[String]
  ): Map[Int, RichAddress] = {
    /**
      * It is assumed that the selected columns are always [UL, NR_BUD] in this order.
      * Real input data may have more or fewer columns.
      */
    val streetColumnHeader = selectedColumns(0)
    val buildingNumberColumnHeader = selectedColumns(1)
    val result = inputCsv.zipWithIndex.flatMap{
      case (row, i) =>
        val key = row(streetColumnHeader).normalizeStreetName() + "|" +row(buildingNumberColumnHeader).normalize()
        val matching = richAddressDictionary.richAddresses.find(_._1.contains(key))
        if(matching.isEmpty){
          logger.debug(s"Failed to match '$key'")
        }
        matching.map{
          case (addressKey, richAddress) =>
            if(key != addressKey){
              logger.debug(s"Imprecise match: $key to $addressKey")
            }
            (i -> richAddress)
        }
    }
    result.toMap
  }
  
}
