package com.example.moviecatalog.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviecatalog.data.api.TheMovieDBInterface
import com.example.moviecatalog.data.vo.popular_movies.PopularMoviesItem
import io.reactivex.disposables.CompositeDisposable

class MovieListDataSourceFactory(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, PopularMoviesItem>() {
    val moviesLiveDataSource = MutableLiveData<MovieListDataSource>()

    override fun create(): DataSource<Int, PopularMoviesItem> {
        val moviesDataSource = MovieListDataSource(apiService, compositeDisposable)
        moviesLiveDataSource.postValue(moviesDataSource)
        return moviesDataSource
    }
}
