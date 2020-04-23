package clicklisteners

import model.Notes

interface ItemClickListener {
    fun onClick(notes: Notes?)
}