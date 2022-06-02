package eus.natureops.natureops.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.Data;
import lombok.Generated;

@Entity
@Data
@Generated
public class News {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String title;

  @Column
  private String subtitle;

  @Column
  private String content;

  @Column
  private String image;

  @Column
  private boolean enabled;

  @Version
  private int version;
}
