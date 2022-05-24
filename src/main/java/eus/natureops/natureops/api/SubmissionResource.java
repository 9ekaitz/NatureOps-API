package eus.natureops.natureops.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.json.JSONObject;

import eus.natureops.natureops.form.ImageSubmit;

@RestController
@RequestMapping("/api")
public class SubmissionResource {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @PostMapping(value = "/submit", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<?> submitImage(HttpServletRequest request, HttpServletResponse response,
      ImageSubmit imageSubmit) {
    String score;
    String res = null;
    try {
      JSONObject msg = new JSONObject();
      msg.put("id", imageSubmit.getUsername());
      msg.put("image", new String(Base64Utils.encode(imageSubmit.getImage().getBytes())));
      String json = msg.toString();
      res = (String) rabbitTemplate.convertSendAndReceive("amq.topic", "rest.submission", json);
      JSONObject jsonRes = new JSONObject(res);
    } catch (AmqpException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return ResponseEntity.ok(res);
  }
}
