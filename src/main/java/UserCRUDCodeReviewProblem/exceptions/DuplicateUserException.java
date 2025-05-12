package UserCRUDCodeReviewProblem.exceptions;

public class DuplicateUserException extends RuntimeException {
  public DuplicateUserException(String userId) {
    super("User already exists with id: " + userId);
  }
}
