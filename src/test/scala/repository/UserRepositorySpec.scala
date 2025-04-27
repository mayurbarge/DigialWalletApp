package repository

import domain.Currency._
import domain.{Money, User, Wallet}
import zio._
import zio.test._
import zio.test.Assertion._
object UserRepositorySpec extends ZIOSpecDefault {
  override def spec = suite("UserRepositorySpec") (
    test("should add user to the repository if not already added") {
      val repository = new UserRepository()
      for {
        ref <- repository.make
        _ <- repository.addUser(ref, User("Mayur"))
        users <- repository.getUsers(ref)
      }
      yield {
        assertTrue(
          users.head.name == "Mayur",
          users.head.balance == Money(BigDecimal(0),USD))
      }
    },

    test("should not add user to the repository if already added") {
      val repository = new UserRepository()
      val result: ZIO[Any, String, Unit] =
        for {
          ref <- repository.make
          _ <- repository.addUser(ref, User("Mayur"))
          ref <- repository.addUser(ref, User("Mayur"))
        }
        yield ref
      assertZIO(result.exit)(fails(equalTo("User already present.")))
    },

    test("should update wallet balance to 10 USD if user wallet has 2 USD and balance is updated to 10 USD") {
      val repository = new UserRepository()
      for {
        ref <- repository.make
        _ <- repository.addUser(ref, User("Mayur", Wallet(balance = Money(BigDecimal(2), USD))))
        _ <- repository.updateMoney(ref, "Mayur", Money(BigDecimal(10), USD))
        users <- repository.getUsers(ref)
      }
      yield {
        assertTrue(
          users.head.name == "Mayur",
          users.head.balance == Money(BigDecimal(10),USD))
      }
    },
  )
}