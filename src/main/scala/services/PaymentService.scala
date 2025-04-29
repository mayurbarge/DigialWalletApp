package services

import domain.{Money, Payment, PaymentCompleted}
import repository.{PaymentRepository, UserRepository}
import repository.PaymentRepository.PaymentState
import repository.UserRepository.UserState
import zio.ZIO

import java.time.Instant

class PaymentService(userRepository: UserRepository, paymentRepository: PaymentRepository) {
  def transfer(userRef: UserState, paymentRef: PaymentState,
               senderUser: String, receiverUser: String, money: Money) = {
    for {
      users <- userRepository.getUsers(userRef)
      from <- ZIO.fromOption(users.find(_.name == senderUser)).orElseFail(s"Sender user '$senderUser' not found.")
      to <- ZIO.fromOption(users.find(_.name == receiverUser)).orElseFail(s"Receiver user '$receiverUser' not found.")
      _ <- if(from.wallet.balance >= money) {
        ZIO.collectAll(List(
          userRepository.updateMoney(userRef, senderUser, from.wallet.balance - money),
          userRepository.updateMoney(userRef, receiverUser, to.wallet.balance + money),
          paymentRepository.addPayment(paymentRef, Payment(fromWallet = from.wallet.id, toWallet = to.wallet.id,
            money = money, timestamp = Instant.now(), status = PaymentCompleted
            ))
        ))
        }
        else ZIO.fail("Insufficient balance.")
    } yield {
    }
  }
  def getAllPayments(paymentRef: PaymentState) = paymentRepository.getPayments(paymentRef)

  def getAllPaymentsByUser(userRef: UserState, paymentRef: PaymentState, user: String) = {
    for {
      users <- userRepository.getUsers(userRef)
      payments <- paymentRepository.getPayments(paymentRef)
      walletId <- ZIO.fromOption(users.find(_.name == user).map(_.wallet.id)).orElseFail("User not found.")
    } yield payments.filter(_.fromWallet == walletId)
  }

}
