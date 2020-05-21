package mm.androidlearn.mynotes

import adapter.NotesAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clicklisteners.ItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import model.Notes

class MyNotesActivity : AppCompatActivity() {

    lateinit var actionButton: FloatingActionButton
    lateinit var sharedPreferences : SharedPreferences
    val TAG = "MyNotesActivity"
    lateinit var recyclerviewNotes : RecyclerView
    var notesList = ArrayList<Notes>()
    lateinit var fullname :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)
        setUpSharedPreference()
        bindViews()
        getIntentData()

        supportActionBar?.title = fullname

        actionButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Log.d(TAG,"Clicked on Floating action button")
                setUpDialogBox()
            }
        })

    }

    private fun setUpDialogBox() {
        val view = LayoutInflater.from(this@MyNotesActivity).inflate(R.layout.add_notes_dialog,null)
        val edittextTitle : EditText = view.findViewById(R.id.editTextTitle)
        val editTextDesc : EditText = view.findViewById(R.id.editTextDescription)
        val dialog:AlertDialog = AlertDialog.Builder(this).
            setView(view).
            setCancelable(false).create()
        val submitbtn : Button = view.findViewById(R.id.submitBtn)

        submitbtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val titleText = edittextTitle.text.toString()
                val descriptionText = editTextDesc.text.toString()
                if(titleText.isNotEmpty() && descriptionText.isNotEmpty()){
                    val notes = Notes(titleText,descriptionText)
                    notesList.add(notes)

                } else {
                    Toast.makeText(this@MyNotesActivity, "Title or Description cant be empty", Toast.LENGTH_SHORT).show()
                }
                setUpRecyclerview()

                Log.d(TAG, notesList.size.toString())
                dialog.hide()
            }

        })
        dialog.show()
    }

    private fun setUpRecyclerview() {
        val itemClickListener = object : ItemClickListener{
            override fun onClick(notes: Notes?) {
               val intent = Intent(this@MyNotesActivity,DetailsActivity::class.java)
                intent.putExtra(AppConstant.TITLE,notes?.title)
                intent.putExtra(AppConstant.DESCRIPTION,notes?.description)
                startActivity(intent)
            }
        }

        val notesAdapter = NotesAdapter(notesList,itemClickListener)
        val linearLayoutManager = LinearLayoutManager(this@MyNotesActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerviewNotes.layoutManager = linearLayoutManager
        recyclerviewNotes.adapter = notesAdapter
    }

    private fun getIntentData() {
       val intent = getIntent()
        if(intent.hasExtra(AppConstant.FULLNAME)){
            fullname = intent.getStringExtra(AppConstant.FULLNAME)
        }

        if(fullname.isEmpty()){
            fullname = sharedPreferences.getString(PrefConstant.FULL_NAME,"")!!
        }

    }

    private fun bindViews() {
        actionButton = findViewById(R.id.fabAddNotes)
    }

    private fun setUpSharedPreference() {
       sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
       recyclerviewNotes = findViewById(R.id.recyclerNotes)
    }


}