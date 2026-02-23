package com.example.movieapp.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TmdbMovie(
    int id,
    String title,
    String overview,
    
    @JsonProperty("genre_ids")
    List<Integer> genreIds,
    
    @JsonProperty("vote_average")
    double voteAverage,
    
    @JsonProperty("poster_path")
    String posterPath,
    
    @JsonProperty("backdrop_path")
    String backdropPath,
    
    @JsonProperty("release_date")
    String releaseDate
) {}