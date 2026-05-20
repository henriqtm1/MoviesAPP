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
import com.example.moviesapp.utils.MoviePosterLoader
import com.example.moviesapp.utils.RatingFormatter

class DetailsFragment : Fragment() {
    private val args: DetailsFragmentArgs by navArgs()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        bindMovieDetails(args.image, args.title, args.rating, args.desc)
        return binding.root
    }

    private fun bindMovieDetails(
        image: String,
        title: String,
        rating: Float,
        description: String
    ) {
        binding.imgMovie.contentDescription = getString(
            R.string.content_description_movie_poster_with_title,
            title
        )
        MoviePosterLoader.load(image, binding.imgMovie)
        binding.txtTitle.text = title
        binding.txtMovieRating.text = RatingFormatter.format(rating)
        binding.txtDescription.text = description
        binding.imgBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
