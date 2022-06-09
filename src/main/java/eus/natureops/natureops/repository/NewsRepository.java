package eus.natureops.natureops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eus.natureops.natureops.domain.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{
  
}
