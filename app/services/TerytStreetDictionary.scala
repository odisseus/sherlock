package services

import java.io.File

import com.github.tototoshi.csv.CSVReader
import model.TerytStreet


class TerytStreetDictionary(
  dataSource: File
) {

  def loadAll(): List[TerytStreet] ={
    val reader = CSVReader.open(dataSource)(CsvFileParseService.csvFormat)
    val data = reader.allWithHeaders()
    println(data.size)
    val result = data.map{ case row => TerytStreet(
      voivodeship = row("WOJ").toInt,
      county = row("POW").toInt,
      municipality = row("GMI").toInt,
      municipalityType = row("RODZ_GMI").toInt,
      placeId = row("SYM").toInt,
      streetId = row("SYM_UL").toInt,
      streetType = row("CECHA"),
      streetNameMain = row("NAZWA_1"),
      streetNameAdditional = row("NAZWA_2"),
      updatedAt = row("STAN_NA")
    )}
    reader.close()
    result
  }

}
