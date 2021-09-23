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


class CosmosFragment : Fragment() {
    private var _binding: FragmentCosmosBinding? = null
    private val binding: FragmentCosmosBinding
        get() {
            return _binding!!
        }

    companion object {
        fun newInstance() = CosmosFragment()
    }

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
    }
}