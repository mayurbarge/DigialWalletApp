package repository

import domain.Currency._
import domain.{Money, User, Wallet}
import zio.test._

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

    test("should update the money for the user") {
      val repository = new UserRepository()
      for {
        ref <- repository.make
        _ <- repository.addUser(ref, User("Mayur"))
        _ <- repository.updateMoney(ref, "Mayur", Money(23, USD))
        users <- repository.getUsers(ref)
      }
      yield {
        assertTrue(
          users.head.name == "Mayur",
          users.head.balance == Money(BigDecimal(23),USD))
      }
    },

  test("should add 1 USD to the balance for the user having 121 USD") {
    val repository = new UserRepository()
    for {
      ref <- repository.make
      _ <- repository.addUser(ref, User("Mayur", Wallet(balance = Money(1, USD))))
      _ <- repository.addMoney(ref, "Mayur", Money(121, USD))
      users <- repository.getUsers(ref)
    }
    yield {
      assertTrue(
        users.head.name == "Mayur",
        users.head.balance == Money(BigDecimal(122),USD))
    }
  },
  )
}