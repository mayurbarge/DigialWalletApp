package services

import domain.{Money, User}
import repository.UserRepository
import repository.UserRepository.UserState
import zio.ZIO
class UserService(userRepository: UserRepository) {
  def addUser(ref: UserState, name: String) = {
    for {
      users <- userRepository.getUsers(ref)
      result <-
      if(users.map(_.name).contains(name))
        ZIO.fail("User already present.")
      else
        User.validateUser(name).toZIO.flatMap(userRepository.addUser(ref, _))
    } yield {
      result
    }
  }
  def findUser(ref: UserState, name: String) = {
    for {
      users <- userRepository.getUsers(ref)
      user <- ZIO.fromOption(users.find(_.name == name)).orElseFail(s"User $name not found.")
    } yield user
  }
  def topUpBalance(ref: UserState, name: String, topUpMoney: Money) = {
    for {
      _ <- userRepository.addMoney(ref, name, topUpMoney)
    } yield ()
  }
}

