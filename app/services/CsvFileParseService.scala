package services

import java.io.File
import com.github.tototoshi.csv.CSVReader
import model.CsvTable

class CsvFileParseService {

  def parse(file: File): CsvTable = {
    val reader = CSVReader.open(file)
    val data = reader.all()
    val result = CsvTable(
      header = data.head,
      rows = data.tail
    )
    println(data.size)
    reader.close()
    result
  }

}
