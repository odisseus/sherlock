package model

case class CsvTable(
  header: List[String],
  rows: List[List[String]]
)
