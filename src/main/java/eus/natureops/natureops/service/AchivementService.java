package eus.natureops.natureops.service;

import java.util.List;

import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Achivement;
import eus.natureops.natureops.form.AchivementsForm;

@Service
public interface AchivementService {
  public Achivement save(Achivement achivement);
  public List<AchivementsForm> findAll(int page, int numOfNews, String username);
  public int achievementsSize();
  public Achivement findById(Long id);
}
