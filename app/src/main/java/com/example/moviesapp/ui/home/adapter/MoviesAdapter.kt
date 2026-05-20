package com.example.moviesapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ItemMovieBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.utils.MoviePosterLoader
import com.example.moviesapp.utils.RatingFormatter

class MoviesAdapter(
    private val itemClickListener: (Movie) -> Unit
) : PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder>(MovieDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { movie ->
            holder.bind(movie, itemClickListener)
        }
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, clickListener: (Movie) -> Unit) {
            binding.txtMovieTitle.text = movie.title
            binding.txtMovieRating.text = RatingFormatter.format(movie.voteAverage)
            binding.imgMovie.contentDescription = binding.imgMovie.context.getString(
                R.string.content_description_movie_poster_with_title,
                movie.title
            )
            MoviePosterLoader.load(movie.posterPath, binding.imgMovie)

            binding.root.setOnClickListener { clickListener(movie) }
        }
    }
}
