package services

import javax.inject.Inject

import model.RichAddress

class AddressResolutionService @Inject() (
  richAddressDictionary: RichAddressDictionary
) {

  def resolveAddresses(
    inputCsv: List[Map[String, String]],
    selectedColumns: List[String]
  ): Map[Int, RichAddress] = {
    println(selectedColumns)
    /**
      * It is assumed that the selected columns are always [UL, NR_BUD] in this order.
      * Real input data may have more or fewer columns.
      */
    val streetColumnHeader = selectedColumns(0)
    val buildingNumberColumnHeader = selectedColumns(1)
    val result = inputCsv.zipWithIndex.flatMap{
      case (row, i) =>
        val key = row(streetColumnHeader).normalizeStreetName()+row(buildingNumberColumnHeader).normalize()
        val richAddress = richAddressDictionary.richAddresses.get(key)
        richAddress.map(addr => i -> addr)
    }
    result.toMap
  }

}
