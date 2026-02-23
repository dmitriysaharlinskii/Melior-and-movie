package com.example.movieapp.service;

import com.example.movieapp.dto.api.MovieRecommendationDto;
import com.example.movieapp.dto.tmdb.TmdbMovie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final com.example.movieapp.service.TmdbMovieService tmdbService;
    private final String imageBaseUrl;
    
    // Маппинг ID жанров TMDB в названия
    private static final Map<Integer, String> GENRE_MAP = Map.ofEntries(
        Map.entry(28, "боевик"),
        Map.entry(12, "приключения"),
        Map.entry(16, "анимация"),
        Map.entry(35, "комедия"),
        Map.entry(80, "криминал"),
        Map.entry(99, "документальный"),
        Map.entry(18, "драма"),
        Map.entry(10751, "семейный"),
        Map.entry(14, "фэнтези"),
        Map.entry(36, "история"),
        Map.entry(27, "ужасы"),
        Map.entry(10402, "музыка"),
        Map.entry(9648, "детектив"),
        Map.entry(10749, "мелодрама"),
        Map.entry(878, "фантастика"),
        Map.entry(10770, "телевизионный фильм"),
        Map.entry(53, "триллер"),
        Map.entry(10752, "военный"),
        Map.entry(37, "вестерн")
    );

    public RecommendationService(com.example.movieapp.service.TmdbMovieService tmdbService,
                                 @Value("${tmdb.api.image-base-url}") String imageBaseUrl) {
        this.tmdbService = tmdbService;
        this.imageBaseUrl = imageBaseUrl;
    }

    /**
     * Получить следующую рекомендацию
     */
    public MovieRecommendationDto getNextRecommendation(String context) {
        // Получаем популярные фильмы
        List<TmdbMovie> popularMovies = tmdbService.getPopularMovies(1);
        
        if (popularMovies.isEmpty()) {
            return null; // вернет 204 No Content
        }
        
        // Берем случайный фильм (для демонстрации)
        // В реальном проекте здесь будет алгоритм рекомендаций
        Random random = new Random();
        TmdbMovie movie = popularMovies.get(random.nextInt(popularMovies.size()));
        
        // Конвертируем в DTO для нашего API
        return convertToDto(movie, context);
    }

    /**
     * Конвертация из TMDB Movie в наш DTO
     */
    private MovieRecommendationDto convertToDto(TmdbMovie movie, String context) {
        // Конвертируем ID жанров в названия
        List<String> genreNames = movie.genreIds().stream()
                .map(id -> GENRE_MAP.getOrDefault(id, "другой"))
                .collect(Collectors.toList());
        
        // Формируем полные URL для изображений
        String posterUrl = movie.posterPath() != null ? 
                imageBaseUrl + movie.posterPath() : null;
        String backdropUrl = movie.backdropPath() != null ? 
                imageBaseUrl + movie.backdropPath() : null;
        
        // Генерируем "фичи" для демонстрации
        Map<String, Double> features = generateFeatures(movie);
        
        // Вычисляем "оценку соответствия"
        double matchScore = calculateMatchScore(movie, context);
        
        // Генерируем AI-объяснение
        String explanation = generateAIExplanation(movie, matchScore);
        
        return new MovieRecommendationDto(
            movie.id(),
            movie.title(),
            movie.overview(),
            genreNames,
            movie.voteAverage(),
            posterUrl,
            backdropUrl,
            movie.releaseDate(),
            null, // trailerUrl - можно добавить отдельным запросом
            features,
            matchScore,
            explanation
        );
    }

    /**
     * Генерация фич (для демонстрации)
     */
    private Map<String, Double> generateFeatures(TmdbMovie movie) {
        Map<String, Double> features = new HashMap<>();
        features.put("сюжет", 0.7 + Math.random() * 0.3);
        features.put("визуальные_эффекты", 0.6 + Math.random() * 0.4);
        features.put("актерская_игра", 0.8 + Math.random() * 0.2);
        return features;
    }

    /**
     * Расчет оценки соответствия (для демонстрации)
     */
    private double calculateMatchScore(TmdbMovie movie, String context) {
        // Базовая оценка на основе рейтинга
        double baseScore = movie.voteAverage() / 10.0; // 0.0 - 1.0
        
        // Добавляем случайность для демонстрации
        double randomFactor = 0.7 + (Math.random() * 0.3);
        
        // Корректировка по контексту
        if ("evening".equals(context)) {
            // Вечером выше оцениваем драмы
            if (movie.genreIds().contains(18)) { // драма
                baseScore += 0.1;
            }
        }
        
        return Math.min(1.0, baseScore * randomFactor);
    }

    /**
     * Генерация AI-объяснения
     */
    private String generateAIExplanation(TmdbMovie movie, double matchScore) {
        int percent = (int) (matchScore * 100);
        return String.format("На %d%% соответствует вашему вкусу. Этот фильм популярен " +
                "среди пользователей с похожими предпочтениями. Рекомендуем к просмотру!", percent);
    }
}