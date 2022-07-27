package com.example.moviecatalog.ui.popular_movies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.data.api.POSTER_BASE_URL
import com.example.moviecatalog.data.repository.NetworkState
import com.example.moviecatalog.data.vo.popular_movies.PopularMovieItem
import com.example.moviecatalog.ui.movie_details.MovieDetailsActivity
import org.w3c.dom.Text

class PopularMoviesPagedListAdapter(public val context: Context) : PagedListAdapter<PopularMovieItem, RecyclerView.ViewHolder>(
    MoviesDiffCallback()
) {
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousNetworkState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        if (hadExtraRow != hasExtraRow()) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow() && previousNetworkState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MoviesItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return MoviesItemNetworkStateViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MoviesItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as MoviesItemNetworkStateViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return if (hasExtraRow()) {
            super.getItemCount() + 1
        } else {
            super.getItemCount() + 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    class MoviesDiffCallback : DiffUtil.ItemCallback<PopularMovieItem>() {
        override fun areItemsTheSame(
            oldItem: PopularMovieItem,
            newItem: PopularMovieItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PopularMovieItem,
            newItem: PopularMovieItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    class MoviesItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: PopularMovieItem?, context: Context) {
            itemView.findViewById<TextView>(R.id.title).text = movie?.title
            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.findViewById(R.id.poster))
            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class MoviesItemNetworkStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
            } else {
                itemView.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
            }
            if (
                networkState != null
                && (networkState == NetworkState.ERROR || networkState == NetworkState.EOL)
            ) {
                itemView.findViewById<TextView>(R.id.txt_error).text = networkState.msg
                itemView.findViewById<TextView>(R.id.txt_error).visibility = View.VISIBLE
            } else {
                itemView.findViewById<TextView>(R.id.txt_error).visibility = View.GONE
            }
        }
    }
}
