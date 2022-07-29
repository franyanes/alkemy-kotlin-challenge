package com.example.moviecatalog.ui.popular_movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.TheMovieDBClient
import com.example.moviecatalog.data.api.TheMovieDBInterface
import com.example.moviecatalog.data.repository.NetworkState

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    lateinit var moviePagedListRepository: MoviesPagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        moviePagedListRepository = MoviesPagedListRepository(apiService)
        viewModel = getViewModel()
        val popularMoviesPagedListAdapter = PopularMoviesPagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = popularMoviesPagedListAdapter.getItemViewType(position)
                return if (viewType == popularMoviesPagedListAdapter.MOVIE_VIEW_TYPE) {
                    1
                } else {
                    3
                }
            }
        }
        findViewById<RecyclerView>(R.id.rv_movie_list).layoutManager = gridLayoutManager
        findViewById<RecyclerView>(R.id.rv_movie_list).setHasFixedSize(true)
        findViewById<RecyclerView>(R.id.rv_movie_list).adapter = popularMoviesPagedListAdapter
        viewModel.moviesPagedList.observe(this, Observer{
            popularMoviesPagedListAdapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            findViewById<ProgressBar>(R.id.progress_bar).visibility = if (
                viewModel.listIsEmpty() && it == NetworkState.LOADING
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }
            findViewById<TextView>(R.id.txt_connection_error).visibility = if (
                viewModel.listIsEmpty() && it == NetworkState.ERROR
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }
            if (!viewModel.listIsEmpty()) {
                popularMoviesPagedListAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(moviePagedListRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }
}
