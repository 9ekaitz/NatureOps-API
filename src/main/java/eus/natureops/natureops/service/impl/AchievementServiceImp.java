package eus.natureops.natureops.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Achivement;
import eus.natureops.natureops.repository.AchivementRepository;
import eus.natureops.natureops.service.AchivementService;

@Service
public class AchievementServiceImp   implements AchivementService{

    @Autowired
    AchivementRepository achivementRepository;

    @Override
    public Achivement save(Achivement achivement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Achivement> findAll(int page, int numOfNews) {
        Pageable sortedByName = PageRequest.of(page,numOfNews, Sort.by("id").descending());
        return achivementRepository.findByEnabledTrue(sortedByName).toList();
    }

   

    @Override
    public Achivement findById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int achievementsSize() {
        return achivementRepository.findByEnabledTrue().size();
    }
    
}
