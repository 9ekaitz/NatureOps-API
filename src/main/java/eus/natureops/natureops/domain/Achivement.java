package eus.natureops.natureops.domain;

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
public class Achivement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String desription;
  private String image;
  private String objetivo;
  private boolean enabled;
  private int objetivoMax;


  @Version
  @EqualsAndHashCode.Exclude
  private int version;
}
