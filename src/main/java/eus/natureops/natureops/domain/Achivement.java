package eus.natureops.natureops.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Data @Generated @AllArgsConstructor @NoArgsConstructor
public class Achivement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String dexription;
  private String image;
  private String objetivo;
  private boolean enabled;
  
  private User user;
}
