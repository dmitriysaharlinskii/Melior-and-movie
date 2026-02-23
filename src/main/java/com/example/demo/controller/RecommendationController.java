package com.example.movieapp.controller;

import com.example.movieapp.dto.api.MovieRecommendationDto;
import com.example.movieapp.dto.api.SwipeRequestDto;
import com.example.movieapp.dto.api.SwipeResponseDto;
import com.example.movieapp.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Получить следующий фильм для рекомендации
     * URL: /api/recommendations/next?context=evening
     */
    @GetMapping("/next")
    public ResponseEntity<MovieRecommendationDto> getNextRecommendation(
            @RequestParam(required = false) String context,
            @RequestHeader(value = "X-User-ID", required = false) Long userId) {
        
        // Логируем входящий запрос
        System.out.println("📱 Запрос рекомендации от пользователя: " + userId + ", контекст: " + context);
        
        // Получаем рекомендацию
        MovieRecommendationDto recommendation = recommendationService.getNextRecommendation(context);
        
        // Если нет рекомендаций - 204 No Content
        if (recommendation == null) {
            return ResponseEntity
                    .noContent()
                    .header("X-Total-Available", "0")
                    .header("Retry-After", "300")
                    .header("X-Response-Time", "30ms")
                    .build();
        }
        
        // Возвращаем рекомендацию с заголовками
        return ResponseEntity.ok()
                .header("X-Total-Available", "25")
                .header("X-Match-Score", String.format("%.2f", recommendation.matchScore()))
                .header("X-Recommendation-ID", "rec-" + recommendation.id())
                .header("X-Cache", "MISS")
                .header("X-Response-Time", "120ms")
                .body(recommendation);
    }

    /**
     * Обработать свайп пользователя
     * URL: /api/recommendations/swipe
     */
    @PostMapping("/swipe")
    public ResponseEntity<SwipeResponseDto> processSwipe(
            @RequestBody SwipeRequestDto swipeRequest,
            @RequestHeader(value = "X-Swipe-Timestamp", required = false) String timestamp) {
        
        System.out.println("👆 Свайп: фильм " + swipeRequest.movieId() + 
                          (swipeRequest.liked() ? " 👍" : " 👎") + 
                          (timestamp != null ? " в " + timestamp : ""));
        
        // Здесь будет логика обновления профиля пользователя в БД
        // Пока просто заглушка
        
        SwipeResponseDto response = new SwipeResponseDto("processed", true);
        
        return ResponseEntity.ok()
                .header("X-Profile-Updated", "true")
                .header("X-Recommendations-Affected", "15")
                .header("X-Response-Time", "25ms")
                .body(response);
    }

    /**
     * Тестовый endpoint (из вашей спецификации)
     */
    @GetMapping("/test")
    public String test() {
        return "Movie controller is working!";
    }
}