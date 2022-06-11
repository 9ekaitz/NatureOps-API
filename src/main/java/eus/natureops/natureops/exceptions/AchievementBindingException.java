package eus.natureops.natureops.exceptions;

public class AchievementBindingException extends RuntimeException {
  public AchievementBindingException() {
    super("An error ocurred while binding the user with the achivements");
  }
}
