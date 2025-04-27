package domain

import domain.Currency.USD
import io.jvm.uuid._
import zio.prelude.Validation
import zio.test._

object UserSpec extends ZIOSpecDefault {
  override def spec = suite("UserSpec")(
    test("should fail when name is empty") {
      val result = User.validateName("")
      assertTrue(
        result == Validation.fail("Name can not be empty.")
      )
    },

    test("should pass when name is not empty") {
      val result: Validation[String, String] = User.validateName("Mayur")
      assertTrue(result == Validation.succeed("Mayur"))
    },

    test("should create user with valid uuid when name is valid") {
      val result = User.validateUser("Mayur")
      val id = result.toOption.map(_.id.isInstanceOf[UUID])
      assertTrue(id == Option(true))
    },

    test("should create user with empty wallet with 0 USD") {
      val user = User("TestUser")
      assertTrue(user.wallet.id.isInstanceOf[UUID])
      assertTrue(user.balance == Money(BigDecimal(0), USD))
    },
  )
}
