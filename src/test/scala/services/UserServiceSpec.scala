package services

import domain.Currency._
import domain._
import io.jvm.uuid._
import repository.{PaymentRepository, UserRepository}
import zio.test.Assertion.{equalTo, fails}
import zio.test._

import java.time.Instant

object UserServiceSpec extends ZIOSpecDefault {
  override def spec = suite("UserServiceSpec") (
    test("should save a user when name is not empty") {
      val repository = new UserRepository()
      val userService = new UserService(repository)
      for {
        ref <- repository.make
        _ <- userService.addUser(ref, "Mayur")
        user <- userService.findUser(ref, "Mayur")
      }
      yield {
        assertTrue(
          user.map(_.name).getOrElse("") == "Mayur",
          user.map(_.balance).getOrElse(Money(BigDecimal(99))) == Money(BigDecimal(0))
        )
      }
    },

    test("should fail when a user when name is empty") {
      val repository = new UserRepository()
      val userService = new UserService(repository)
      val result =
        for {
          ref <- repository.make
          r <- userService.addUser(ref, "")
        }
        yield r
      assertZIO(result.exit)(fails(equalTo("Name can not be empty.")))
    },

    test("should fail when a user is already present") {
      val repository = new UserRepository()
      val userService = new UserService(repository)
      val result =
        for {
          ref <- repository.make
          _ <- userService.addUser(ref, "Mayur")
          r <- userService.addUser(ref, "Mayur")
        }
        yield r
      assertZIO(result.exit)(fails(equalTo("User already present.")))
    },
  )
}