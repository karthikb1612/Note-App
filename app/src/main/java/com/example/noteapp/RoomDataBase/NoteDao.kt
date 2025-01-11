package com.example.noteapp.RoomDataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.noteapp.Note

@Dao
 interface NoteDao {
    @Insert()
    suspend fun addNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<Note>>
}