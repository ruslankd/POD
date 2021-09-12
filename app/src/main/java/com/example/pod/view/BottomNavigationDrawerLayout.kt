package com.example.pod.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pod.R
import com.example.pod.databinding.BottomNavigationLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding: BottomNavigationLayoutBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.app_bar_fav ->{
                    Toast.makeText(context,"Favorite", Toast.LENGTH_SHORT).show()
                }
                R.id.app_bar_settings ->{
                    Toast.makeText(context,"Settings", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        fun newInstance() = BottomNavigationDrawerFragment()
    }
}