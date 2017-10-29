package services

import java.io.File
import java.nio.file.Files

import com.github.tototoshi.csv.CSVWriter
import model.RichAddress

class OutputWriterService(
  matchedFile: File,
  unmatchedFile: File
) {

  def write(
    inputs: (scala.List[String], scala.List[Map[String, String]]),
    resolvedAddresses: Map[Int, RichAddress]
  ): Unit ={
    //Remove previous attempts so that the action can be retried after server has crashed
    Files.deleteIfExists(matchedFile.toPath)
    Files.deleteIfExists(unmatchedFile.toPath)

    val matchedWriter = CSVWriter.open(matchedFile)
    val unmatchedWriter = CSVWriter.open(unmatchedFile)

    val matchedHeaders = inputs._1 :+ "x" :+ "y"
    val unmatchedHeaders = inputs._1

    matchedWriter.writeRow(matchedHeaders)
    unmatchedWriter.writeRow(unmatchedHeaders)

    inputs._2.zipWithIndex.foreach{ case (row, i) =>
      resolvedAddresses.get(i) match{
        case Some(richAddress) =>
          val rowWithCoordinates = row + ("x" -> richAddress.geomX) + ("y" -> richAddress.geomY)
          matchedWriter.writeRow(matchedHeaders.map(rowWithCoordinates.apply))
        case None =>
          unmatchedWriter.writeRow(unmatchedHeaders.map(row.apply))
      }
    }

    matchedWriter.close()
    unmatchedWriter.close()
  }

}
