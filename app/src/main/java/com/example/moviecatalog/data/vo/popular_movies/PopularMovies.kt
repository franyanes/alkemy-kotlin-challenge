package com.example.moviecatalog.data.vo.popular_movies


import com.google.gson.annotations.SerializedName

data class PopularMovies(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val popularMovieItems: List<PopularMovieItem>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)