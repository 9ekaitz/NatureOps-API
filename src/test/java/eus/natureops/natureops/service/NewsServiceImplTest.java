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

import eus.natureops.natureops.domain.News;
import eus.natureops.natureops.repository.NewsRepository;
import eus.natureops.natureops.service.impl.NewsServiceImpl;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    NewsServiceImpl newsServiceImpl;

    @Mock
    NewsRepository newsRepository;

    @Test
    void testFindAll() {
        List<News> lista = new ArrayList<>();
        News noticia1 = new News(0, "titulo1", "subtitulo1", "content1", "image1","google.com", true, 0);
        News noticia2 = new News(1, "titulo2", "subtitulo2", "content2", "image2", "google.com",true, 0);
        lista.add(noticia1);
        lista.add(noticia2);

        Pageable sortedByName = PageRequest.of(0, 2, Sort.by("id").descending());
        Page<News> page = new PageImpl<>(lista);
        when(newsRepository.findByEnabledTrue(sortedByName)).thenReturn(page);
        assertEquals(lista.toString(), newsServiceImpl.findAll(0, 2).toString());
    }

    @Test
    void testNewsSize() {
        List<News> lista = new ArrayList<>();
        News noticia1 = new News(0, "titulo1", "subtitulo1", "content1", "image1","google.com", true, 0);
        News noticia2 = new News(1, "titulo2", "subtitulo2", "content2", "image2","google.com", true, 0);
        lista.add(noticia1);
        lista.add(noticia2);

        when(newsRepository.findByEnabledTrue()).thenReturn(lista);
        assertEquals(2, newsServiceImpl.getNewsSize());
    }

    @Test
    void testCreteNews() {
        News noticia1 = new News(0, "titulo1", "subtitulo1", "content1", "image1","google.com", true, 0);
        newsRepository.save(noticia1);
    }

}
