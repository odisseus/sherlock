package services

import java.io.{File, OutputStream, PrintWriter}
import java.nio.file.Files

import com.github.tototoshi.csv.CSVWriter
import model.RichAddress
import org.joda.time.{DateTime, Seconds}


class OutputWriterService(
  matchedSink: OutputStream,
  unmatchedSink: OutputStream,
  summarySink: OutputStream
) {

  def write(
    inputs: (scala.List[String], scala.List[Map[String, String]]),
    resolvedAddresses: Map[Int, RichAddress],
    startTime: DateTime
  ): Unit ={

    val matchedWriter = CSVWriter.open(matchedSink)
    val unmatchedWriter = CSVWriter.open(unmatchedSink)

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

    val endTime = DateTime.now()
    val summaryWriter = new PrintWriter(summarySink)
    summaryWriter.println(s"Time taken: ${Seconds.secondsBetween(startTime, endTime).getSeconds} seconds")
    summaryWriter.println(s"Finished at ${endTime}")
    summaryWriter.println(s"Matched ${resolvedAddresses.size} entries")
    summaryWriter.println(s"Failed to match ${inputs._2.size - resolvedAddresses.size} entries")
    summaryWriter.println(s"Total ${inputs._2.size} entries")
    summaryWriter.close()

  }

}
