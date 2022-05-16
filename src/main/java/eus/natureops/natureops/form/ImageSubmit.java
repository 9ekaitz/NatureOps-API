package eus.natureops.natureops.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Generated;

@Data @Generated
public class ImageSubmit {

  private MultipartFile image;
  private String location;
}
