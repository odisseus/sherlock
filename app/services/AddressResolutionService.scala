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
        val key = row(streetColumnHeader).normalizeStreetName()+row(buildingNumberColumnHeader).normalize()
        val upperBound = richAddressDictionary.richAddresses.from(key).headOption
        val lowerBound = richAddressDictionary.richAddresses.to(key).lastOption
        val matching = (upperBound.filter(_._1 == key))
          .orElse(lowerBound.filter(_._1 == key))
        if(matching.isEmpty){
          logger.debug(s"Failed to match '$key', closest options were '${lowerBound.map(_._1)}' and '${upperBound.map(_._1)}'")
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
