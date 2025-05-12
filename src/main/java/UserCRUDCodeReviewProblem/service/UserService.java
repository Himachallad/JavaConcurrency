package UserCRUDCodeReviewProblem.service;

import UserCRUDCodeReviewProblem.entity.User;
import UserCRUDCodeReviewProblem.exceptions.UserNotFoundException;
import UserCRUDCodeReviewProblem.repository.UserRepository;
import java.util.Collection;

public class UserService {
  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(User user) {
    return userRepository.create(user);
  }

  public User getUser(String id) {
    return userRepository.read(id);
  }

  public User updateUser(String id, String name, String email) {
    User updatedUser = userRepository.update(id, name, email);
    if(updatedUser == null) {
      throw new UserNotFoundException(id);
    }
    return updatedUser;
  }

  public void deleteUser(String id) {
    userRepository.delete(id);
  }

  public Collection<User> listAllUsers() {
    return userRepository.getAllUsers();
  }

}
