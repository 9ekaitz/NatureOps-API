package eus.natureops.natureops.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Achivement;
import eus.natureops.natureops.domain.AchivementUser;
import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.form.AchivementsForm;
import eus.natureops.natureops.repository.AchievementsUserRepository;
import eus.natureops.natureops.repository.AchivementRepository;
import eus.natureops.natureops.repository.UserRepository;
import eus.natureops.natureops.service.AchivementService;

@Service
public class AchievementServiceImp   implements AchivementService{

    @Autowired
    AchivementRepository achivementRepository;
    
    @Autowired
    AchievementsUserRepository achievementsUserRepository;

    @Autowired
    UserRepository userRepository;

    

    @Override
    public List<AchivementsForm> findAll(int page, int numOfNews, String username) {
        Pageable sortedByName = PageRequest.of(page,numOfNews, Sort.by("id").descending());
        List<Achivement> lista = achivementRepository.findByEnabledTrue(sortedByName).toList();
        User user = userRepository.findByUsername(username);
        List<AchivementUser> listaValores = achievementsUserRepository.findByUser(user);
        List<AchivementsForm> listaForm = new ArrayList<>();
        for(int i = 0; i < lista.size(); i++)
        {
            AchivementsForm form = new AchivementsForm();
            Achivement item = lista.get(i);
            form.setProgress(0);
            form.setAchivement(item);

            for(int j = 0; j < listaValores.size(); j++)
                if(listaValores.get(j).getAchivement().getId() == item.getId())
                    form.setProgress(listaValores.get(j).getProgress());

            listaForm.add(form);
            
        }
        return listaForm;
    }

   



    @Override
    public int achievementsSize() {
        return achivementRepository.findByEnabledTrue().size();
    }
    
}
