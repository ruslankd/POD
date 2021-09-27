package com.example.pod.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.transition.*
import com.example.pod.R
import com.example.pod.databinding.FragmentEarthBinding
import androidx.core.app.ActivityCompat

import androidx.core.app.ActivityOptionsCompat

import android.content.Intent
import androidx.core.util.Pair
import androidx.fragment.app.FragmentActivity


class EarthFragment : Fragment() {

    private var _binding: FragmentEarthBinding? = null
    private val binding: FragmentEarthBinding
        get() {
            return _binding!!
        }

    private var isExpand = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEarthBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFabMenu()
        setFabMenuListener()
    }

    private fun setFabMenuListener() {
        binding.fabMenu.cardSun.setOnClickListener {
            setupAndRunTransition(R.drawable.sun, binding.fabMenu.ivFabSun)
        }
        binding.fabMenu.cardEarth.setOnClickListener {
            setupAndRunTransition(R.drawable.earth, binding.fabMenu.ivFabEarth)
        }
        binding.fabMenu.cardMars.setOnClickListener {
            setupAndRunTransition(R.drawable.mars, binding.fabMenu.ivFabMars)
        }
    }

    private fun setupAndRunTransition(drawable: Int, view: View) {
        val intent = Intent(context, SceneTransitionActivity::class.java)
        intent.putExtra(SceneTransitionActivity.EXTRA_PARAM_ID, drawable)

        val imageViewPair = Pair(view, SceneTransitionActivity.VIEW_NAME_HEADER_IMAGE)
        val activityOptions: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity as FragmentActivity,
                imageViewPair
            )
        context?.let { it1 -> ActivityCompat.startActivity(it1, intent, activityOptions.toBundle()) }
    }

    private fun setFabMenu() {
        binding.fabMenu.fabMain.setOnClickListener {
            val changeBounds = ChangeBounds()
            changeBounds.setPathMotion(ArcMotion())
            changeBounds.duration = 500
            TransitionManager.beginDelayedTransition(
                binding.fabMenu.fabConstraint,
                TransitionSet().addTransition(changeBounds).addTransition(Fade())
            )

            isExpand = !isExpand
            val radius = resources.getDimension(R.dimen.fab_radius)
            if (isExpand) {
                setCircle(binding.fabMenu.cardSun, radius)
                setCircle(binding.fabMenu.cardMars, radius)
                setCircle(binding.fabMenu.cardEarth, radius)
            } else {
                setCircle(binding.fabMenu.cardSun, 0f)
                setCircle(binding.fabMenu.cardMars, 0f)
                setCircle(binding.fabMenu.cardEarth, 0f)
            }
        }
    }

    private fun setCircle(view: View, circle: Float) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.circleRadius = circle.toInt()
        view.layoutParams = params
        view.visibility = if (isExpand) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun newInstance() = EarthFragment()
    }
}