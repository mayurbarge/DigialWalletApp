package domain

import zio.prelude.Validation
sealed trait Currency {
  val symbol: Char
}
object Currency {
  case object USD extends Currency {
    override val symbol: Char = '$'
    override def toString: String = symbol.toString
  }
}
case class Money(value: BigDecimal, currency: Currency = Currency.USD) {
  def +(other: Money) = Money(this.value + other.value)
  def -(other: Money) = Money(this.value - other.value)

  override def toString: String = value.toString + currency.toString
}
object Money {
  def validateMoney(value: BigDecimal) = {
    Validation.fromPredicateWith("Money can not be negative.")(value)(_ > 0) map (Money(_))
  }
}
