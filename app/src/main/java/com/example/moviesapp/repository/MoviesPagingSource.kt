package com.example.moviesapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.model.Movie

private const val FIRST_PAGE = 1

class MoviesPagingSource(
    private val moviesRepository: MoviesRepository,
    private val includeAdult: Boolean,
    private val includeVideo: Boolean,
    private val language: String,
    private val initialPage: Int = FIRST_PAGE
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: initialPage

        return when (
            val result = moviesRepository.getMovies(
                includeAdult = includeAdult,
                includeVideo = includeVideo,
                language = language,
                page = page
            )
        ) {
            is ApiResult.Success -> {
                val moviesPage = result.data
                LoadResult.Page(
                    data = moviesPage.movies,
                    prevKey = if (moviesPage.page <= initialPage) null else moviesPage.page - 1,
                    nextKey = if (
                        moviesPage.movies.isEmpty() ||
                        moviesPage.page >= moviesPage.totalPages
                    ) {
                        null
                    } else {
                        moviesPage.page + 1
                    }
                )
            }
            is ApiResult.Error -> LoadResult.Error(
                MoviesPagingException(
                    type = result.type,
                    message = result.message,
                    cause = result.cause
                )
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
