package eus.natureops.natureops.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Achivement> findAll() {
        return achivementRepository.findByEnabledTrue();
    }

    @Override
    public Achivement findById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
