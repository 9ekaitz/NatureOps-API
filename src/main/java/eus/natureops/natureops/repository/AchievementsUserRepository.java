package eus.natureops.natureops.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eus.natureops.natureops.domain.AchievementUser;
import eus.natureops.natureops.domain.AchievementUserKey;
import eus.natureops.natureops.domain.User;

public interface AchievementsUserRepository  extends JpaRepository<AchievementUser, AchievementUserKey>  {
    public <T> Page<T> findByUser(User user, Pageable pageable, Class<T> type);
}

