package services

import java.io.File

import model.RichAddress

/**
  * ULICA;ADR_NR;NAZWA_PRZEDROSTEK_A_CZESC;NAZWA_PRZEDROSTEK_B_CZESC;NAZWA_NAZWA_A_CZESC;NAZWA_GLOWNA_CZESC;
  * TYP_CIAGU_KOMUNIKACYJNEGO;TERYT_KOD;DZL_NUMER;DZL_NAZWA;GEOM_X;GEOM_Y
  */
class RichAddressDictionary(
  val dataSource: File
) extends CsvDictionary[RichAddress] {

  override val parseRow: Map[String, String] => RichAddress = row => RichAddress(
    street = row("ULICA"),
    number = row("ADR_NR"),
    namePrefixA = row("NAZWA_PRZEDROSTEK_A_CZESC"),
    namePrefixB = row("NAZWA_PRZEDROSTEK_B_CZESC"),
    nameAdditional = row("NAZWA_NAZWA_A_CZESC"),
    nameMain = row("NAZWA_GLOWNA_CZESC"),
    thoroughfareType = row("TYP_CIAGU_KOMUNIKACYJNEGO"),
    territoryCode = row("TERYT_KOD").toInt,
    boroughCode = row("DZL_NUMER").toInt,
    boroughName = row("DZL_NAZWA"),
    geomX = row("GEOM_X").toDouble,
    geomY = row("GEOM_Y").toDouble,
  )

}
