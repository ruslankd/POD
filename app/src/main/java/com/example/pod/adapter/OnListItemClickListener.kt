package com.example.pod.adapter

import com.example.pod.adapter.data.Note

interface OnListItemClickListener {
    fun onItemClick(note: Note, position: Int)
    fun onImageClick(position: Int)
}