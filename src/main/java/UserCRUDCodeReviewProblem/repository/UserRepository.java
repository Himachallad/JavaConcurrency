package UserCRUDCodeReviewProblem.repository;

import UserCRUDCodeReviewProblem.entity.User;
import UserCRUDCodeReviewProblem.exceptions.DuplicateUserException;
import UserCRUDCodeReviewProblem.exceptions.UserNotFoundException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
  private final ConcurrentHashMap<String, User> userStore = new ConcurrentHashMap<>();

  private static class Holder {
    private static final UserRepository INSTANCE = new UserRepository();
  }

  private UserRepository() {}

  public static UserRepository getInstance() {
    return Holder.INSTANCE;
  }

  public User create(User user) {
    if(userStore.putIfAbsent((user.getUserId()), user) != null) {
      throw new DuplicateUserException(user.getUserId());
    }
    return user;
  }

  public User read(String id) {
    User user = userStore.get(id);
    if(user == null) {
      throw new UserNotFoundException(id);
    }
    return user;
  }

  public User update(String id, String newName, String newEmail) {
//    return userStore.computeIfPresent(id, (key, user) -> {
//      user.setUserName(newName);
//      user.setEmail(newEmail);
//      return user;
//    });
    return userStore.computeIfPresent(id, (key, user) -> user.withUpdatedDetails(newName, newEmail));


  }

  public void delete(String id) {
    if(userStore.remove(id) == null) {
      throw new UserNotFoundException(id);
    }
  }

  public Collection<User> getAllUsers() {
    return userStore.values();
  }
}
