package com.example.moviesapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.databinding.ItemMoviesLoadStateBinding

class MoviesLoadStateAdapter(
    private val retry: () -> Unit,
    private val errorMessage: (Throwable) -> String
) : LoadStateAdapter<MoviesLoadStateAdapter.MovieLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): MovieLoadStateViewHolder {
        val binding = ItemMoviesLoadStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieLoadStateViewHolder(binding, retry, errorMessage)
    }

    override fun onBindViewHolder(holder: MovieLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class MovieLoadStateViewHolder(
        private val binding: ItemMoviesLoadStateBinding,
        private val retry: () -> Unit,
        private val errorMessage: (Throwable) -> String
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.txtLoadStateRetry.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            val error = loadState as? LoadState.Error
            binding.progressLoadState.visibility = if (loadState is LoadState.Loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.txtLoadStateMessage.visibility = if (error != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.txtLoadStateRetry.visibility = if (error != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.txtLoadStateMessage.text = error?.let { errorMessage(it.error) }
        }
    }
}
