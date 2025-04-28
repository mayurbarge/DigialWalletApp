package repository

import domain.Currency._
import domain._
import io.jvm.uuid._
import zio.test._

import java.time.Instant
object PaymentRepositorySpec extends ZIOSpecDefault {
  override def spec = suite("PaymentRepositorySpec") (
    test("should add payment to the repository") {
      val repository = new PaymentRepository()
      for {
        ref <- repository.make
        _ <- repository.addPayment(ref, Payment(UUID.random, UUID.random, UUID.random, Money(2, USD), Instant.now(), PaymentCompleted))
        payments <- repository.getPayments(ref)
      }
      yield {
        assertTrue(
          payments.head.id.isInstanceOf[UUID],
          payments.head.money.value == BigDecimal(2),
          payments.head.status == PaymentCompleted
        )
      }
    },
  )
}