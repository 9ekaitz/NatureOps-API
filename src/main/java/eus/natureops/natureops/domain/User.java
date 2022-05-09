package eus.natureops.natureops.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Data @Generated @AllArgsConstructor @NoArgsConstructor
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  private String username;
  private String password;
  private String name;
  private String surname;
  private String image;
  private boolean enabled;

  @ManyToOne
  private Role role;

  @Version
  private int version;
}
