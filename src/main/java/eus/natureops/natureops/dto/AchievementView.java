package eus.natureops.natureops.dto;

import eus.natureops.natureops.domain.Achievement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

@Data
@Generated
@AllArgsConstructor
public class AchievementView {
  private Achievement achievement;
  private int progress;
}
