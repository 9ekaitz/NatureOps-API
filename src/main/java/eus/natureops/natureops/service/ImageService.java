package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.Image;

public interface ImageService {
  public Image save(Image image);

  public List<Image> findAll();

  public Image findById(Long id);
}
