package com.example.pod.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.pod.R
import com.example.pod.adapter.ItemTouchHelperViewHolder
import com.example.pod.adapter.NotesAdapter
import com.example.pod.adapter.OnListItemClickListener
import com.example.pod.adapter.data.Note
import com.example.pod.databinding.FragmentNotesBinding
import com.example.pod.view.NotesSetFragment.Companion.NOTE_BODY
import com.example.pod.view.NotesSetFragment.Companion.NOTE_POSITION
import com.example.pod.view.NotesSetFragment.Companion.NOTE_TITLE
import com.example.pod.view.NotesSetFragment.Companion.REQUEST_KEY


class NotesFragment : Fragment() {

    lateinit var itemTouchHelper: ItemTouchHelper
    lateinit var adapter: NotesAdapter

    private var _binding: FragmentNotesBinding? = null
    private val binding: FragmentNotesBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notes:MutableList<Note> = ArrayList()
        repeat(10){
            if(it%2==0){
                notes.add(Note("Должники", "Антон - 150\nИгорь - 2000\nНастя - 200\n", false))
            } else {
                notes.add(Note("Покупки", "Молоко\nТуалетная бумага\nМыло\n", false))
            }
        }

        adapter = NotesAdapter(
            object : OnListItemClickListener {
                override fun onItemClick(note: Note, position: Int) {
                    val bundle = Bundle().apply {
                        putString(NOTE_TITLE, note.title)
                        putString(NOTE_BODY, note.body)
                        putInt(NOTE_POSITION, position)
                    }

                    requireActivity().apply {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, NotesSetFragment.newInstance().apply { arguments = bundle })
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }

                override fun onImageClick(position: Int) {
                    binding.rvNotes.scrollToPosition(position)
                }
            }, notes)
        binding.rvNotes.adapter = adapter

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvNotes)

        parentFragmentManager.setFragmentResultListener(REQUEST_KEY, this, { _, bundle ->
            adapter.updateItem(
                bundle.getString(NOTE_TITLE) ?: "",
                bundle.getString(NOTE_BODY) ?: "",
                bundle.getInt(NOTE_POSITION)
            )
        })

        binding.fabAdd.setOnClickListener {
            adapter.addItem(Note("Название", "Описание", false))
            binding.rvNotes.smoothScrollToPosition(adapter.itemCount)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = NotesFragment()
    }

    class ItemTouchHelperCallback(private val adapter: NotesAdapter) :
        ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(
                dragFlags,
                swipeFlags
            )
        }

        override fun onMove(
            recyclerView: RecyclerView,
            source: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            adapter.onItemMove(source.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
            adapter.onItemDismiss(viewHolder.adapterPosition)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
                itemViewHolder.onItemSelected()
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
            itemViewHolder.onItemClear()
        }
    }
}