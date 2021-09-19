package com.example.pod.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pod.R
import com.example.pod.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chipGroupForTheme.setOnCheckedChangeListener { _, checkedId ->
            var themeId: Int = R.style.Theme_POD
            when(checkedId) {
                R.id.chipTheme1 -> themeId = R.style.Theme_POD
                R.id.chipTheme2 -> themeId = R.style.Theme2
                R.id.chipTheme3 -> themeId = R.style.Theme3
            }
            requireActivity()
                    .getSharedPreferences("POD_SP", Context.MODE_PRIVATE)
                    .edit()
                    .putInt(SP_THEME, themeId)
                    .apply()
            Handler().post {
                val intent = requireActivity().intent
                requireActivity().overridePendingTransition(0, 0)
                requireActivity().finish()
                requireActivity().overridePendingTransition(0, 0)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()

        const val SP_THEME = "SP theme"
    }

}