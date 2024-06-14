package com.example.moviesapp.ui.home

import MoviesAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.R
import com.example.moviesapp.api.models.movies.Result
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.utils.ToastMessage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class HomeFragment : Fragment() {
    private val mHomeViewModel: HomeViewModel by viewModel()
    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreateView(
        aInflater: LayoutInflater,
        aContainer: ViewGroup?,
        aSavedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(aInflater, aContainer, false)
        val lRoot = mBinding.root
        setupRecyclerViewWithObserver()
        getMoviesApi()
        setErrorApi()
        setListeners()
        return lRoot
    }

    private fun getMoviesApi() {
        mHomeViewModel.vmGetMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = Locale.getDefault().language,
            aPage = 2
        )
    }

    private fun setupRecyclerViewWithObserver() {
        mHomeViewModel.mMovies.observe(viewLifecycleOwner) { aMovies ->
            val lAdapter = MoviesAdapter(aMovies.results) { aMovie ->
                onMovieClicked(aMovie)
            }
            mBinding.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = lAdapter
            }
        }
    }

    private fun setErrorApi() {
        mHomeViewModel.mErrorMessage.observe(viewLifecycleOwner) { error ->
            ToastMessage.showToastMessage_SHORT(
                requireContext(),
                error
            )
        }
    }

    private fun onMovieClicked(movie: Result) {
        val lOverview = movie.overview.ifBlank { getString(R.string.txt_empty_description) }

        val lAction = HomeFragmentDirections.actionNavHomeToNavDetails(
            movie.poster_path,
            movie.title,
            movie.vote_average.toFloat(),
            lOverview
        )
        findNavController().navigate(lAction)
    }

    private fun setListeners() {
        mBinding.imgBack.setOnClickListener { activity?.finish() }
    }

    fun fetchHomeViewModel(): HomeViewModel {
        return mHomeViewModel
    }
}