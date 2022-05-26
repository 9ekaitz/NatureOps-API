package eus.natureops.natureops.dto;

import java.util.Date;

import eus.natureops.natureops.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

@AllArgsConstructor
@Data
@Generated
public class EventSimpleView {
  private String name;
  private Date startDate;
  private String location;
  private User creator;
}
