package com.example.moviesapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ItemMovieBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.utils.DecimalFormatRating
import com.example.moviesapp.utils.GlideImage

class MoviesAdapter(
    private val mItemClickListener: (Movie) -> Unit
) : PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder>(MovieDiffCallback) {

    override fun onCreateViewHolder(aParent: ViewGroup, aViewType: Int): MovieViewHolder {
        val lBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(aParent.context), aParent, false)
        return MovieViewHolder(lBinding)
    }

    override fun onBindViewHolder(aHolder: MovieViewHolder, aPosition: Int) {
        getItem(aPosition)?.let { aMovie ->
            aHolder.bind(aMovie, mItemClickListener)
        }
    }

    class MovieViewHolder(private val aBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(aBinding.root) {

        fun bind(aMovie: Movie, clickListener: (Movie) -> Unit) {
            aBinding.txtMovieTitle.text = aMovie.title
            aBinding.txtMovieRating.text =
                DecimalFormatRating.mDecimalFormat.format(aMovie.voteAverage)
            aBinding.imgMovie.contentDescription = aBinding.imgMovie.context.getString(
                R.string.content_description_movie_poster_with_title,
                aMovie.title
            )
            GlideImage.GlideImageTransform(
                aBinding.imgMovie.context,
                aMovie.posterPath,
                aBinding.imgMovie
            )

            aBinding.root.setOnClickListener { clickListener(aMovie) }
        }
    }
}
