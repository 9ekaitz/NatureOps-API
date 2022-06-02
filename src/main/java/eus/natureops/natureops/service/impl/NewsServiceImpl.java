package eus.natureops.natureops.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.News;
import eus.natureops.natureops.repository.NewsRepository;
import eus.natureops.natureops.service.NewsService;

@Service
public class NewsServiceImpl implements NewsService{

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public List<News> findAll(int page, int numOfNews) {
        Pageable sortedByName = PageRequest.of(page,numOfNews, Sort.by("id").descending());
        return newsRepository.findAll(sortedByName).toList();
    }

    @Override
    public int getNewsSize() {
        return newsRepository.findByEnabledTrue().size();
    }
    
}
