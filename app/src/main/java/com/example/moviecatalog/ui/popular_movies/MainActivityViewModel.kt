package com.example.moviecatalog.ui.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviecatalog.data.repository.NetworkState
import com.example.moviecatalog.data.vo.popular_movies.PopularMoviesItem
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val movieRepository: MoviesPagedListRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val moviesPagedList: LiveData<PagedList<PopularMoviesItem>> by lazy {
        movieRepository.fetchMoviePagedList(compositeDisposable)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviesPagedList.value?.isEmpty() ?: true // Elvis operator
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
