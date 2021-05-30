package com.example.studycase.features.home_detail


import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.studycase.R
import com.example.studycase.databinding.FragmentHomeDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeDetailFragment @Inject constructor() : Fragment(R.layout.fragment_home_detail) {

    lateinit var binding: FragmentHomeDetailBinding

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        binding = FragmentHomeDetailBinding.bind(view)
        var title = arguments?.get("title")
        binding.txtHomeDetailTitle.text = title.toString()
    }
}