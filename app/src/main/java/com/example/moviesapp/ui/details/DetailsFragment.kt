package com.example.moviesapp.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviesapp.databinding.FragmentDetailsBinding
import com.example.moviesapp.utils.DecimalFormatRating
import com.example.moviesapp.utils.GlideImage

class DetailsFragment : Fragment() {
    private lateinit var mBinding: FragmentDetailsBinding

    override fun onCreateView(
        aInflater: LayoutInflater,
        aContainer: ViewGroup?,
        aSavedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDetailsBinding.inflate(aInflater, aContainer, false)
        val lImage = arguments?.getString("image")
        val lTitle = arguments?.getString("title")
        val lRating = arguments?.getFloat("rating")
        val lDesc = arguments?.getString("desc")

       setComponentesInScreen(lImage,lTitle,lRating,lDesc)
        val lRoot = mBinding.root
        return lRoot
    }

    private fun setComponentesInScreen(lImage: String?, lTitle: String?, lRating: Float?, lDesc: String?) {
        GlideImage.GlideImageTransform(mBinding.imgMovie.context, lImage!!, mBinding.imgMovie)
        mBinding.txtTitle.text = lTitle
        mBinding.txtMovieRating.text = DecimalFormatRating.mDecimalFormat.format(lRating)
        mBinding.txtDescription.text = lDesc
        mBinding.imgBack.setOnClickListener { findNavController().popBackStack() }
    }
}
