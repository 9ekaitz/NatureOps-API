package eus.natureops.natureops.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.dto.AchievementView;
import eus.natureops.natureops.repository.AchievementsUserRepository;
import eus.natureops.natureops.repository.AchivementRepository;
import eus.natureops.natureops.service.AchivementService;
import eus.natureops.natureops.service.UserService;

@Service
public class AchievementServiceImp implements AchivementService{

    @Autowired
    AchivementRepository achievementRepository;
    
    @Autowired
    AchievementsUserRepository achievementsUserRepository;

    @Autowired
    UserService userService;

    @Override
    public List<AchievementView> getPage(int page, int numOfNews, String username) {
        Pageable sortedById = PageRequest.of(page,numOfNews, Sort.by("id").descending());
        User user = userService.findByUsername(username);
        return achievementsUserRepository.findByUser(user, sortedById, AchievementView.class).toList();
    }

    @Override
    public int achievementsSize() {
        return achievementRepository.findByEnabledTrue().size();
    }
    
}
