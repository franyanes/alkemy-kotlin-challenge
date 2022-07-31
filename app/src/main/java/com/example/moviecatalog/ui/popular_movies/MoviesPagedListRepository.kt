package com.example.moviecatalog.ui.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviecatalog.data.api.POST_PER_PAGE
import com.example.moviecatalog.data.api.TheMovieDBInterface
import com.example.moviecatalog.data.repository.MovieListDataSource
import com.example.moviecatalog.data.repository.MovieListDataSourceFactory
import com.example.moviecatalog.data.repository.NetworkState
import com.example.moviecatalog.data.dto.popular_movies.PopularMoviesItem
import io.reactivex.disposables.CompositeDisposable

class MoviesPagedListRepository(private val apiService: TheMovieDBInterface) {
    lateinit var moviePagedList: LiveData<PagedList<PopularMoviesItem>>
    lateinit var moviesDataSourceFactory: MovieListDataSourceFactory

    fun fetchMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<PopularMoviesItem>> {
        moviesDataSourceFactory = MovieListDataSourceFactory(apiService, compositeDisposable)
        val config: PagedList.Config = PagedList.Config.Builder() // This is for configuring the page list
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()
        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap(
            moviesDataSourceFactory.movieListLiveDataSource, MovieListDataSource::networkState
        )
    }
}
