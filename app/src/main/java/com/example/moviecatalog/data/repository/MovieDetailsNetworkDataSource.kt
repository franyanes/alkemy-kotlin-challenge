package com.example.moviecatalog.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviecatalog.data.api.TheMovieDBInterface
import com.example.moviecatalog.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/* Here we will call the API using RxJava and assign the movie details to a LiveData */

class MovieDetailsNetworkDataSource(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable /* CompositeDisposable: Is an RxJava component we can use to dispose our API calls */
) {
    private val _networkState = MutableLiveData<NetworkState>() // We use MutableLiveData because LiveData is not mutable by nature.
    val networkState: LiveData<NetworkState> // Getter
        get() = _networkState
    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>() // We use MutableLiveData because LiveData is not mutable by nature.
    val downloadedMovieDetailsResponse: LiveData<MovieDetails> // Getter
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        // Network call.
        try {
            compositeDisposable.add( // We want this thread to be disposable, so we add it to `compositeDisposable`.
                apiService.getMovieDetails(movieId) // This will return an observable, so we subscribe to it.
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            // If the call is successful, we post the movie details contained in `it`.
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            // If there is an error in the call...
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message.toString())
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("MovieDetailsDataSource", e.message.toString())
        }
    }
}
