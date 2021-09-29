package com.example.pod.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pod.databinding.FragmentNotesSetBinding

class NotesSetFragment : Fragment() {

    private var _binding: FragmentNotesSetBinding? = null
    private val binding: FragmentNotesSetBinding
        get() {
            return _binding!!
        }

    private var position: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesSetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        setupSaveButton()
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val bundle = Bundle().apply {
                putString(NOTE_TITLE, binding.etNoteTitle.text.toString())
                putString(NOTE_BODY, binding.etNoteBody.text.toString())
                putInt(NOTE_POSITION, position)
            }
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundle)
            requireActivity().onBackPressed()
        }
    }

    private fun setData() {
        binding.apply {
            etNoteTitle.setText(arguments?.getString(NOTE_TITLE))
            etNoteBody.setText(arguments?.getString(NOTE_BODY))
            position = arguments?.getInt(NOTE_POSITION) ?: -1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = NotesSetFragment()

        const val NOTE_TITLE = "note title"
        const val NOTE_BODY = "note body"
        const val NOTE_POSITION = "note position"
        const val REQUEST_KEY = "request key"
    }
}