package repository

import domain.Payment
import zio.{Ref, UIO}
class PaymentRepository {
  type PaymentState = Ref[List[Payment]]
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
