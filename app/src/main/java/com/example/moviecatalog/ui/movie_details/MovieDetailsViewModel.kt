package com.example.moviecatalog.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalog.data.repository.NetworkState
import com.example.moviecatalog.data.vo.movie_details.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel(private val movieRepository: MovieDetailsRepository, movieId: Int): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    /* We use `lazy` so get the movie details when we need them and not when we instantiate the class.
    * It would work without `lazy` regardless, but we use it for better performance. */
    val movieDetails: LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    /* The function `onCleared()` gets called when the activity or fragment gets destroyed,
    * so on this function we will dispose of our composite disposable to avoid memory leaks. */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
