package eus.natureops.natureops.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import eus.natureops.natureops.validation.ValidPassword;
import lombok.Data;
import lombok.Generated;

@Data @Generated
public class UserRegistrationForm {
  @NotEmpty(message = "{page.register.field.username.notEmpty}")
  private String username;

  @ValidPassword(message = "Worng password")
  private String password;

  @NotEmpty(message = "{page.register.field.name.notEmpty}")
  private String name;

  @Email(message = "{page.register.field.email.valid}")
  private String email;
}
