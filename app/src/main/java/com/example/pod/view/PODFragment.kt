package com.example.pod.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.pod.R
import com.example.pod.databinding.FragmentMainBinding
import com.example.pod.viewmodel.PODData
import com.example.pod.viewmodel.PODViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var datePOD: String = ""

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
        return binding.root
    }

    private fun setChipsGroup() {
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            setDatePOD()
            viewModel.sendServerRequest(datePOD)
        }
    }

    private fun setDatePOD() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        val date =
                when (binding.chipGroup.checkedChipId) {
                    0 -> Date(System.currentTimeMillis() - 2 * (1000 * 60 * 60 * 24))
                    1 -> Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24))
                    else -> Date(System.currentTimeMillis())
        }
        datePOD = dateFormat.format(date.time)
    }

    private fun setChips() {
        binding.chipHd.setOnCheckedChangeListener { buttonView, isChecked ->
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

        setBottomSheetBehavior(binding.bottomSheet.bottomSheetContainer)

        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.sendServerRequest(datePOD)

        binding.inputLayout.setEndIconOnClickListener {
            val i = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            }
            startActivity(i)
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderData(data: PODData) {
        when (data) {
            is PODData.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
            }
            is PODData.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is PODData.Success -> {
                binding.progressBar.visibility = View.GONE
                if (data.serverResponseData.url.isNullOrEmpty()) {
                    Toast.makeText(context, "URL is empty", Toast.LENGTH_SHORT).show()
                } else {
                    val url =
                            if (binding.chipHd.isChecked) data.serverResponseData.hdurl
                            else data.serverResponseData.url
                    binding.imageView.load(url) {
                        lifecycle(this@PODFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    binding.tvExplanation.text = data.serverResponseData.explanation
                }

//                binding.includeLayout.bottomSheetDescriptionHeader.text =data.serverResponseData.explanation // так
//                binding.includeLayout.bottomSheetDescription.text =data.serverResponseData.explanation // или так, дальше уже на ваш вкус
            }
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
            R.id.app_bar_fav -> {
                Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT).show()
            }
            R.id.app_bar_settings -> {
                requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance())
                        .addToBackStack("")
                        .commit()
            }
            android.R.id.home -> {
                BottomNavigationDrawerFragment.newInstance()
                    .show(requireActivity().supportFragmentManager, "")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}