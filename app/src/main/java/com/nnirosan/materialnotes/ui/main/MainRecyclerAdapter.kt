package com.nnirosan.materialnotes.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nnirosan.materialnotes.R
import com.nnirosan.materialnotes.data.note.Note


class MainRecyclerAdapter(
    val context: Context,
    val notes: MutableList<Note>,
    var itemListener: NoteClickListener
) :
    RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    private var removedPosition: Int = 0
    private lateinit var selectedItem: Note


    fun removeItem(holder: RecyclerView.ViewHolder) {
        removedPosition = holder.adapterPosition
        selectedItem = selectItem(holder)

        notes.removeAt(holder.adapterPosition)
        notifyItemRemoved(holder.adapterPosition)

        Snackbar.make(holder.itemView, "${selectedItem.title} deleted", Snackbar.LENGTH_LONG)
            .setAction("UNDO")
            {
                notes.add(removedPosition, selectedItem)
                notifyItemInserted(removedPosition)
            }
            .show()
    }

    fun selectItem(holder: RecyclerView.ViewHolder): Note {
        return notes[holder.adapterPosition]
    }

    override fun getItemCount() = notes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleText = itemView.findViewById<TextView>(R.id.titleText)
        val bodyText = itemView.findViewById<TextView>(R.id.bodyText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        with(holder) {
            titleText?.text = note.title
            bodyText?.text = note.body
        }
        holder.itemView.setOnClickListener {
            itemListener.onNoteSelected(note)
        }
    }

    interface NoteClickListener {
        fun onNoteSelected(note: Note)
    }
}