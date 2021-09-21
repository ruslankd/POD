package com.example.pod.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import com.example.pod.databinding.FragmentCosmosBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs


class CosmosFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
    private var _binding: FragmentCosmosBinding? = null
    private val binding: FragmentCosmosBinding
        get() {
            return _binding!!
        }

    companion object {
        fun newInstance() = CosmosFragment()
    }

    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCosmosBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainAppbar.addOnOffsetChangedListener(this@CosmosFragment)

        startAlphaAnimation(binding.mainTextviewTitle, 0, View.INVISIBLE);
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(binding.mainTextviewTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleVisible = true
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(binding.mainTextviewTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(binding.mainLinearlayoutTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                mIsTheTitleContainerVisible = false
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(binding.mainLinearlayoutTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }

    private fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }
}