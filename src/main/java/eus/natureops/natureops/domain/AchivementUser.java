package eus.natureops.natureops.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Version;

import lombok.Data;
import lombok.Generated;

@Entity
@Data
@Generated
public class AchivementUser {

  @EmbeddedId
  private AchivementUserKey id;

  @ManyToOne
  @MapsId("achivementId")
  @JoinColumn(name = "achivement_id")
  private Achivement achivement;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @Column
  private int progress;

  @Version
  private int version;
}
