package com.example.pod.adapter

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition:Int, toPosition:Int)
    fun onItemDismiss(position:Int)
}

interface ItemTouchHelperViewHolder {
    fun onItemSelected()
    fun onItemClear()
}