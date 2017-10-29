package services

import java.io.File

import com.github.tototoshi.csv.CSVReader

trait CsvDictionary[T] {

  protected val dataSource: File

  val parseRow: Map[String, String] => T

  def loadAll(): List[T] ={
    val reader = CSVReader.open(dataSource)
    val data = reader.allWithHeaders()
    val result = data.map(parseRow)
    reader.close()
    result
  }

}
