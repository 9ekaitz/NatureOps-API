package eus.natureops.natureops.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Entity
@Data
@Generated
public class Submission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String score;

  @Temporal(value=TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date date;

  // private Place place;
  @ManyToOne
  private User user;
  private String location;
  private String path;

  @Version
  @EqualsAndHashCode.Exclude
  private int version;
}
