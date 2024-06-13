package com.example.moviesapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moviesapp.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel.vmGetMovies(false, false, Locale.getDefault().language,1)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    fun fetchHomeViewModel(): HomeViewModel {
        return homeViewModel
    }
}
