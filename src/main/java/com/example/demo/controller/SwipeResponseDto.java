package com.example.movieapp.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SwipeResponseDto(
    String status,
    
    @JsonProperty("profileUpdated")
    boolean profileUpdated
) {}