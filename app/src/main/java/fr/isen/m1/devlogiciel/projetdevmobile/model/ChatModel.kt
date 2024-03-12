package fr.isen.m1.devlogiciel.projetdevmobile.model

class ChatModel (var user: String, var message: String)

class ChatsModel (var chats: MutableList<ChatModel>)