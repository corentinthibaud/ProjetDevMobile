package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.model.ChatModel

class ChatActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface {
                ChatView()
            }
        }
    }

    @Composable
    fun ChatView() {
        val message = remember { mutableStateListOf<ChatModel>() }
        if(message.size < 1) {
            message.add(ChatModel("user1", "Ceci est un message qui ce doit d'être très long avec pleins d'accents et caractères un peu bizarre afin de voir le rendu sur l'écran"))
            message.add(ChatModel("user2", "Test"))
        }
        LazyColumn {
            if (message.size < 1) {
                item(message) {
                    Text("No message to display")
                }
            } else {
                items(message) {
                    Card(modifier = Modifier.padding(12.dp)) {
                        Text(it.user, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                        Text(it.message, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp))
                    }
                }
            }
        }
        Box(modifier = Modifier.run { fillMaxSize().padding(20.dp) }) {
            Row(modifier = Modifier.align(Alignment.BottomEnd)) {
                var messageToSend by remember { mutableStateOf("") }
                TextField(value = messageToSend, onValueChange = {messageToSend = it}, placeholder = {Text("Your message")})
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(R.drawable.baseline_send_24), contentDescription = "Send")
                }
            }
        }
    }
}