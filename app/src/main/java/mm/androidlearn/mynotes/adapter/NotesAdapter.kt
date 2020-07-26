package mm.androidlearn.mynotes.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mm.androidlearn.mynotes.clicklisteners.ItemClickListener
import mm.androidlearn.mynotes.R
import mm.androidlearn.mynotes.db.Notes

class NotesAdapter(var notesList: ArrayList<Notes>, var listener: ItemClickListener) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    val TAG = "NotesAdapter"
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
        holder.checkBoxMarkStatus.isChecked = notes.isTaskCompleted
        Glide.with(holder.itemView).load(notes.imagepath).into(holder.imageView)

        holder.itemView.setOnClickListener (object:View.OnClickListener {
            override fun onClick(v: View?) {
                listener.onClick(notes)
            }

        })
        holder.checkBoxMarkStatus.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(button: CompoundButton?, isChecked: Boolean) {
                Log.d(TAG,isChecked.toString())
                notes.isTaskCompleted = isChecked
                listener.onUpdate(notes)
            }

        })
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var textTitle: TextView = itemView.findViewById(R.id.textTitle)
         var textDesc: TextView = itemView.findViewById(R.id.textDescription)
         var checkBoxMarkStatus : CheckBox =itemView.findViewById(R.id.selectNotes)
         var imageView: ImageView = itemView.findViewById(R.id.imagepath)

     }

}