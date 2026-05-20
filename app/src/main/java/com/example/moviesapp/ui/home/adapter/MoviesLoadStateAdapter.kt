package com.example.moviesapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.databinding.ItemMoviesLoadStateBinding

class MoviesLoadStateAdapter(
    private val mRetry: () -> Unit,
    private val mErrorMessage: (Throwable) -> String
) : LoadStateAdapter<MoviesLoadStateAdapter.MovieLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): MovieLoadStateViewHolder {
        val lBinding = ItemMoviesLoadStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieLoadStateViewHolder(lBinding, mRetry, mErrorMessage)
    }

    override fun onBindViewHolder(holder: MovieLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class MovieLoadStateViewHolder(
        private val aBinding: ItemMoviesLoadStateBinding,
        private val aRetry: () -> Unit,
        private val aErrorMessage: (Throwable) -> String
    ) : RecyclerView.ViewHolder(aBinding.root) {

        init {
            aBinding.txtLoadStateRetry.setOnClickListener { aRetry() }
        }

        fun bind(aLoadState: LoadState) {
            val lError = aLoadState as? LoadState.Error
            aBinding.progressLoadState.visibility = if (aLoadState is LoadState.Loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
            aBinding.txtLoadStateMessage.visibility = if (lError != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            aBinding.txtLoadStateRetry.visibility = if (lError != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            aBinding.txtLoadStateMessage.text = lError?.let { aErrorMessage(it.error) }
        }
    }
}
