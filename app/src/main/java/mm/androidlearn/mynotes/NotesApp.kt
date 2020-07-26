package mm.androidlearn.mynotes

import android.app.Application
import mm.androidlearn.mynotes.db.NotesDatabase

class NotesApp : Application(){
    override fun onCreate() {
        super.onCreate()
    }

    fun getNotesdb(): NotesDatabase{
        return NotesDatabase.getinstance(this)
    }

}