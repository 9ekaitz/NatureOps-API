package eus.natureops.natureops.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String username;
  private String password;
  private String name;
  private String surname;
  private String email;
  private String image;
  private boolean enabled;

  @ManyToOne
  private Role role;

  @ManyToMany(mappedBy = "enrolledUsers")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<Event> enrolledEvents;

  @Version
  private int version;
}
