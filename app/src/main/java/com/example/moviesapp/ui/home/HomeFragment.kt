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

    private val homeViewModel: HomeViewModel by viewModels()
    private val moviesAdapter = MoviesAdapter { movie -> onMovieClicked(movie) }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupPagingCollectors()
        loadMovies()
        setListeners()
        return binding.root
    }

    private fun loadMovies() {
        homeViewModel.loadMovies(
            includeAdult = false,
            includeVideo = false,
            language = Locale.getDefault().language,
            page = FIRST_PAGE
        )
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moviesAdapter.withLoadStateFooter(
                footer = MoviesLoadStateAdapter(
                    retry = { moviesAdapter.retry() },
                    errorMessage = { throwable ->
                        getString(throwable.toHomeErrorMessageRes())
                    }
                )
            )
        }
    }

    private fun setupPagingCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.moviesPagingData.collectLatest { pagingData ->
                        moviesAdapter.submitData(pagingData)
                    }
                }
                launch {
                    moviesAdapter.loadStateFlow.collectLatest { loadStates ->
                        renderLoadState(loadStates)
                    }
                }
            }
        }
    }

    private fun renderLoadState(loadStates: CombinedLoadStates) {
        val refreshState = loadStates.refresh
        val isListEmpty = refreshState is LoadState.NotLoading && moviesAdapter.itemCount == 0

        when {
            refreshState is LoadState.Loading -> showLoading()
            refreshState is LoadState.Error -> showStateMessage(
                message = getString(refreshState.error.toHomeErrorMessageRes()),
                canRetry = true
            )
            isListEmpty -> showStateMessage(
                message = getString(R.string.txt_empty_movies),
                canRetry = false
            )
            else -> showMovies()
        }
    }

    private fun showLoading() {
        binding.progressMovies.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.txtStateMessage.visibility = View.GONE
        binding.txtRetry.visibility = View.GONE
    }

    private fun showMovies() {
        binding.progressMovies.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.txtStateMessage.visibility = View.GONE
        binding.txtRetry.visibility = View.GONE
    }

    private fun showStateMessage(message: String, canRetry: Boolean) {
        binding.progressMovies.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.txtStateMessage.text = message
        binding.txtStateMessage.visibility = View.VISIBLE
        binding.txtRetry.visibility = if (canRetry) View.VISIBLE else View.GONE
    }

    private fun onMovieClicked(movie: Movie) {
        val overview = movie.overview.ifBlank { getString(R.string.txt_empty_description) }

        val action = HomeFragmentDirections.actionNavHomeToNavDetails(
            movie.posterPath,
            movie.title,
            movie.voteAverage.toFloat(),
            overview
        )
        findNavController().navigate(action)
    }

    private fun setListeners() {
        binding.imgBack.setOnClickListener { activity?.finish() }
        binding.txtRetry.setOnClickListener { moviesAdapter.retry() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
