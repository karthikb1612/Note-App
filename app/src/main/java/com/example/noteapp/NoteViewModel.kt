package com.example.noteapp

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.RoomDataBase.NoteDatabase
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class NoteViewModel(application: Application): AndroidViewModel(application) {

    private val noteDao = NoteDatabase.getDatabase(application).noteDao()
    val notes: LiveData<List<Note>> get() = noteDao.getAllNotes()

    fun addNote(title: String,content: String,) {
        val timestamp = java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(
            Date()
        )
        val note = Note(
            title = title, content = content, timestamp = timestamp)
        viewModelScope.launch {
            noteDao.addNote(note)
        }
    }
    fun remove(note: Note){
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }
}