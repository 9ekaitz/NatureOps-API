package eus.natureops.natureops.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eus.natureops.natureops.domain.Achivement;

@Repository
public interface AchivementRepository  extends JpaRepository<Achivement, Long>  {
    public List<Achivement> findByEnabledTrue();
    public Page  findByEnabledTrue(Pageable pageable);

}

