package com.example.pod.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import coil.load
import com.example.pod.databinding.ActivitySceneTransitionBinding

class SceneTransitionActivity : AppCompatActivity() {

    private var _binding: ActivitySceneTransitionBinding? = null
    private val binding: ActivitySceneTransitionBinding
        get() {
            return _binding!!
        }

    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySceneTransitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getIntExtra(EXTRA_PARAM_ID, 0)
        ViewCompat.setTransitionName(binding.ivFromFab, VIEW_NAME_HEADER_IMAGE)

        binding.ivFromFab.load(ResourcesCompat.getDrawable(resources, id, theme))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        const val EXTRA_PARAM_ID = "detail:_id"
        const val VIEW_NAME_HEADER_IMAGE = "detail:header:image"
    }
}