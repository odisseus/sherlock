package services

import javax.inject.Inject

import model.RichAddress
import play.api.Logger

class AddressResolutionService @Inject() (
  richAddressDictionary: RichAddressDictionary
) {

  private val loggerImprecise = Logger("resolver-imprecise")
  private val loggerFailed = Logger("resolver-failed")

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
        val x = row(streetColumnHeader).normalizeStreetName()
        val y = row(buildingNumberColumnHeader).toUpperCase().replaceAll("""\s""", "")
        val matching = richAddressDictionary.richAddresses.find { case (key, address) =>
          isMatching(address,x, y)
        }
        if(matching.isEmpty){
          loggerFailed.debug(s"Failed to match '$key'")
        }
        matching.map{
          case (addressKey, richAddress) =>
            if(key != addressKey){
              loggerImprecise.debug(s"Imprecise match: $key to $addressKey")
            }
            (i -> richAddress)
        }
    }
    result.toMap
  }

  def isMatching(address: RichAddress, streetName: String, buildingNumber: String) = {
    buildingNumber == address.number &&
      streetName.contains(address.nameMain) &&
    address.street.contains(streetName)
  }

}
