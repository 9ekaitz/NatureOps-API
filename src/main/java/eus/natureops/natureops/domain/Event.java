package eus.natureops.natureops.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;
  private Date startDate;
  private Date endDate;
  private String location;
  private String description;
  private boolean enabled;

  @ManyToMany
  @JoinTable(name = "event_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<User> enrolledUsers;

  @ManyToOne
  private User creator;

  @Version
  private int version;
}
