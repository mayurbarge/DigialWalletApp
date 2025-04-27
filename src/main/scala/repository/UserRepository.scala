package repository
import domain.{Money, User}
import zio._
class UserRepository {
  type UserState = Ref[List[User]]
  def make: UIO[UserState] =
    Ref.make(List.empty[User])

  def addUser(ref: UserState, user: User): ZIO[Any, String, Unit] = {
    for {
      users <- ref.get
      _ <-
        if(users.exists(_.name == user.name))
          ZIO.fail("User already present.")
        else
          ref.update(users => user :: users)
    } yield ()
  }
  def getUsers(ref: UserState): UIO[List[User]] =
    ref.get

  def updateMoney(ref: UserState, username: String, newMoney: Money): UIO[Unit] =
    ref.update { users =>
      users.map {
        case user if user.name == username => user.copy(wallet = user.wallet.copy(balance = newMoney))
        case user => user
      }
    }
}

