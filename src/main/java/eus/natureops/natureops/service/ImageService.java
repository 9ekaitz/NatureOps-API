package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.Submission;

public interface ImageService {
  public Submission save(Submission image);

  public List<Submission> findAll();

  public Submission findById(Long id);
}
