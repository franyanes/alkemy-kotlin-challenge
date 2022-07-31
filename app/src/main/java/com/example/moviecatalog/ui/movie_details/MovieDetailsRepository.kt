package com.example.moviecatalog.ui.movie_details

import androidx.lifecycle.LiveData
import com.example.moviecatalog.data.api.TheMovieDBInterface
import com.example.moviecatalog.data.repository.MovieDetailsNetworkDataSource
import com.example.moviecatalog.data.repository.NetworkState
import com.example.moviecatalog.data.dto.movie_details.MovieDetails
import io.reactivex.disposables.CompositeDisposable

/* This class seems like an extra step, but it's created in case we want to cache the data in the future. */

class MovieDetailsRepository(private val apiService: TheMovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)
        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}
