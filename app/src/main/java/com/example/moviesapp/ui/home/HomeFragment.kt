package com.example.moviesapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moviesapp.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class HomeFragment : Fragment() {
    private val mHomeViewModel: HomeViewModel by viewModel()
    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val lRoot = mBinding.root
        mHomeViewModel.vmGetMovies(
            aIncludeAdult = false,
            aIncludeVideo = false,
            aLanguage = Locale.getDefault().language,
            aPage = 1
        )
        return lRoot
    }

    fun fetchHomeViewModel(): HomeViewModel {
        return mHomeViewModel
    }
}
