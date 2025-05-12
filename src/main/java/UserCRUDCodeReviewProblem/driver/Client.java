package UserCRUDCodeReviewProblem.driver;

import UserCRUDCodeReviewProblem.entity.User;
import UserCRUDCodeReviewProblem.repository.UserRepository;
import UserCRUDCodeReviewProblem.service.UserService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
  public static void main(String[] args) {
    UserRepository repository = UserRepository.getInstance();
    UserService userService = new UserService(repository);

    // Create some initial users
    userService.createUser(new User("1", "Alice", "alice@example.com"));
    userService.createUser(new User("2", "Bob", "bob@example.com"));

    // Executor to simulate concurrent updates
    ExecutorService executor = Executors.newFixedThreadPool(4);

    // Task: Update user "1" concurrently
    Runnable updateTask = () -> {
      try {
        User updatedUser = userService.updateUser("1", "Alice Updated", "alice.new@example.com");
        System.out.println("Updated User: " + updatedUser);
      } catch (Exception e) {
        System.err.println("Error in updateTask: " + e.getMessage());
      }
    };

    // Task: Read user "1" concurrently
    Runnable readTask = () -> {
      try {
        User user = userService.getUser("1");
        System.out.println("Read User: " + user);
      } catch (Exception e) {
        System.err.println("Error in readTask: " + e.getMessage());
      }
    };

    // Task: Delete user "2" concurrently
    Runnable deleteTask = () -> {
      try {
        userService.deleteUser("2");
        System.out.println("Deleted User with id 2");
      } catch (Exception e) {
        System.err.println("Error in deleteTask: " + e.getMessage());
      }
    };

    // Execute tasks concurrently
    executor.submit(updateTask);
    executor.submit(readTask);
    executor.submit(deleteTask);
    executor.submit(readTask);

    // Shut down executor gracefully
    executor.shutdown();

  }
}
