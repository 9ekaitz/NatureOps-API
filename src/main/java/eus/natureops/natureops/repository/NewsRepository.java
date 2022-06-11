package eus.natureops.natureops.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eus.natureops.natureops.domain.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
  public Page<News> findByEnabledTrue(Pageable pageable);

  public List<News> findByEnabledTrue();
}
