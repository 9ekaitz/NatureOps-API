package eus.natureops.natureops.service;

import java.util.List;

import org.springframework.stereotype.Service;
import eus.natureops.natureops.form.AchivementsForm;

@Service
public interface AchivementService {
  public List<AchivementsForm> findAll(int page, int numOfNews, String username);
  public int achievementsSize();
}
