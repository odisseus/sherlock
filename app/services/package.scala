import com.github.tototoshi.csv.DefaultCSVFormat

package object services {

  implicit val csvFormat = new DefaultCSVFormat {
    override val delimiter: Char = ';'
  }
  implicit class NormalizeableString(val value: String) extends AnyVal{
    def normalize(): String = StringNormalizer.normalize(value)
    def normalizeStreetName(): String = StringNormalizer.normalizeStreetName(value)
  }

}
