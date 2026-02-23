package com.example.movieapp.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SwipeRequestDto(
    @JsonProperty("movieId")
    int movieId,
    
    boolean liked
) {}