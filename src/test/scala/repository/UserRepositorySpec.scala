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
  )
}