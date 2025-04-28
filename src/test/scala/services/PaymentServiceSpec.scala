package services

import domain.Currency.USD
import domain._
import io.jvm.uuid.UUID
import repository.{PaymentRepository, UserRepository}
import zio.test.Assertion.{equalTo, fails}
import zio.test._
object PaymentServiceSpec extends ZIOSpecDefault {
  override def spec = suite("PaymentServiceSpec") (
    test("should transfer amount from one user's wallet to another") {
      val userRepository = new UserRepository()
      val userService = new UserService(userRepository)
      val paymentRepository = new PaymentRepository()
      val paymentService = new PaymentService(userRepository, paymentRepository)

      for {
        usersRef <- userRepository.make
        paymentsRef <- paymentRepository.make
        _ <- userService.addUser(usersRef, "Mayur")
        _ <- userService.addUser(usersRef, "Mahesh")
        _ <- userRepository.updateMoney(usersRef, "Mayur", Money(BigDecimal(120), USD))
        _ <- paymentService.transfer(usersRef, paymentsRef, "Mayur", "Mahesh", Money(BigDecimal(20), USD))
        users <- userRepository.getUsers(usersRef)
        payments <- paymentRepository.getPayments(paymentsRef)
      }
      yield {
        assertTrue(
          payments.head.status == PaymentCompleted,
          payments.head.money ==  Money(BigDecimal(20), USD),
          payments.head.fromWallet == users.find(_.name == "Mayur").map(_.wallet.id).getOrElse(UUID.random),
          payments.head.toWallet == users.find(_.name == "Mahesh").map(_.wallet.id).getOrElse(UUID.random),
          users.find(_.name == "Mayur").map(_.balance).getOrElse(Money()) == Money(BigDecimal(100), USD),
          users.find(_.name == "Mahesh").map(_.balance).getOrElse(Money()) == Money(BigDecimal(20), USD)
        )
      }
    },

    test("should fail if balance is insufficient") {
      val userRepository = new UserRepository()
      val userService = new UserService(userRepository)
      val paymentRepository = new PaymentRepository()
      val paymentService = new PaymentService(userRepository, paymentRepository)

      val result =
        for {
          usersRef <- userRepository.make
          paymentsRef <- paymentRepository.make
          _ <- userService.addUser(usersRef, "Mayur")
          _ <- userService.addUser(usersRef, "Mahesh")
          _ <- userRepository.updateMoney(usersRef, "Mayur", Money(BigDecimal(20), USD))
          r <- paymentService.transfer(usersRef, paymentsRef, "Mayur", "Mahesh", Money(BigDecimal(30), USD))
        }
        yield r
      assertZIO(result.exit)(fails(equalTo("Insufficient balance.")))
    },

    test("should fail if receiver user is not found") {
      val userRepository = new UserRepository()
      val userService = new UserService(userRepository)
      val paymentRepository = new PaymentRepository()
      val paymentService = new PaymentService(userRepository, paymentRepository)

      val result =
        for {
          usersRef <- userRepository.make
          paymentsRef <- paymentRepository.make
          _ <- userService.addUser(usersRef, "Mayur")
          r <- paymentService.transfer(usersRef, paymentsRef, "Mayur", "Mahesh", Money(BigDecimal(30), USD))
        }
        yield r
      assertZIO(result.exit)(fails(equalTo("Receiver user 'Mahesh' not found.")))
    },

    test("should fail if sender user is not found") {
      val userRepository = new UserRepository()
      val paymentRepository = new PaymentRepository()
      val paymentService = new PaymentService(userRepository, paymentRepository)

      val result =
        for {
          usersRef <- userRepository.make
          paymentsRef <- paymentRepository.make
          r <- paymentService.transfer(usersRef, paymentsRef, "Mayur", "Mahesh", Money(BigDecimal(30), USD))
        }
        yield r
      assertZIO(result.exit)(fails(equalTo("Sender user 'Mayur' not found.")))
    },
  )
}