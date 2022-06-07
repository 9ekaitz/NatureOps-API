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

import eus.natureops.natureops.domain.Achivement;
import eus.natureops.natureops.domain.AchivementUser;
import eus.natureops.natureops.domain.News;
import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.form.AchivementsForm;
import eus.natureops.natureops.repository.AchievementsUserRepository;
import eus.natureops.natureops.repository.AchivementRepository;
import eus.natureops.natureops.repository.NewsRepository;
import eus.natureops.natureops.repository.UserRepository;
import eus.natureops.natureops.service.impl.AchievementServiceImp;

@ExtendWith(MockitoExtension.class)
class AchivementServiceTest {
    
    @InjectMocks
    AchievementServiceImp achievementServiceImp;

    @Mock
    AchivementRepository achivementRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    AchievementsUserRepository achievementsUserRepository;

    @Test
    void testAchivementSize()
    {
        List<Achivement> lista = new ArrayList<>();
        Achivement achivement = new Achivement();
        lista.add(achivement);
        when(achivementRepository.findByEnabledTrue()).thenReturn(lista);
        assertEquals(achievementServiceImp.achievementsSize(),1);
    }

    @Test
    void testNewsSize()
    {
        List<Achivement> lista = new ArrayList<>();
        List<AchivementUser> listAchivement = new ArrayList<>();
        AchivementUser achivementUser = new AchivementUser();
        Achivement achivement = new Achivement();
        achivement.setId(0);
        lista.add(achivement);
        achivementUser.setProgress(50);
        achivementUser.setAchivement(achivement);
        listAchivement.add(achivementUser);

      
        
        Pageable sortedByName = PageRequest.of(0,3, Sort.by("id").descending());
        Page page = new PageImpl<>(lista);
        User user = new User();
        user.setId(0);
        when(achivementRepository.findByEnabledTrue(sortedByName)).thenReturn(page);
        when(userRepository.findByUsername("eka")).thenReturn(user);
        when(achievementsUserRepository.findByUser(user)).thenReturn(listAchivement);

        List<AchivementsForm> listAchivementsForms = new ArrayList<>();
        AchivementsForm achivementsForm = new AchivementsForm();
        achivementsForm.setAchivement(achivement);
        achivementsForm.setProgress(50);
        listAchivementsForms.add(achivementsForm);
        assertEquals(achievementServiceImp.findAll(0,3,"eka"), listAchivementsForms);


    }
    
}
