package domain
import io.jvm.uuid._
import java.time.Instant
sealed trait PaymentStatus
case object PaymentCompleted extends PaymentStatus
case object PaymentFailed extends PaymentStatus
case class Payment(id: UUID = UUID.random, fromWallet: UUID, toWallet: UUID, money: Money, timestamp: Instant, status: PaymentStatus)
