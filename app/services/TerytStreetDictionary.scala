package services

import java.io.File

import model.TerytStreet


class TerytStreetDictionary(
  val dataSource: File
) extends CsvDictionary[TerytStreet] {

  override val parseRow: Map[String, String] => TerytStreet = row =>
    TerytStreet(
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
    )

}
