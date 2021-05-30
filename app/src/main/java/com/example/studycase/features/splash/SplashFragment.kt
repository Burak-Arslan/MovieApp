package com.example.studycase.features.splash

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.studycase.R
import com.example.studycase.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment @Inject constructor() : Fragment(R.layout.fragment_splash) {

    lateinit var binding: FragmentSplashBinding

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        binding = FragmentSplashBinding.bind(view)
        events()
    }

    private fun events() {
        binding.splashAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                try {
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                } catch (ex: Exception) {
                    ex.toString()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })
    }
}