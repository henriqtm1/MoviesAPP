
package com.example.moviesapp.ui.home

import androidx.lifecycle.*
import com.example.moviesapp.api.models.movies.MoviesBaseResponse
import com.example.moviesapp.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val aMoviesRepository: MoviesRepository) : ViewModel() {

    private val _moviesList = MutableLiveData<MoviesBaseResponse>()
    val mMovies: LiveData<MoviesBaseResponse> get() = _moviesList

    private val _errorMessage = MutableLiveData<String>()
    val mErrorMessage: LiveData<String> get() = _errorMessage

    fun vmGetMovies(aIncludeAdult: Boolean, aIncludeVideo: Boolean, aLanguage: String, aPage: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _moviesList.postValue(aMoviesRepository.repoGetMovies(aIncludeAdult, aIncludeVideo, aLanguage, aPage))
                } catch (aException: Exception) {
                    _errorMessage.postValue(aException.message)
                }
            }
        }
    }
}