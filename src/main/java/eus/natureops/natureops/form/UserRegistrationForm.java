package eus.natureops.natureops.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import eus.natureops.natureops.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationForm {
  @NotEmpty(message = "{page.register.field.username.notEmpty}")
  private String username;

  @ValidPassword(message = "Worng password", upperCaseRule = true, specialCharacterRule = true, digitRule = true, lowerCaseRule = true)
  private String password;

  @NotEmpty(message = "{page.register.field.name.notEmpty}")
  private String name;

  @Email(message = "{page.register.field.email.valid}")
  private String email;
}
