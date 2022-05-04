package eus.natureops.natureops.repository;

import eus.natureops.natureops.domain.Achivement;

public interface AchivementRepository {
  public Achivement findByName(String name);
}
