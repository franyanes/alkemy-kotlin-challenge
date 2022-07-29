package com.example.moviecatalog.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviecatalog.data.api.FIRST_PAGE
import com.example.moviecatalog.data.api.TheMovieDBInterface
import com.example.moviecatalog.data.vo.popular_movies.PopularMoviesItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesListDataSource(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
): PageKeyedDataSource<Int, PopularMoviesItem>() {
    private val page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularMoviesItem>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovies(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.popularMoviesItems, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieListDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMoviesItem>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovies(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.popularMoviesItems, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.EOL)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieListDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PopularMoviesItem>) {
        /* Do nothing. */
    }
}
