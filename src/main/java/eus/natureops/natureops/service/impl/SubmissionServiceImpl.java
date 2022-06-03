package eus.natureops.natureops.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Submission;
import eus.natureops.natureops.repository.SubmissionRepository;
import eus.natureops.natureops.service.SubmissionService;

@Service
public class SubmissionServiceImpl implements SubmissionService{

  @Autowired
  private SubmissionRepository submissionRepository;

  @Override
  public Submission save(Submission image) {
    return submissionRepository.save(image);
  } 
}
