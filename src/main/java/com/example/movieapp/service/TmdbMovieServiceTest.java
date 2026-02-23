package com.example.movieapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TmdbMovieServiceTest {

    @Autowired
    private TmdbMovieService tmdbMovieService;

    @Test
    void testGetPopularMovies() {
        var movies = tmdbMovieService.getPopularMovies(1);
        assertThat(movies).isNotEmpty();
        System.out.println("Найдено фильмов: " + movies.size());
        System.out.println("Первый фильм: " + movies.get(0).title());
    }
}