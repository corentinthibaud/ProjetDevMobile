package fr.isen.m1.devlogiciel.projetdevmobile.model

import java.io.Serializable

data class CommentModel (
    val user: String? = null,
    val comment: String? = null,
): Serializable