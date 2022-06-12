package eus.natureops.natureops.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.News;
import eus.natureops.natureops.form.NewsForm;
import eus.natureops.natureops.repository.NewsRepository;
import eus.natureops.natureops.service.NewsService;

@Service
public class NewsServiceImpl implements NewsService {

  @Autowired
  private NewsRepository newsRepository;

  @Override
  public List<News> findAll(int page, int numOfNews) {
    Pageable sortedByName = PageRequest.of(page, numOfNews, Sort.by("id").descending());
    return newsRepository.findByEnabledTrue(sortedByName).toList();
  }

  @Override
  public int getNewsSize() {
    return newsRepository.findByEnabledTrue().size();
  }

  @Override
  public void createNews(NewsForm newsForm) {
    News newItem = new News();
    newItem.setTitle(newsForm.getTitle());
    newItem.setSubtitle(newsForm.getSubtitle());
    newItem.setContent(newsForm.getContent());
    newItem.setImage(newsForm.getImage());
    newItem.setUrl(newsForm.getUrl());
    newItem.setEnabled(true);

    newsRepository.save(newItem);
  }
}
