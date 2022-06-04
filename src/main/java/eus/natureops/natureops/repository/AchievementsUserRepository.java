package eus.natureops.natureops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eus.natureops.natureops.domain.Achivement;
import eus.natureops.natureops.domain.AchivementUser;
import eus.natureops.natureops.domain.AchivementUserKey;
import eus.natureops.natureops.domain.User;

public interface AchievementsUserRepository  extends JpaRepository<AchivementUser, AchivementUserKey>  {
    public List<AchivementUser> findByUser(User user);
}

