package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.News;

public interface NewsService {
  public News save(News news);

  public List<News> findAll();
}
