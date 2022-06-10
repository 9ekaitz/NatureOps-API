package eus.natureops.natureops.dto;

import eus.natureops.natureops.domain.Achievement;
import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class AchievementView {
  private Achievement achivement;
  private int progress;
}
