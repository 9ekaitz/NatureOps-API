package eus.natureops.natureops.service;

import java.util.List;

import org.springframework.stereotype.Service;

import eus.natureops.natureops.dto.AchievementView;

@Service
public interface AchivementService {
  public List<AchievementView> getPage(int page, int numOfNews, String username);
  public int achievementsSize();
}
