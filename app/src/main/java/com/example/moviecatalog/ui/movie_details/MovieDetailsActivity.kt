package com.example.moviecatalog.ui.movie_details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.POSTER_BASE_URL
import com.example.moviecatalog.data.api.TheMovieDBClient
import com.example.moviecatalog.data.api.TheMovieDBInterface
import com.example.moviecatalog.data.repository.NetworkState
import com.example.moviecatalog.data.vo.movie_details.MovieDetails
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val movieId: Int = intent.getIntExtra("id", 0)
        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieDetailsRepository = MovieDetailsRepository(apiService)
        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            /* We can observe movieDetails because it's a LiveData.
            * So if any changes happen, we can update the UI. */
            bindUI(it) // `it` is `movieDetails` returned data.
        })
        viewModel.networkState.observe(this, Observer {
            findViewById<ProgressBar>(R.id.pb_details).visibility =
                if (it == NetworkState.LOADING) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            findViewById<TextView>(R.id.tv_details_connection_error).visibility =
                if (it == NetworkState.ERROR) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        })
    }

    private fun bindUI(it: MovieDetails) {
        val moviePosterURL = POSTER_BASE_URL + it.posterPath // We load the movie poster.
        Glide.with(this)
            .load(moviePosterURL)
            .into(findViewById(R.id.iv_details_poster))
        findViewById<TextView>(R.id.tv_details_title).text = it.title
        findViewById<TextView>(R.id.tv_details_overview).text = it.overview
        findViewById<TextView>(R.id.tv_details_release_date).text = getFormattedDateFromResponse(it)
        findViewById<TextView>(R.id.tv_details_genre).text = getConcatenatedGenresFromResponse(it)
        findViewById<TextView>(R.id.tv_details_language).text = it.spokenLanguages.first().englishName
        findViewById<TextView>(R.id.tv_details_rating).text = it.voteAverage.toString().take(3)
    }

    /* This is ViewModel Provider Factory for MovieDetailsViewModel. */
    private fun getViewModel(movieId: Int): MovieDetailsViewModel {
        return ViewModelProvider(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailsViewModel(movieDetailsRepository, movieId) as T
            }
        })[MovieDetailsViewModel::class.java]
    }

    private fun getConcatenatedGenresFromResponse(it: MovieDetails): String {
        var genreList = mutableListOf<String>()
        for (genre in it.genres) {
            genreList.add(genre.name)
        }
        return genreList.joinToString(", ")
    }

    @SuppressLint("NewApi")
    private fun getFormattedDateFromResponse(it: MovieDetails): String {
        val date = LocalDate.parse(it.releaseDate)
        return date.month.toString().lowercase().replaceFirstChar{ it.uppercase() } + " " +
                date.dayOfMonth.toString() + ", " +
                date.year.toString()
    }
}
