package mm.androidlearn.mynotes.clicklisteners

import mm.androidlearn.mynotes.db.Notes


interface ItemClickListener {
    fun onClick(notes: Notes?)
    fun onUpdate(notes:Notes)
}