package eus.natureops.natureops.domain;

import java.util.Date;

import lombok.Data;
import lombok.Generated;

@Data @Generated
public class Submission {
  private int id;
  private String score;
  private Date date;
  private Place place;
  private User user;
  private Location location;
  private String image;
}
