package UserCRUDCodeReviewProblem.entity;

public class User {
  private String userId;
  private String userName;
  private String email;

  public User(String userId, String userName, String email) {
    if (userId == null || userId.isEmpty()) {
      throw new IllegalArgumentException("User id cannot be null or empty");
    }
    this.userId = userId;
    this.userName = userName;
    this.email = email;
  }

  public User withUpdatedDetails(String newName, String newEmail) {
    return new User(this.userId, newName, newEmail);
  }

  public String getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "User{" +
           "userId='" + userId + '\'' +
           ", userName='" + userName + '\'' +
           ", email='" + email + '\'' +
           '}';
  }
}
