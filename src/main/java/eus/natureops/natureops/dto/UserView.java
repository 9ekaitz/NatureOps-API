package eus.natureops.natureops.dto;

import eus.natureops.natureops.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

@Data
@AllArgsConstructor
@Generated
public class UserView {
  private long id;
  private String username;
  private String name;
  private String email;
}
