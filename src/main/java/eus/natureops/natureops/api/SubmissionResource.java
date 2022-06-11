package eus.natureops.natureops.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.domain.Submission;
import eus.natureops.natureops.exceptions.AMQPCommunicationException;
import eus.natureops.natureops.exceptions.SubmissionReadingException;
import eus.natureops.natureops.form.ImageSubmit;
import eus.natureops.natureops.repository.ImageRepository;
import eus.natureops.natureops.service.SubmissionService;
import eus.natureops.natureops.service.UserService;

@RestController
@RequestMapping("/api")
public class SubmissionResource {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private SubmissionService submissionService;

  @Autowired
  private UserService userService;

  @PostMapping(value = "/submit", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<String> submitImage(HttpServletRequest request, HttpServletResponse response,
      ImageSubmit imageSubmit, Authentication auth) {
    Submission submission = new Submission();
    submission.setLocation(imageSubmit.getLocation());
    submission.setUser(userService.findByUsername(auth.getName()));
    submission = submissionService.save(submission);

    String path = null;
    try {
      path = imageRepository.save(imageSubmit.getImage(), auth.getName() + "_" +
          submission.getId());
    } catch (IOException e) {
      throw new SubmissionReadingException();
    }
    submission.setPath(path);
    submission = submissionService.save(submission);
    JSONObject jsonRes = null;
    try {
      byte[] res = (byte[]) rabbitTemplate.convertSendAndReceive("amq.topic", "rest.submission",
          imageSubmit.getImage().getBytes());
      jsonRes = new JSONObject(new String(res));
    } catch (AmqpException | IOException e) {
      throw new AMQPCommunicationException();
    }

    submission.setScore(jsonRes.getString("score"));
    submissionService.save(submission);

    return ResponseEntity.ok(jsonRes.toString());
  }
}
