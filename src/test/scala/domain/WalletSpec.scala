package domain

import domain.Currency.USD
import domain.MoneySpec.test
import zio.prelude.Validation
import zio.test._
import io.jvm.uuid._
object WalletSpec extends ZIOSpecDefault {
  override def spec = suite("WalletSpec")(
    test("should create empty wallet with 0 USD Money and unique id") {
      val result = Wallet()
      assertTrue(result.id.isInstanceOf[UUID])
      assertTrue(result.balance == Money(0, USD))
    },

    test("should increase balance to 10 USD if wallet with 6 USD is credited by 4 USD") {
      val result = Wallet(balance = Money(6, USD)).credit(Money(4, USD))
      assertTrue(result.balance == Money(10, USD))
    },

    test("should decrease balance to 10 USD if wallet with 16 USD is debited by 6 USD") {
      val result = Wallet(balance = Money(16, USD)).debit(Money(6, USD))
      assertTrue(result.balance == Money(10, USD))
    },
  )
}
