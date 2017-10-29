package controllers

import java.io.File
import java.nio.file.{Files, Path}
import javax.inject._

import akka.stream.IOResult
import akka.stream.scaladsl._
import akka.util.ByteString
import com.github.tototoshi.csv.{CSVReader, CSVWriter}
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.streams._
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import play.core.parsers.Multipart.FileInfo
import services.{AddressResolutionService, CsvFileParseService, OutputWriterService, RichAddressDictionary}

import scala.concurrent.{ExecutionContext, Future}

case class FormData(name: String)

/**
 * This controller handles damn EVERYTHING.
 */
@Singleton
class HomeController @Inject() (cc:MessagesControllerComponents, rad: RichAddressDictionary)
                               (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = Logger(this.getClass)

  private val csvFileParseService = new CsvFileParseService

  val form = Form(
    mapping(
      "name" -> text
    )(FormData.apply)(FormData.unapply)
  )

  /**
   * Renders a start page.
   */
  def index = Action { implicit request =>
    Ok(views.html.index(form))
  }

  type FilePartHandler[A] = FileInfo => Accumulator[ByteString, FilePart[A]]

  /**
   * Uses a custom FilePartHandler to return a type of "File" rather than
   * using Play's TemporaryFile class.  Deletion must happen explicitly on
   * completion, rather than TemporaryFile (which uses finalization to
   * delete temporary files).
   *
   * @return
   */
  private def handleFilePartAsFile: FilePartHandler[File] = {
    case FileInfo(partName, filename, contentType) =>
      val path: Path = Files.createTempFile("multipartBody", "tempFile")
      val fileSink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(path)
      val accumulator: Accumulator[ByteString, IOResult] = Accumulator(fileSink)
      accumulator.map {
        case IOResult(count, status) =>
          logger.info(s"count = $count, status = $status")
          FilePart(partName, filename, contentType, path.toFile)
      }
  }

  private def operateOnFile(file: File) = {
    val size = Files.size(file.toPath)
    logger.info(s"size = ${size}")
    val parsedCsv = csvFileParseService.parse(file)
    parsedCsv
  }

  /**
   * Uploads a multipart file as a POST request.
   *
   * @return
   */
  def upload = Action(parse.multipartFormData(handleFilePartAsFile)) { implicit request =>
    val fileOption = request.body.file("Select file").map {
      case FilePart(key, filename, contentType, file) =>
        logger.info(s"key = ${key}, filename = ${filename}, contentType = ${contentType}, file = $file")
        file
    }
    fileOption.fold[Result](BadRequest){file =>
      Found(s"/columns/${file.getName}")
    }
  }

  def selectColumns(path: String) = Action { implicit request =>
    val file = new File(s"/tmp/$path")
    val parsedCsv = operateOnFile(file)
    Ok(views.html.columns(parsedCsv))
  }

  def computeResults(path: String, selectedColumns: String) = Action {
    val addressColumns = selectedColumns.split(",").toList
    val addressResolutionService = new AddressResolutionService(rad)
    /*
     * At this point the same file is being parsed for the second time.
     * But then again, this allows to try the same request after the server has crashed
     */
    val file = new File(s"/tmp/$path")
    val reader = CSVReader.open(file)(services.csvFormat)
    val data = reader.allWithOrderedHeaders()
    reader.close()
    val resolved = addressResolutionService.resolveAddresses(data._2, addressColumns)

    val matchedFile = new File(s"/tmp/$path-matched.csv")
    val unmatchedFile = new File(s"/tmp/$path-unmatched.csv")

    (new OutputWriterService(matchedFile, unmatchedFile)).write(data, resolved)

    Ok(Seq(matchedFile.getName, unmatchedFile.getName).toString())
  }
}
