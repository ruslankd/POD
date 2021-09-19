package com.example.pod.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.pod.R
import com.example.pod.databinding.FragmentMarsBinding
import com.example.pod.viewmodel.MarsData
import com.example.pod.viewmodel.MarsViewModel
import java.util.*

class MarsFragment : Fragment() {

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var date: String = ""

    private var _binding: FragmentMarsBinding? = null
    private val binding: FragmentMarsBinding
        get() {
            return _binding!!
        }

    private val viewModel: MarsViewModel by lazy {
        ViewModelProvider(this).get(MarsViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })

        binding.dateSelect.setOnClickListener {
            datePicker()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun datePicker() {
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext(),
                { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    date = "$year-${monthOfYear + 1}-$dayOfMonth"
                    binding.dateSelect.setText(date)
                    viewModel.sendServerRequest(date)
                }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun renderData(data: MarsData) {
        when (data) {
            is MarsData.Error -> {
                Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
            }
            is MarsData.Loading -> {
                binding.marsPhoto.load(R.drawable.progress_animation) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is MarsData.Success -> {
                if (data.serverResponseData.photos.isEmpty()) {
                    Toast.makeText(context, "URL is empty", Toast.LENGTH_SHORT).show()
                } else {
                    val url = data.serverResponseData.photos[0].imgSrc
                    binding.marsPhoto.load(url) {
                        placeholder(R.drawable.progress_animation)
                        error(R.drawable.ic_load_error_vector)
                        lifecycle(this@MarsFragment)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = MarsFragment()
    }
}