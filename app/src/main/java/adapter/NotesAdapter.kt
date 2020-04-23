package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import clicklisteners.ItemClickListener
import mm.androidlearn.mynotes.R
import model.Notes

class NotesAdapter( var notesList: List<Notes>, var listener: ItemClickListener) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { //responsible for adding the layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { //responsible for binding data to layout
        val notes = notesList[position]
        val title = notes.title
        val desc = notes.description
        holder.textTitle.text = title
        holder.textDesc.text = desc
        holder.itemView.setOnClickListener { listener.onClick(notes) }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textTitle: TextView = itemView.findViewById(R.id.textTitle)
         var textDesc: TextView = itemView.findViewById(R.id.textDescription)

     }

}