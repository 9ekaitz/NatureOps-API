package eus.natureops.natureops.repository;

import eus.natureops.natureops.domain.Image;

public interface ImageRepository {
  public Image saveToDisk(Image image);
}
