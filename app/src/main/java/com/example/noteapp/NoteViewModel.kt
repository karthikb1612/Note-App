package com.example.noteapp

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.RoomDataBase.NoteDatabase
import com.example.noteapp.RoomDataBase.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class NoteViewModel(application: Application): AndroidViewModel(application) {

     val getAllDate: LiveData<List<Note>>
     val repository: NoteRepository
    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository= NoteRepository(noteDao)
        getAllDate=repository.getAllDate
    }

    fun addNote(title: String,content: String,) {
        val timestamp = java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(
            Date()
        )
        val note = Note(
            title = title, content = content, timestamp = timestamp)
        viewModelScope.launch(Dispatchers.IO) {
           repository.addUserData(note)
        }
    }
    fun remove(note: Note){
        viewModelScope.launch {
            repository.deleteUserDate(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUserData(note)
        }
    }
}