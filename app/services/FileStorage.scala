package services

import java.io._

class FileStorage(
 directoryPath: String
                 ) {

  def writingToFile[T](file: File)(action: OutputStream => T): T = {
    val outputStream = new FileOutputStream(file)
    try{
      action(outputStream)
    } finally {
      outputStream.close()
    }
  }

  def writingToFile[T](filename: String)(action: OutputStream => T): T = {
    val outputFile = new File(s"$directoryPath/$filename")
    writingToFile(outputFile)(action)
  }

  def readingFromFile[T](file: File)(action: InputStream => T): T = {
    val inputStream = new FileInputStream(file)
    try{
      action(inputStream)
    } finally {
      inputStream.close()
    }
  }

  def readingFromFile[T](filename: String)(action: InputStream => T): T = {
    val inputFile = new File(s"$directoryPath/$filename")
    readingFromFile(inputFile)(action)
  }

}
