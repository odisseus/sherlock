package services

import java.io.File
import com.github.tototoshi.csv.DefaultCSVFormat
import com.github.tototoshi.csv.CSVReader
import model.CsvFile

class CsvFileParseService {

  def parse(file: File): CsvFile = {
    val reader = CSVReader.open(file)(CsvFileParseService.csvFormat)
    val data = reader.all()
    val result = CsvFile(
      header = data.head,
      rows = data.tail
    )
    println(data.size)
    reader.close()
    result
  }

}

object CsvFileParseService {
  val csvFormat = new DefaultCSVFormat {
    override val delimiter: Char = ';'
  }
}
