package eus.natureops.natureops.repository.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import eus.natureops.natureops.repository.ImageRepository;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
  @Override
  public String save(MultipartFile file, String name) throws IOException {
    File convFile = new File(ABSOLUTE_SUBMISSION_PATH + name+".png");
    convFile.getParentFile().mkdirs();
    if (convFile.createNewFile()) {
      try (FileOutputStream out = new FileOutputStream(convFile)) {
        out.write(file.getBytes());
      } catch (Exception e) {
        return null;
      }
    }
    return ABSOLUTE_SUBMISSION_PATH.concat(name+".png");
  }
}
