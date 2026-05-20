package com.example.moviesapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.model.Movie

private const val FIRST_PAGE = 1

class MoviesPagingSource(
    private val aMoviesRepository: MoviesRepository,
    private val aIncludeAdult: Boolean,
    private val aIncludeVideo: Boolean,
    private val aLanguage: String,
    private val aInitialPage: Int = FIRST_PAGE
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val lPage = params.key ?: aInitialPage

        return when (
            val lResult = aMoviesRepository.getMovies(
                aIncludeAdult = aIncludeAdult,
                aIncludeVideo = aIncludeVideo,
                aLanguage = aLanguage,
                aPage = lPage
            )
        ) {
            is ApiResult.Success -> {
                val lMoviesPage = lResult.data
                LoadResult.Page(
                    data = lMoviesPage.movies,
                    prevKey = if (lMoviesPage.page <= aInitialPage) null else lMoviesPage.page - 1,
                    nextKey = if (
                        lMoviesPage.movies.isEmpty() ||
                        lMoviesPage.page >= lMoviesPage.totalPages
                    ) {
                        null
                    } else {
                        lMoviesPage.page + 1
                    }
                )
            }
            is ApiResult.Error -> LoadResult.Error(
                MoviesPagingException(
                    type = lResult.type,
                    message = lResult.message,
                    cause = lResult.cause
                )
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { aAnchorPosition ->
            state.closestPageToPosition(aAnchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(aAnchorPosition)?.nextKey?.minus(1)
        }
    }
}
