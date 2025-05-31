package com.example.noteapp.RoomDataBase

import androidx.lifecycle.LiveData
import com.example.noteapp.Note

class NoteRepository(private val noteDao: NoteDao) {

    val getAllDate: LiveData<List<Note>> = noteDao.getAllNotes()

   suspend fun addUserData(note: Note){
        noteDao.addNote(note)
    }
    suspend fun updateUserData(note: Note){
        noteDao.updateNote(note)
    }
    suspend fun deleteUserDate(note: Note){
        noteDao.deleteNote(note)
    }
}