package com.example.moviesapp.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.moviesapp.model.Movie

object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(aOldItem: Movie, aNewItem: Movie): Boolean {
        return aOldItem.id == aNewItem.id
    }

    override fun areContentsTheSame(aOldItem: Movie, aNewItem: Movie): Boolean {
        return aOldItem == aNewItem
    }
}
