package com.example.pod.adapter

import android.R.attr
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pod.R
import com.example.pod.adapter.data.Note
import com.example.pod.databinding.ItemNoteBinding
import android.R.attr.data
import java.util.*
import kotlin.Comparator


class NotesAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var notes: MutableList<Note>
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding: ItemNoteBinding =
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return NoteViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    fun updateItem(_title: String, _body: String, position: Int) {
        notes[position].apply {
            title = _title
            body = _body
        }
        notifyItemChanged(position)
    }

    fun sort(fromPosition: Int, toPosition: Int) {
        notes.sortWith { lhs, rhs ->
            if (lhs.isFixed > rhs.isFixed) {
                -1
            } else {
                1
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    fun addItem(note: Note) {
        notes.add(notes.size, note)
        notifyItemInserted(notes.size)
    }

    fun getData() = notes

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
        fun bind(note: Note) {
            ItemNoteBinding.bind(itemView).apply {
                titleItemNotes.text = note.title
                bodyItemNotes.text = note.body
                imageFixedItemNotes.setImageResource(if (note.isFixed) R.drawable.ic_push_pin_color else R.drawable.ic_push_pin)
                imageFixedItemNotes.setOnClickListener {
                    note.isFixed = !note.isFixed
                    if (note.isFixed)
                    {
                        (it as ImageView).setImageResource(R.drawable.ic_push_pin_color)
                        sort(layoutPosition, 0)
                    } else {
                        (it as ImageView).setImageResource(R.drawable.ic_push_pin)
                        sort(layoutPosition, notes.count { it.isFixed })
                    }
                    onListItemClickListener.onImageClick(0)
                }
                titleItemNotes.setOnClickListener {
                    onListItemClickListener.onItemClick(note, layoutPosition)
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.noteBackgroundColor, itemView.context.theme))
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        notes.removeAt(fromPosition).apply {
            notes.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
    }
}