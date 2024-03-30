package fr.isen.m1.devlogiciel.projetdevmobile.model

import java.io.Serializable

data class ChatModel (var user: String? = null, var message: String? = null): Serializable

data class ChatsModel (var chats: List<ChatModel>): Serializable
