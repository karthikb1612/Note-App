package com.example.noteapp

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteApp(noteViewModel: NoteViewModel) {
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    val notes by noteViewModel.getAllDate.observeAsState(emptyList())
    val context = LocalContext.current

    var editingNote by remember { mutableStateOf<Note?>(null) }

    Box(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.img),
//            contentDescription = "Background Image",
//            contentScale = ContentScale.FillHeight,
//            modifier = Modifier.fillMaxSize()
//        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Text(
                    "NOTE ",
                    fontWeight = FontWeight.Bold, fontSize = 35.sp,
                    color = Color(0xFFEA034C)
                )
                Text(
                    "APP",
                    fontWeight = FontWeight.Bold, fontSize = 24.sp,
                    color = Color(0xFF0E0D0D)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            FloatingActionButton(
                onClick = {
                    if (title.isNotEmpty() && content.isNotEmpty()) {
                        if (editingNote != null) {
                            val updatedNote = editingNote!!.copy(
                                title = title,
                                content = content,
                                timestamp = SimpleDateFormat(
                                    "yyyy-MM-dd hh:mm a",
                                    Locale.getDefault()
                                ).format(Date())
                            )
                            noteViewModel.updateNote(updatedNote)
                            editingNote = null
                        } else {
                            noteViewModel.addNote(title, content)
                        }
                        title = ""
                        content = ""
                        Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.width(100.dp),
                contentColor = Color.White,
                containerColor = Color(0xFFEA034C)
            ) {
                Text(if (editingNote != null) "Update" else "Add Note")
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(notes, key = { it.id }) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .combinedClickable(
                                onClick = { /* Single click - no action */ },
                                onDoubleClick = {
                                    title = note.title
                                    content = note.content
                                    editingNote = note
                                }
                            ),
                        elevation = CardDefaults.cardElevation(4.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(
                                    text = " ${note.timestamp.toString().toUpperCase()} ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = note.title.uppercase(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF041367),
                                )
                                Text(
                                    text = note.content,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF380000),
                                    fontWeight = FontWeight.SemiBold,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                            IconButton(
                                onClick = {
                                    noteViewModel.remove(note)
                                    val mediaPlayer = MediaPlayer.create(context, R.raw.deletetwo)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener {
                                        mediaPlayer.release()
                                    }
                                    Toast.makeText(context,"Note is Deleted", Toast.LENGTH_LONG).show()
                                }
                            ) {
                                Icon(
                                    painterResource(R.drawable.baseline_delete_24),
                                    contentDescription = "Delete",
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}