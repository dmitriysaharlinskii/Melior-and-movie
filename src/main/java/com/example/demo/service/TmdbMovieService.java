package com.example.demo.service;

import com.example.movieapp.dto.tmdb.TmdbMovie;
import com.example.movieapp.dto.tmdb.TmdbMovieResponse;
import com.example.movieapp.exception.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Service
public class TmdbMovieService {

    private final RestClient tmdbRestClient;

    public TmdbMovieService(@Qualifier("tmdbRestClient") RestClient tmdbRestClient) {
        this.tmdbRestClient = tmdbRestClient;
    }

    /**
     * Получить популярные фильмы
     */
    public List<TmdbMovie> getPopularMovies(int page) {
        TmdbMovieResponse response = tmdbRestClient.get()
                .uri("/movie/popular?page={page}", page)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new RuntimeException("Ошибка TMDB API: " + res.getStatusCode());
                })
                .body(TmdbMovieResponse.class);

        List<TmdbMovie> tmdbMovies = response != null ? response.results() : (List<TmdbMovie>) List.of();
        return Collections.unmodifiableList(tmdbMovies);
    }

    /**
     * Получить фильм по ID
     */
    public TmdbMovie getMovieById(int movieId) {
        try {
            return tmdbRestClient.get()
                    .uri("/movie/{movieId}", movieId)
                    .retrieve()
                    .onStatus(status -> status == HttpStatus.NOT_FOUND, (req, res) -> {
                        throw new MovieNotFoundException("Фильм с ID " + movieId + " не найден");
                    })
                    .body(TmdbMovie.class);
        } catch (MovieNotFoundException e) {
            throw e; // пробрасываем дальше
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении фильма: " + e.getMessage(), e);
        }
    }

    /**
     * Поиск фильмов
     */
    public List<TmdbMovie> searchMovies(String query) {
        TmdbMovieResponse response = tmdbRestClient.get()
                .uri("/search/movie?query={query}", query)
                .retrieve()
                .body(TmdbMovieResponse.class);

        List<TmdbMovie> tmdbMovies;
        if (response != null) tmdbMovies = response.results();
        else tmdbMovies = (List<TmdbMovie>) List.of();
        List<TmdbMovie> tmdbMovies1 = tmdbMovies;
        return tmdbMovies1;
    }
}