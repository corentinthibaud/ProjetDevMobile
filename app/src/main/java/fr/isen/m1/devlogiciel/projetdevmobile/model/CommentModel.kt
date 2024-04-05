package fr.isen.m1.devlogiciel.projetdevmobile.model

import java.io.Serializable

data class CommentModel (
    val user: String? = null,
    val message: String? = null,
): Serializable