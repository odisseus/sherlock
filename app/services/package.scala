import com.github.tototoshi.csv.DefaultCSVFormat

package object services {

  implicit val csvFormat = new DefaultCSVFormat {
    override val delimiter: Char = ';'
  }

}
