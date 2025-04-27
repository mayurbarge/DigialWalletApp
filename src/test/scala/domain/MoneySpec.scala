package domain

import domain.Currency.USD
import zio.prelude.Validation
import zio.test._

object MoneySpec extends ZIOSpecDefault {
  override def spec = suite("MoneySpec")(
    test("should fail when money is negative") {
      val result = Money.validateMoney(BigDecimal(-10))
      assertTrue(
        result == Validation.fail("Money can not be negative.")
      )
    },

    test("should pass when money is a positive amount") {
      val result = Money.validateMoney(BigDecimal(1))
      assertTrue(result == Validation.succeed(Money(BigDecimal(1), USD)))
    },

    test("should add 5 USD to 2 USD to get 7 USD") {
      val result = Money(2, USD) + Money(5, USD)
      assertTrue(result == Money(7, USD))
    },

    test("should subtract 2 USD from 5 USD to get 3 USD") {
      val result = Money(5, USD) - Money(2, USD)
      assertTrue(result == Money(3, USD))
    },

    test("should print 2$ for 2 USD") {
      assertTrue(Money(2, USD).toString == "2$")
    },

  )
}
