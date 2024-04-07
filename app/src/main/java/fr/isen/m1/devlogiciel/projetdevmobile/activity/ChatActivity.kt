package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.activity.ui.theme.ProjetDevMobileTheme
import fr.isen.m1.devlogiciel.projetdevmobile.model.ChatModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.ChatsModel
import fr.isen.m1.devlogiciel.projetdevmobile.services.ChatDatabaseService

class ChatActivity: ComponentActivity() {
    private lateinit var authFirebase: FirebaseAuth
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authFirebase = Firebase.auth

        if (authFirebase.currentUser == null) {
            val intent = Intent(this, AuthenticationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        setContent {
            ProjetDevMobileTheme {
                Scaffold(
                    topBar = {
                        Header("Chat")
                    },
                    bottomBar = {
                        NavBar("Chat")
                    }
                ) { paddingValues ->
                    if(authFirebase.currentUser?.displayName.isNullOrBlank()) {
                        SetUsername(paddingValues)
                    } else {
                        authFirebase.currentUser?.displayName?.let {
                            username = it
                        }
                        ChatView(paddingValues)
                    }
                }
            }
        }
    }

    @Composable
    fun ChatView(paddingValues: PaddingValues) {
        val chatsModel = remember { mutableStateOf<ChatsModel?>(null) }

        LaunchedEffect(Unit) {
            ChatDatabaseService().getChat().observe(this@ChatActivity) { data ->
                chatsModel.value = data
            }
            Log.d("CHAT", "Data fetched")
        }

        val messages = chatsModel.value?.chats

        if (messages.isNullOrEmpty()) {
            Log.d("CHAT", "No message has been fetched from the database.")
            Text(text = "No message to display", modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))
        } else {
            Log.d("CHAT", "There is a message")
            Log.d("CHAT", messages.toString())
            LazyColumn(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                items(messages) { chat ->
                    if (!chat.user.isNullOrBlank() && !chat.message.isNullOrBlank()) {
                        DisplayMessage(chat)
                    }
                }
            }
        }

        Box(modifier = Modifier.run { fillMaxSize().padding(bottom = paddingValues.calculateBottomPadding()) }) {
            Row(modifier = Modifier.align(Alignment.BottomEnd)) {
                var messageToSend by remember { mutableStateOf("") }
                TextField(
                    value = messageToSend,
                    onValueChange = {messageToSend = it},
                    placeholder = {Text("Your message")}
                )
                IconButton(onClick = {
                    if(username.isEmpty() || username.isBlank()) {
                        Toast.makeText(
                            this@ChatActivity,
                            "You cannot send a message as you didn't define a username",
                            Toast.LENGTH_LONG).show()
                    } else {
                        val newMessage = ChatModel(username, messageToSend)
                        chatsModel.value = chatsModel.value?.copy(chats = chatsModel.value?.chats.orEmpty() + listOf(newMessage))
                        ChatDatabaseService().addChat(newMessage,
                            chatsModel.value?.chats?.size?.plus(1) ?: 0
                        )
                    }
                    messageToSend = ""
                }) {
                    Icon(painter = painterResource(R.drawable.baseline_send_24), contentDescription = "Send")
                }
            }
        }
    }

    @Composable
    private fun DisplayMessage(chat: ChatModel) {
        if(chat.user.orEmpty() != authFirebase.currentUser?.displayName) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Card(modifier = Modifier.padding(12.dp)) {
                    Text(
                        chat.user.orEmpty(),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        chat.message.orEmpty(),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Card(
                    modifier = Modifier.padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                    Text(
                        chat.user.orEmpty(),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        chat.message.orEmpty(),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun SetUsername(paddingValues: PaddingValues) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = paddingValues.calculateTopPadding())) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth())
            {
                Text(
                    text = "Welcome on the chat !",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp))
            }
            Text(
                text = "To use this functionality, you must set a username. Please go to the profile page to set it up first",
                modifier = Modifier.padding(16.dp, 20.dp, 16.dp, 20.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                val intent = Intent(this@ChatActivity, ProfileActivity::class.java)
                startActivity(intent)
            }) {
                Text(text = "Go to profile")
            }
        }
    }
}