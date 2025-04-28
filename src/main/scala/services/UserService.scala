package services

import domain.User
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
    } yield {
      users.find(_.name == name)
    }
  }
}

