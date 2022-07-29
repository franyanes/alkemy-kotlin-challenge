package com.example.moviecatalog.data.api

import com.example.moviecatalog.data.vo.movie_details.MovieDetails
import com.example.moviecatalog.data.vo.popular_movies.PopularMovies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/* This interface will be used to fetch the data from the API. */

interface TheMovieDBInterface {
    /* `Query` will link the `page` parameter to the page item in the URL. */
    @GET("movie/popular")
    fun getPopularMovies(@Query("page")page: Int): Single<PopularMovies>

    /* `Path` links the `movie_id` from the GET to the `id` parameter. */
    /* Single is one type of observable in Reactivex or RxJava.
    *  It emits a single value (instead of multiple), or an error. */
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id")id: Int): Single<MovieDetails>
}
