package repository

import domain.Payment
import zio.{Ref, UIO}

object PaymentRepository {
  type PaymentState = Ref[List[Payment]]
}
class PaymentRepository {
  import repository.PaymentRepository._
  def make: UIO[PaymentState] =
    Ref.make(List.empty[Payment])

  def addPayment(ref: PaymentState, payment: Payment) =
    for {
      _ <- ref.get
      _ <- ref.update(payments => payment :: payments)
    } yield ()
  def getPayments(ref: PaymentState): UIO[List[Payment]] =
    ref.get
}
