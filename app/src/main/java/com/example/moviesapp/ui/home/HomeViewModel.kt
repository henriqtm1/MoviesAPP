
package com.example.moviesapp.ui.home

import androidx.lifecycle.*
import com.example.moviesapp.api.models.movies.MoviesBaseResponse
import com.example.moviesapp.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val aMoviesRepository: MoviesRepository
) : ViewModel() {

    val mMoviesList = MutableLiveData<MoviesBaseResponse>()
    val mErrorMessage = MutableLiveData<String>()

    fun vmGetMovies( aIncludeAdult: Boolean,
                     aIncludeVideo: Boolean,
                     aLanguage: String,
                     aPage: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    mMoviesList.postValue(aMoviesRepository.repoGetMovies(aIncludeAdult,aIncludeVideo,aLanguage, aPage))
                } catch (e: Exception) {
                    mErrorMessage.postValue(e.message)
                }
            }
        }
    }
}