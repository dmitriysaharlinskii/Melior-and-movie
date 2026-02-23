package com.example.movieapp.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public record MovieRecommendationDto(
    int id,
    String title,
    String overview,
    List<String> genres,
    
    @JsonProperty("voteAverage")
    double voteAverage,
    
    @JsonProperty("posterPath")
    String posterPath,
    
    @JsonProperty("backdropPath")
    String backdropPath,
    
    @JsonProperty("releaseDate")
    String releaseDate,
    
    @JsonProperty("trailerUrl")
    String trailerUrl,
    
    Map<String, Double> features,
    
    @JsonProperty("matchScore")
    double matchScore,
    
    @JsonProperty("aiExplanation")
    String aiExplanation
) {}