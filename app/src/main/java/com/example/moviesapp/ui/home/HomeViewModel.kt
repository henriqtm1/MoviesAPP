package com.example.moviesapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.MoviesPagingSource
import com.example.moviesapp.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

private const val MOVIES_PAGE_SIZE = 20
private const val PREFETCH_DISTANCE = 4

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val movieQuery = MutableStateFlow<MovieQuery?>(null)

    val moviesPagingData: Flow<PagingData<Movie>> = movieQuery
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { query ->
            Pager(
                config = PagingConfig(
                    pageSize = MOVIES_PAGE_SIZE,
                    prefetchDistance = PREFETCH_DISTANCE,
                    initialLoadSize = MOVIES_PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    MoviesPagingSource(
                        moviesRepository = moviesRepository,
                        includeAdult = query.includeAdult,
                        includeVideo = query.includeVideo,
                        language = query.language,
                        initialPage = query.initialPage
                    )
                }
            ).flow
        }
        .cachedIn(viewModelScope)

    fun loadMovies(includeAdult: Boolean, includeVideo: Boolean, language: String, page: Int) {
        movieQuery.value = MovieQuery(
            includeAdult = includeAdult,
            includeVideo = includeVideo,
            language = language,
            initialPage = page
        )
    }
}

private data class MovieQuery(
    val includeAdult: Boolean,
    val includeVideo: Boolean,
    val language: String,
    val initialPage: Int
)
