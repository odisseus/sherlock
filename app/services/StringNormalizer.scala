package services

import play.api.Logger

object StringNormalizer {

  private val logger = Logger(this.getClass)

  private val replacementTable = "ĄĆĘŁŃÓÖŚÜŹŻ".zip("ACELNOOSUZZ").toList

  // FIXME There's a street called "OSIEDLE". Normalizing it leaves only whitespace
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
    val withoutPrefixes = (thoroughfarePrefixes ++ titlePrefixes).foldLeft(normalized){
      case (str, prefix) => str.replace(prefix, "")
    }
    val withoutInitials = withoutPrefixes
      .replaceAll("""[A-Z]\.""", "")

    val result = withoutInitials.trim
    logger.debug(s"Normalized '$input' to '$result'")
    if(result.length <= 3){
      logger.warn(s"Normalisation result is very short: '$input' to '$result'")
    }
    result
  }

}
