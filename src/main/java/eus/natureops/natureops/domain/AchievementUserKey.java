package eus.natureops.natureops.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.Generated;

@Data
@Generated
@Embeddable
public class AchievementUserKey implements Serializable {

  private static final long serialVersionUID = 42347061246458317L;

  @Column(name = "achievement_id")
  private Long achievementId;

  @Column(name = "user_id")
  private Long userId;
}
