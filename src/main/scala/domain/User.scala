package domain
import zio.prelude.Validation
import io.jvm.uuid._

case class User(name: String, wallet: Wallet = Wallet()) {
  val id: UUID = UUID.random
  def balance = wallet.balance
}
object User {
  def validateName(name: String): Validation[String, String] =
    Validation.fromPredicateWith("Name can not be empty.")(name)(_.nonEmpty)
  def validatePerson(name: String): Validation[String, User] =
    validateName(name).map(User(_))
}