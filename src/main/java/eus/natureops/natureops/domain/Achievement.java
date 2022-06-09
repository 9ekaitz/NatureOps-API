package eus.natureops.natureops.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Entity
@Data
@Generated
public class Achievement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String desription;

  @Column
  private String image;

  @Column
  private String objetivo;

  @Column
  private boolean enabled;

  @Column
  private int objetivoMax;

  @Version
  @EqualsAndHashCode.Exclude
  private int version;
}
