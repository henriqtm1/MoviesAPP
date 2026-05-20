package com.example.moviesapp.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentDetailsBinding
import com.example.moviesapp.utils.DecimalFormatRating
import com.example.moviesapp.utils.GlideImage

class DetailsFragment : Fragment() {
    private val mArgs: DetailsFragmentArgs by navArgs()
    private var _binding: FragmentDetailsBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        aInflater: LayoutInflater,
        aContainer: ViewGroup?,
        aSavedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(aInflater, aContainer, false)
        setComponentesInScreen(mArgs.image, mArgs.title, mArgs.rating, mArgs.desc)
        val lRoot = mBinding.root
        return lRoot
    }

    private fun setComponentesInScreen(
        lImage: String,
        lTitle: String,
        lRating: Float,
        lDesc: String
    ) {
        mBinding.imgMovie.contentDescription = getString(
            R.string.content_description_movie_poster_with_title,
            lTitle
        )
        GlideImage.GlideImageTransform(mBinding.imgMovie.context, lImage, mBinding.imgMovie)
        mBinding.txtTitle.text = lTitle
        mBinding.txtMovieRating.text = DecimalFormatRating.mDecimalFormat.format(lRating)
        mBinding.txtDescription.text = lDesc
        mBinding.imgBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
