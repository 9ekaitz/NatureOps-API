package eus.natureops.natureops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.natureops.natureops.domain.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
  
}
