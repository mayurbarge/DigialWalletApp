package domain
import domain.Currency.USD
import io.jvm.uuid._
case class Wallet(id: UUID = UUID.random, balance: Money = Money(0, USD)) {
  def credit(other: Money) = this.copy(id, this.balance + other)
  def debit(other: Money) = this.copy(id, this.balance - other)

}

