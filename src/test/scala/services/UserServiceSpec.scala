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
          user.name == "Mayur",
          user.balance == Money(BigDecimal(0))
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

    test("should fail when user is not found") {
      val repository = new UserRepository()
      val userService = new UserService(repository)
      val result =
        for {
          ref <- repository.make
          _ <- userService.findUser(ref, "Mayur")
        }
        yield ()
      assertZIO(result.exit)(fails(equalTo("User Mayur not found.")))
    },

    test("should top up 5 USD to the users wallet having 10 USD") {
      val repository = new UserRepository()
      val userService = new UserService(repository)
      for {
        ref <- repository.make
        _ <- userService.addUser(ref, "Mayur")
        _ <- userService.topUpBalance(ref, "Mayur", Money(5, USD))
        _ <- userService.topUpBalance(ref, "Mayur", Money(10, USD))
        users <- repository.getUsers(ref)
      }
      yield {
        assertTrue(
          users.head.name == "Mayur",
          users.head.balance == Money(15, USD)
        )
      }
    },
  )
}