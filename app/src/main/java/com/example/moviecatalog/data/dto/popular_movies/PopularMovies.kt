package com.example.moviecatalog.data.dto.popular_movies


import com.google.gson.annotations.SerializedName

data class PopularMovies(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val popularMoviesItems: List<PopularMoviesItem>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)