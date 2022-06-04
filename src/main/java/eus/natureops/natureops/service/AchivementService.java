package eus.natureops.natureops.service;

import java.util.List;

import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Achivement;

@Service
public interface AchivementService {
  public Achivement save(Achivement achivement);
  public List<Achivement> findAll();
  public Achivement findById(Long id);
}
