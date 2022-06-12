package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.News;
import eus.natureops.natureops.form.NewsForm;

public interface NewsService {
  public List<News> findAll(int page, int numOfNews);
  public int getNewsSize();
  public void createNews(NewsForm newsForm);
}
