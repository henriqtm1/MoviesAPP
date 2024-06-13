
package com.example.moviesapp.ui.home

import androidx.lifecycle.*
import com.example.moviesapp.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val MoviesRepository: MoviesRepository
) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
  //  val mAdress = MutableLiveData<AddressBaseResponse>()

    val errorMessage = MutableLiveData<String>()

    fun vmGetMovies( aIncludeAdult: Boolean,
                     aIncludeVideo: Boolean,
                     aLanguage: String,
                     aPage: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    MoviesRepository.repoGetMovies(aIncludeAdult,aIncludeVideo,aLanguage, aPage)
                } catch (e: Exception) {


                }
            }
        }
    }
}