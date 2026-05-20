package com.example.moviesapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.ui.home.adapter.MoviesAdapter
import com.example.moviesapp.ui.home.adapter.MoviesLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private companion object {
        private const val FIRST_PAGE = 1
    }

    private val mHomeViewModel: HomeViewModel by viewModels()
    private val mMoviesAdapter = MoviesAdapter { aMovie -> onMovieClicked(aMovie) }
    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        aInflater: LayoutInflater,
        aContainer: ViewGroup?,
        aSavedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(aInflater, aContainer, false)
        val lRoot = mBinding.root
        setupRecyclerView()
        setupPagingCollectors()
        getMoviesApi()
        setListeners()
        return lRoot
    }

    private fun getMoviesApi() {
        mHomeViewModel.vmGetMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = Locale.getDefault().language,
            aPage = FIRST_PAGE
        )
    }

    private fun setupRecyclerView() {
        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mMoviesAdapter.withLoadStateFooter(
                footer = MoviesLoadStateAdapter(
                    mRetry = { mMoviesAdapter.retry() },
                    mErrorMessage = { aThrowable ->
                        getString(aThrowable.toHomeErrorMessageRes())
                    }
                )
            )
        }
    }

    private fun setupPagingCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mHomeViewModel.mMoviesPagingData.collectLatest { aPagingData ->
                        mMoviesAdapter.submitData(aPagingData)
                    }
                }
                launch {
                    mMoviesAdapter.loadStateFlow.collectLatest { aLoadStates ->
                        renderLoadState(aLoadStates)
                    }
                }
            }
        }
    }

    private fun renderLoadState(aLoadStates: CombinedLoadStates) {
        val lRefreshState = aLoadStates.refresh
        val lIsListEmpty = lRefreshState is LoadState.NotLoading && mMoviesAdapter.itemCount == 0

        when {
            lRefreshState is LoadState.Loading -> showLoading()
            lRefreshState is LoadState.Error -> showStateMessage(
                aMessage = getString(lRefreshState.error.toHomeErrorMessageRes()),
                aCanRetry = true
            )
            lIsListEmpty -> showStateMessage(
                aMessage = getString(R.string.txt_empty_movies),
                aCanRetry = false
            )
            else -> showMovies()
        }
    }

    private fun showLoading() {
        mBinding.progressMovies.visibility = View.VISIBLE
        mBinding.recyclerView.visibility = View.GONE
        mBinding.txtStateMessage.visibility = View.GONE
        mBinding.txtRetry.visibility = View.GONE
    }

    private fun showMovies() {
        mBinding.progressMovies.visibility = View.GONE
        mBinding.recyclerView.visibility = View.VISIBLE
        mBinding.txtStateMessage.visibility = View.GONE
        mBinding.txtRetry.visibility = View.GONE
    }

    private fun showStateMessage(aMessage: String, aCanRetry: Boolean) {
        mBinding.progressMovies.visibility = View.GONE
        mBinding.recyclerView.visibility = View.GONE
        mBinding.txtStateMessage.text = aMessage
        mBinding.txtStateMessage.visibility = View.VISIBLE
        mBinding.txtRetry.visibility = if (aCanRetry) View.VISIBLE else View.GONE
    }

    private fun onMovieClicked(movie: Movie) {
        val lOverview = movie.overview.ifBlank { getString(R.string.txt_empty_description) }

        val lAction = HomeFragmentDirections.actionNavHomeToNavDetails(
            movie.posterPath,
            movie.title,
            movie.voteAverage.toFloat(),
            lOverview
        )
        findNavController().navigate(lAction)
    }

    private fun setListeners() {
        mBinding.imgBack.setOnClickListener { activity?.finish() }
        mBinding.txtRetry.setOnClickListener { mMoviesAdapter.retry() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun fetchHomeViewModel(): HomeViewModel {
        return mHomeViewModel
    }
}
