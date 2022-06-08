package eus.natureops.natureops.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Data @Generated @AllArgsConstructor @NoArgsConstructor
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @Column(unique = true)
  private String username;

  @Column
  private String password;

  @Column
  private String name;

  @Column
  private String email;

  @Column
  private boolean enabled;

  @ManyToOne
  @EqualsAndHashCode.Exclude
  private Role role;

  @Version
  @EqualsAndHashCode.Exclude
  private int version;
}
