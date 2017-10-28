package model

case class CsvFile(
  header: List[String],
  rows: List[List[String]]
)
