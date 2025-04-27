package domain

import io.jvm.uuid.UUID
import zio.Scope
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue}

import java.time.Instant
object PaymentSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("PaymentSpec") (
    test("should get created with a unique id") {
      val payment = Payment(UUID.random, UUID.random, UUID.random, Money(), Instant.now(), PaymentFailed)
      assertTrue(payment.id.isInstanceOf[UUID])
    }
  )
}
