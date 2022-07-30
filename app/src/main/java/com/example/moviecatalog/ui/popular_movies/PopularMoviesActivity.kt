package com.example.moviecatalog.ui.popular_movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviecatalog.data.api.TheMovieDBClient
import com.example.moviecatalog.data.api.TheMovieDBInterface
import com.example.moviecatalog.data.repository.NetworkState
import com.example.moviecatalog.databinding.ActivityPopularMoviesBinding

class PopularMoviesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPopularMoviesBinding
    private lateinit var viewModel: PopularMoviesViewModel
    lateinit var moviePagedListRepository: MoviesPagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        moviePagedListRepository = MoviesPagedListRepository(apiService)
        viewModel = getViewModel()
        val moviesPagedListAdapter = MoviesPagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = moviesPagedListAdapter.getItemViewType(position)
                return if (viewType == moviesPagedListAdapter.MOVIE_VIEW_TYPE) {
                    1
                } else {
                    3
                }
            }
        }
        binding.rvMainMovieList.layoutManager = gridLayoutManager
        binding.rvMainMovieList.setHasFixedSize(true)
        binding.rvMainMovieList.adapter = moviesPagedListAdapter
        viewModel.moviesPagedList.observe(this, Observer{
            moviesPagedListAdapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            binding.pbMain.visibility = if (
                viewModel.listIsEmpty() && it == NetworkState.LOADING
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.txtMainConnectionError.visibility = if (
                viewModel.listIsEmpty() && it == NetworkState.ERROR
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }
            if (!viewModel.listIsEmpty()) {
                moviesPagedListAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): PopularMoviesViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PopularMoviesViewModel(moviePagedListRepository) as T
            }
        })[PopularMoviesViewModel::class.java]
    }
}
