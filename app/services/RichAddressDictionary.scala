package services

import java.io.File
import javax.inject._

import model.RichAddress

import scala.collection.SortedMap
import scala.collection.immutable.TreeMap

/**
  * ULICA;ADR_NR;NAZWA_PRZEDROSTEK_A_CZESC;NAZWA_PRZEDROSTEK_B_CZESC;NAZWA_NAZWA_A_CZESC;NAZWA_GLOWNA_CZESC;
  * TYP_CIAGU_KOMUNIKACYJNEGO;TERYT_KOD;DZL_NUMER;DZL_NAZWA;GEOM_X;GEOM_Y
  */
@Singleton
class RichAddressDictionary extends CsvDictionary[RichAddress] {

  override val dataSource = new File("conf/static/Słownik Adresów.csv")

  lazy val richAddresses: SortedMap[String, RichAddress] = {
    val data = this.loadAll()
    val withKeys = data.map{
      case address => (address.nameMain+address.number, address)
    }
    SortedMap.empty[String, RichAddress] ++ withKeys
  }

  override val parseRow: Map[String, String] => RichAddress = row => RichAddress(
    street = row("ULICA").normalize(),
    number = row("ADR_NR").normalize(),
    namePrefixA = row("NAZWA_PRZEDROSTEK_A_CZESC").normalize(),
    namePrefixB = row("NAZWA_PRZEDROSTEK_B_CZESC").normalize(),
    nameAdditional = row("NAZWA_NAZWA_A_CZESC").normalize(),
    nameMain = row("NAZWA_GLOWNA_CZESC").normalize(),
    thoroughfareType = row("TYP_CIAGU_KOMUNIKACYJNEGO"),
    territoryCode = row("TERYT_KOD").replace("brak", "0").toInt,
    boroughCode = row("DZL_NUMER").toInt,
    boroughName = row("DZL_NAZWA").normalize(),
    geomX = row("GEOM_X").replace(',','.').toDouble,
    geomY = row("GEOM_Y").replace(',','.').toDouble,
  )

}
