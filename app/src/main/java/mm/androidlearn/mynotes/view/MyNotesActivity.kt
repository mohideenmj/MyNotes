package mm.androidlearn.mynotes.view

import mm.androidlearn.mynotes.adapter.NotesAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mm.androidlearn.mynotes.clicklisteners.ItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mm.androidlearn.mynotes.NotesApp
import mm.androidlearn.mynotes.utils.AppConstant
import mm.androidlearn.mynotes.utils.PrefConstant
import mm.androidlearn.mynotes.R
import mm.androidlearn.mynotes.db.Notes


class MyNotesActivity : AppCompatActivity() {

    lateinit var actionButton: FloatingActionButton
    lateinit var sharedPreferences : SharedPreferences
    val TAG = "MyNotesActivity"
    lateinit var recyclerviewNotes : RecyclerView
    val notesList = ArrayList<Notes>()
     var fullname :String =""
    val ADD_NOTES_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)
        setUpSharedPreference()
        bindViews()
        getIntentData()
        getDataFromDataBase()
        setUpToolbarText()
        clickListeners()
        setUpRecyclerview()
    }

    private fun getDataFromDataBase() {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesdb().notesdao()
        notesList.addAll(notesDao.getAll())
    }

    private fun setUpToolbarText() {
        supportActionBar?.title = fullname
    }

    private fun clickListeners() {
        actionButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                Log.d(TAG,"Clicked on Floating action button")
                val intent = Intent(this@MyNotesActivity,AddNotesActivity::class.java)
                startActivityForResult(intent,ADD_NOTES_CODE)
                //setUpDialogBox()
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
                    val notes = Notes(title = titleText,description = descriptionText)
                    notesList.add(notes)
                    addNotesTodb(notes)



                } else {
                    Toast.makeText(this@MyNotesActivity, "Title or Description cant be empty", Toast.LENGTH_SHORT).show()
                }


                Log.d(TAG, notesList.size.toString())
                dialog.hide()
            }

        })
        dialog.show()
    }

    private fun addNotesTodb(notes: Notes) {
        //insert notes in db
        val notesApp = applicationContext as NotesApp
        val notesdao = notesApp.getNotesdb().notesdao()
        notesdao.insertNotes(notes)

    }

    private fun setUpRecyclerview() {
        val itemClickListener = object : ItemClickListener{
            override fun onClick(notes: Notes?) {
               val intent = Intent(this@MyNotesActivity, DetailsActivity::class.java)
                intent.putExtra(AppConstant.TITLE,notes?.title)
                intent.putExtra(AppConstant.DESCRIPTION,notes?.description)
                startActivity(intent)
            }

            override fun onUpdate(notes: Notes) {
                 Log.d(TAG,notes.isTaskCompleted.toString())
                val notesapp = applicationContext as NotesApp
                val notesDao = notesapp.getNotesdb().notesdao()
                notesDao.updateNotes(notes)

            }
        }

        val notesAdapter = NotesAdapter(notesList,itemClickListener)
        val linearLayoutManager = LinearLayoutManager(this@MyNotesActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerviewNotes.layoutManager = linearLayoutManager
        recyclerviewNotes.adapter = notesAdapter
    }

    private fun getIntentData() {
       val intent = intent
        if(intent.hasExtra(AppConstant.FULLNAME)){
            fullname = intent.getStringExtra(AppConstant.FULLNAME)
        }

        if(fullname.isEmpty()){
            fullname = sharedPreferences.getString(PrefConstant.FULL_NAME,"")!!
        }

    }

    private fun bindViews() {
        actionButton = findViewById(R.id.fabAddNotes)
        recyclerviewNotes = findViewById(R.id.recyclerNotes)
    }

    private fun setUpSharedPreference() {
       sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){

            ADD_NOTES_CODE ->{
                val title = data?.getStringExtra(AppConstant.TITLE)
                val description = data?.getStringExtra(AppConstant.DESCRIPTION)
                val imagepath = data?.getStringExtra(AppConstant.IMAGE_PATH)

                val notes = Notes(title = title!!,description = description!!,imagepath =  imagepath!!)
                addNotesTodb(notes)
                notesList.add(notes)
                recyclerviewNotes.adapter?.notifyItemChanged(notesList.size-1)
            }
        }

    }

}