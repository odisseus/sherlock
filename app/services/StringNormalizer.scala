package services

import play.api.Logger

object StringNormalizer {

  private val logger = Logger(this.getClass)

  private val replacementTable = "ĄĆĘŁŃÓÖŚÜŹŻ".zip("ACELNOOSUZZ").toList

  private val thoroughfarePrefixes = List(
    "AL.",
    "ALEJA",
    "UL.",
    "ULICA",
    "PL.",
    "PLAC",
    "OS.",
    "OSIEDLE",
    "RONDO",
    "RYNEK"
  )

  private val titlePrefixes = List(
    "GEN.",
    "GENERALA",
    "KS. BP.",
    "KS.",
    "KSIEDZA",
    "BP.",
    "BPA.",
    "ABP.",
    "ARCYBISKUPA",
    "MARSZ.",
    "MARSZALKA",
    "MJR.",
    "MAJORA",
    "POR.",
    "PORUCZNIKA",
    "PROF.",
    "PROFESORA",
    "PROFESOR",
    "DR.",
    "DOKTORA",
    "PLK.",
    "PULKOWNIKA",
    "RTM.",
    "ROTMISTRZA",
    "SW.",
    "SWIETEGO",
    "SWIETEJ"
  )

  def normalize(input: String): String = {
    replacementTable.foldLeft(input.trim.toUpperCase()){
      case (str, (from, to)) => str.replace(from, to)
    }
  }

  def normalizeStreetName(input: String): String = {
    val normalized = normalize(input)
    val result = normalizeSpecialCases.applyOrElse(normalized, normalizeGenericStreetName)
    logger.debug(s"Normalized '$input' to '$result'")
    if(result.length <= 3){
      logger.warn(s"Normalisation result is very short: '$input' to '$result'")
    }
    result
  }

  private def normalizeSpecialCases: PartialFunction[String, String] = {
    // There's a street called "OSIEDLE". Literally
    case "OSIEDLE" => "OSIEDLE"
    // Another case where one of the standard prefixes is important
    case "SWIETEGO KRZYZA" => "SWIETEGO KRZYZA"
    // Parsing or spelling errors that repeat a few times
    case x if x.contains("PETO;FIEGO") => "PETOFIEGO"
    case x if x.contains("BRU;CKNERA") => "BRUCKNERA"
    case x if x.contains("SZYSZKI-BOHUSZA") => "SZYSZKO-BOHUSZA"
    case x if x.contains("MLYNSKA-BOCZNA") => "MLYNSKA BOCZNA"
    // Some streets have shorter name forms in the dictionary than those occurring in the input data
    case x if x.contains("RADZIKOWSKIEGO") => "ELJASZA RADZIKOWSKIEGO"
    case x if x.contains("LELEWELA") => "BORELOWSKIEGO-LELEWELA"
    case x if x.contains("JUNOSZY") => "KLEMENSA JUNOSZY SZANIAWSKIEGO"
    case x if x.contains("LENARTOWICZA") => "LENARTOWICZA"
    case x if x.contains("JAWORSKIEGO") => "JAWORSKIEGO"
    case x if x.contains("MEISELSA") => "MEISELSA"
    case x if x.contains("FELINSKIEGO") => "FELINSKIEGO"
    case x if x.contains("WORONICZA") => "WORONICZA"
    case x if x.contains("MONIUSZKI") => "MONIUSZKI"
    case x if x.contains("SKRZETUSKIEGO") => "SKRZETUSKIEGO"
    case x if x.contains("SEWERA") => "SEWERA"
    case x if x.contains("CHODKIEWICZA") => "CHODKIEWICZA"
    case x if x.contains("WARSZAUERA") => "WARSZAUERA"
    case x if x.contains("SAPIEHY") => "SAPIEHY"
    case x if x.contains("GIERYMSKICH") => "GIERYMSKICH"
    case x if x.contains("WAWEL") => "WAWEL"
    case x if x.contains("REYMONTA") => "REYMONTA"
    case x if x.contains("ZWIRKI I WIGURY") => "ZWIRKI I WIGURY"
  }

  private def normalizeGenericStreetName(input: String): String = {
    val withoutPrefixes = (thoroughfarePrefixes ++ titlePrefixes).foldLeft(input){
      case (str, prefix) => str.replace(prefix, "")
    }
    val withoutInitials = withoutPrefixes
      .replaceAll("""[A-Z]\.""", "")

    withoutInitials.trim
  }

}
