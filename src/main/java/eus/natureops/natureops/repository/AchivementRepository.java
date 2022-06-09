package eus.natureops.natureops.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eus.natureops.natureops.domain.Achievement;

@Repository
public interface AchivementRepository  extends JpaRepository<Achievement, Long>  {
    public List<Achievement> findByEnabledTrue();
    public Page<Achievement>  findByEnabledTrue(Pageable pageable);

}

