package com.example.pod.view

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.*
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.load
import com.example.pod.R
import com.example.pod.databinding.FragmentMainBinding
import com.example.pod.viewmodel.PODData
import com.example.pod.viewmodel.PODViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener
import java.text.SimpleDateFormat
import java.util.*

class PODFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() {
            return _binding!!
        }

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }

    private var datePOD: String = ""
    private var isShow = true
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        setActionBar()
        setScrolling()
        setChips()
        setChipsGroup()
        setDatePOD()
        setImageAnimation()

        val builder = GuideView.Builder(context)
            .setContentText("С помощью этой кнопки вы можете скрыть панель поиска Википедии")
            .setGravity(Gravity.center)
            .setDismissType(DismissType.selfView)
            .setTargetView(binding.includedLayout.ivWiki)
            .setDismissType(DismissType.anywhere)
            .setGuideListener { }
        builder.build().show()

        return binding.root
    }

    private fun setImageAnimation() {
        binding.includedLayout.imageView.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                binding.includedLayout.main, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = binding.includedLayout.imageView.layoutParams
            params.height =
                if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            binding.includedLayout.imageView.layoutParams = params
            binding.includedLayout.imageView.scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.CENTER_INSIDE
        }
    }

    private fun setChipsGroup() {
        binding.includedLayout.chipGroup.setOnCheckedChangeListener { _, _ ->
            setDatePOD()
            viewModel.sendServerRequest(datePOD)
        }
    }

    private fun setDatePOD() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        val date =
            when (binding.includedLayout.chipGroup.checkedChipId) {
                0 -> Date(System.currentTimeMillis() - 2 * (1000 * 60 * 60 * 24))
                1 -> Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24))
                else -> Date(System.currentTimeMillis())
            }
        datePOD = dateFormat.format(date.time)
    }

    private fun setChips() {
        binding.includedLayout.chipHd.setOnCheckedChangeListener { _, _ ->
            viewModel.sendServerRequest(datePOD)
        }
    }

    private fun setScrolling() {
        binding.scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == 0) {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            } else {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            }
        }
    }

    private var isMain = true
    private fun setActionBar() {
        (context as MainActivity).setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null // лучше придумать замену бургеру
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.sendServerRequest(datePOD)

        binding.includedLayout.inputLayout.setEndIconOnClickListener {
            val i = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.includedLayout.inputEditText.text.toString()}")
            }
            startActivity(i)
        }

        binding.includedLayout.ivWiki.setOnClickListener { if (isShow) hideComponents() else showComponents() }

        binding.includedLayout.tvExplanation.typeface =
            Typeface.createFromAsset(requireActivity().assets, "RobotReaversItalic.ttf")


    }

    private fun showComponents() {
        isShow = true

        val constraintSet =
            ConstraintSet().apply { clone(context, R.layout.included_constraint_start) }

        val transition = ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator(2.0f)
            duration = 1200
        }

        TransitionManager.beginDelayedTransition(binding.includedLayout.main, transition)
        constraintSet.applyTo(binding.includedLayout.main)
    }

    private fun hideComponents() {
        isShow = false

        val constraintSet =
            ConstraintSet().apply { clone(context, R.layout.included_constraint_end) }

        val transition = ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator(2.0f)
            duration = 1200
        }

        TransitionManager.beginDelayedTransition(binding.includedLayout.main, transition)
        constraintSet.applyTo(binding.includedLayout.main)
    }

    private fun renderData(data: PODData) {
        when (data) {
            is PODData.Error -> {
                binding.includedLayout.progressBar.visibility = View.GONE
                Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
            }
            is PODData.Loading -> {
                binding.includedLayout.imageView.load(R.drawable.progress_animation) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is PODData.Success -> {
                binding.includedLayout.progressBar.visibility = View.GONE
                if (data.serverResponseData.url.isNullOrEmpty()) {
                    Toast.makeText(context, "URL is empty", Toast.LENGTH_SHORT).show()
                } else {
                    val url =
                        if (binding.includedLayout.chipHd.isChecked) data.serverResponseData.hdurl
                        else data.serverResponseData.url
                    binding.includedLayout.imageView.load(url) {
                        placeholder(R.drawable.progress_animation)
                        error(R.drawable.ic_load_error_vector)
                        lifecycle(this@PODFragment)
                    }
                    data.serverResponseData.explanation?.let {
                        binding.includedLayout.tvExplanation.setText(
                            data.serverResponseData.explanation,
                            TextView.BufferType.EDITABLE
                        )
                        explanationTextDecor()
                    }
                }
            }
        }
    }

    private fun explanationTextDecor() {
        val spannable = binding.includedLayout.tvExplanation.text as SpannableStringBuilder

        for (i in spannable.indices) {
            if (spannable[i] == 'o') {
                spannable.setSpan(
                    ImageSpan(requireContext(), R.drawable.ic_planet_earth),
                    i,
                    i + 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }

        for (i in spannable.length / 3 downTo 1) {
            val n = (0 until (spannable.length - 1)).random()
            spannable.setSpan(
                ForegroundColorSpan(Color.GREEN),
                n,
                n + 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = PODFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_fav -> {
                startActivity(Intent(context, ApiActivity::class.java))
            }
            R.id.action_settings -> {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
            R.id.action_notes -> {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, NotesFragment.newInstance())
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
            android.R.id.home -> {
                BottomNavigationDrawerFragment.newInstance()
                    .show(requireActivity().supportFragmentManager, "")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}