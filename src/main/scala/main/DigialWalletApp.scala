package main

import zio._
import zio.Console._
import repository.{PaymentRepository, UserRepository}
import domain.{Currency, Money, Payment, User}
import services.{PaymentService, UserService}

import java.io.IOException

object DigitalWalletApp extends ZIOAppDefault {

  val userRepository = new UserRepository()
  val paymentRepository = new PaymentRepository()
  val userService = new UserService(userRepository)
  val paymentService = new PaymentService(userRepository, paymentRepository)

  case class AppState(users: Ref[List[User]],
                      transactions: Ref[List[Payment]])

  def makeAppState: UIO[AppState] = for {
    usersRef <- userRepository.make
    paymentsRef <- paymentRepository.make
  } yield AppState(usersRef, paymentsRef)

  def menuLoop(state: AppState): ZIO[Any, Any, Unit] = {
    for {
      _ <- printMenu
      choice <- readLine
      _ <- choice match {
        case "1" => registerUser(state) *> menuLoop(state)
        case "2" => topUpWallet(state) *> menuLoop(state)
        case "3" => transferMoney(state) *> menuLoop(state)
        case "4" => checkBalance(state) *> menuLoop(state)
        case "5" => getAllPayments(state) *> menuLoop(state)
        case "6" => getAllPaymentsByUser(state) *> menuLoop(state)
        case "q" => Console.printLine("Goodbye!")
        case _   => Console.printLine("Invalid choice.") *> menuLoop(state)
      }
    } yield ()
  }

  def printMenu: ZIO[Any, IOException, Unit] =
    Console.printLine(
      """
        |Select an option:
        |1. Register a user
        |2. Top up money into your digital wallet
        |3. Transfer money to another wallet on the same system
        |4. Check balance
        |5. Get All Payments
        |6. Get Payments by User
        |(q to quit)
        |""".stripMargin)

  def registerUser(state: AppState): ZIO[Any, Any, Unit] = for {
    _ <- Console.printLine("Enter username:")
    name <- readLine
    _ <- userService.addUser(state.users, name)
    _ <- Console.printLine("User added successfully.")
  } yield ()

  def parseAmount(str: String): IO[Throwable, BigDecimal] =
    ZIO.attempt(BigDecimal(str)).orElseFail(new RuntimeException("Invalid amount"))

  def topUpWallet(state: AppState): ZIO[Any, Throwable, Unit] = for {
    _ <- Console.printLine("Enter username:")
    name <- readLine
    _ <- Console.printLine("Enter amount to top up:")
    amountStr <- readLine
    amount <- parseAmount(amountStr)
    _ <- userService.topUpBalance(state.users, name, Money(amount))
    _ <- Console.printLine("Top-up successful.")
  } yield ()

  def transferMoney(state: AppState): ZIO[Any, Any, Unit] = for {
    _ <- Console.printLine("Enter sender username:")
    fromUser <- readLine
    _ <- Console.printLine("Enter receiver username:")
    toUser <- readLine
    _ <- Console.printLine("Enter amount to transfer:")
    amountStr <- readLine
    amount <- parseAmount(amountStr)
    _ <- paymentService.transfer(state.users, state.transactions, fromUser, toUser, Money(amount))
    _ <- Console.printLine("Transfer successful.")
  } yield ()

  def checkBalance(state: AppState): ZIO[Any, Any, Unit] = for {
    _ <- Console.printLine("Enter username:")
    name <- readLine
    balance <- userService.findUser(state.users, name).map(_.balance)
    _ <- Console.printLine(s"Balance: $balance")
  } yield ()

  def getAllPayments(state: AppState): ZIO[Any, IOException, Unit] = for {
    _ <- Console.printLine("The payments done in this session are:")
    payments <- paymentService.getAllPayments(state.transactions)
    _ <- ZIO.foreach(payments)(payment => ZIO.succeed(println(payment))).unit
  } yield ()

  def getAllPaymentsByUser(state: AppState): ZIO[Any, Any, Unit] = for {
    _ <- Console.printLine("Enter username:")
    name <- readLine
    _ <- Console.printLine("The transactions done by this user are:")
    payments <- paymentService.getAllPaymentsByUser(state.users, state.transactions, name)
    _ <- ZIO.foreach(payments)(payment => ZIO.succeed(println(payment))).unit
  } yield ()
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = for {
    state <- makeAppState
    _ <- menuLoop(state)
  } yield ()
}
