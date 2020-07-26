package mm.androidlearn.mynotes.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface NotesDao {

    @Query("Select * from notesData")
    fun getAll() : List<Notes>

    @Insert(onConflict = REPLACE)
    fun insertNotes(notes: Notes)

    @Update
    fun updateNotes(notes: Notes)

    @Delete
    fun deleteNotes(notes: Notes)
}