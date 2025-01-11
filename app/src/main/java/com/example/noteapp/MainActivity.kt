package com.example.noteapp

import android.app.Application
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var viewModel= ViewModelProvider(this)[NoteViewModel::class.java]
            NoteApp(viewModel)
        }
    }
}

@Composable
fun NoteApp(noteViewModel: NoteViewModel= viewModel(factory = ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as Application))){
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    val notes by noteViewModel.notes.observeAsState(emptyList())
    val timestamp = LocalContext.current
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Text("NOTE ",
                    fontWeight = FontWeight.Bold, fontSize = 35.sp,
                    color = Color(0xFF6650a4))
                Text("APP",
                    fontWeight = FontWeight.Bold, fontSize = 20.sp,
                    color = Color.Black)
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
                        noteViewModel.addNote(title, content)
                        title = ""
                        content = ""
                    }
                },
                modifier = Modifier.width(100.dp),
                contentColor = Color.LightGray,
                containerColor = Color(0xFF7D5260)
            ) {
                Text("Add Note")
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(notes, key = {it.id}) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.LightGray
                            )
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
                                        text = " ${note.timestamp} ",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = note.title.uppercase(),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6650a4),

                                    )

                                    Text(text = note.content,
                                        style = MaterialTheme.typography.bodyLarge, color = Color(0xFF625b71),
                                        fontWeight = FontWeight.SemiBold,
                                        fontStyle = FontStyle.Italic)

                                }
                                IconButton(

                                    onClick = {
                                        noteViewModel.remove(note)
                                        val mediaPlayer = MediaPlayer.create(context, R.raw.deletetwo)
                                        mediaPlayer.start()
                                        mediaPlayer.setOnCompletionListener {
                                            mediaPlayer.release()

                                        }


                                    }
                                ) {
                                    Icon(painterResource(
                                        R.drawable.baseline_delete_24),
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

