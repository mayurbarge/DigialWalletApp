package domain

import domain.UserSpec.test
import zio.test._
import zio.test.Assertion._
import zio.prelude.Validation
import io.jvm.uuid._

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
      val result = User.validatePerson("Mayur")
      val id = result.toOption.map(_.id.isInstanceOf[UUID])
      assertTrue(id == Option(true))
    }
  )
}
