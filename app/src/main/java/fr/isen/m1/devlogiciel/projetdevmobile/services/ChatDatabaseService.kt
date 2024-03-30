package fr.isen.m1.devlogiciel.projetdevmobile.services

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.m1.devlogiciel.projetdevmobile.model.ChatModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.ChatsModel

class ChatDatabaseService {
    private val database = FirebaseDatabase.getInstance()

    val chatsReference = database.getReference("chat")

    fun getChat(): LiveData<ChatsModel> {
        return object : LiveData<ChatsModel>() {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatList = ArrayList<ChatModel>()
                    for (chat in snapshot.children) {
                        val chatItem = chat.getValue(ChatModel::class.java)
                        chatItem?.let { chatList.add(it) }
                    }
                    value = ChatsModel(chatList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatDatabaseService", "Error getting data", error.toException())
                }
            }

            override fun onActive() {
                chatsReference.addValueEventListener(listener)
            }

            override fun onInactive() {
                chatsReference.removeEventListener(listener)
            }
        }
    }

    fun addChat(chat: ChatModel, id: Int) {
        chatsReference.child(id.toString()).setValue(chat)
    }
}