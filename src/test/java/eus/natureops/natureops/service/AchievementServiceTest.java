package eus.natureops.natureops.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import eus.natureops.natureops.domain.Achievement;
import eus.natureops.natureops.domain.AchievementUser;
import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.dto.AchievementView;
import eus.natureops.natureops.repository.AchievementsUserRepository;
import eus.natureops.natureops.repository.AchivementRepository;
import eus.natureops.natureops.service.impl.AchievementServiceImp;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @InjectMocks
    AchievementServiceImp achievementServiceImp;

    @Mock
    AchivementRepository achivementRepository;

    @Mock
    UserService userService;
    @Mock
    AchievementsUserRepository achievementsUserRepository;

    @Test
    void testAchievementSize() {
        List<Achievement> lista = new ArrayList<>();
        Achievement achivement = new Achievement();
        lista.add(achivement);
        when(achivementRepository.findByEnabledTrue()).thenReturn(lista);
        assertEquals(1, achievementServiceImp.achievementsSize());
    }

    @Test
    void testGetAchievement() {
        User user = new User(1L, "dummy", "password", "name", "email", true, null, 1);
        List<AchievementView> listAchivement = new ArrayList<>();
        AchievementUser achivementUser = new AchievementUser();
        Achievement achivement = new Achievement();
        achivement.setId(0);
        achivementUser.setProgress(50);
        achivementUser.setAchievement(achivement);
        achivementUser.setUser(user);

        Pageable sortedById = PageRequest.of(0, 3, Sort.by("id").descending());
        Page<AchievementView> page = new PageImpl<>(listAchivement);

        when(userService.findByUsername("eka")).thenReturn(user);

        when(achievementsUserRepository.findByUser(user, sortedById, AchievementView.class)).thenReturn(page);

        assertEquals(achievementServiceImp.getPage(0, 3, "eka"), listAchivement);

    }

}
