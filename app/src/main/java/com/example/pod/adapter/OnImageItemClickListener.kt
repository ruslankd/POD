package com.example.pod.adapter

import com.example.pod.adapter.data.Note

interface OnImageItemClickListener {
    fun onImageClick(note: Note, position: Int)
}