package org.example.account;

public class Player implements Account {
  String name;
  String email;


  Player(String name, String email) {
    this.name = name;
    this.email = email;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getEmail() {
    return email;
  }
}
