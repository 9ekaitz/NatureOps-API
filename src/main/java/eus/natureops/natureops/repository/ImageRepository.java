package eus.natureops.natureops.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface ImageRepository {

    public static final String BASE_PATH = System.getProperty("user.dir");
    public static final String RELATIVE_SUBMISSION_PATH = "/img/submissons/";
    public static final String ABSOLUTE_SUBMISSION_PATH = BASE_PATH + RELATIVE_SUBMISSION_PATH;
    
  public String save(MultipartFile file);
}
