package eus.natureops.natureops.service;

import java.util.List;

import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Achievement;
import eus.natureops.natureops.dto.AchievementView;

@Service
public interface AchievementService {
  public List<AchievementView> getPage(int page, int numOfNews, String username);
  public int achievementsSize();
  public List<Achievement> getAll();
}
