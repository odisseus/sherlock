package services

object StringNormalizer {

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
    "RYNEK"
  )

  private val titlePrefixes = List(
    "GEN.",
    "GENERALA",
    "KS.", "BP.",
    "KS.",
    "KSIEDZA",
    "BP.",
    "BPA.",
    "ABP.",
    "ARCYBISKUPA",
    "MARSZ.",
    "MARSZALKA",
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
    }.trim
    println(s"Normalized '$input' to '$withoutPrefixes'")
    withoutPrefixes
  }

}
