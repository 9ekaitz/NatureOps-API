package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.Achivement;

public interface AchivementService {
  public Achivement save(Achivement achivement);

  public List<Achivement> findAll();

  public Achivement findById(Long id);
}
