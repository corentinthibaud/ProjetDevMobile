package fr.isen.m1.devlogiciel.projetdevmobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import fr.isen.m1.devlogiciel.projetdevmobile.R

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var authFirebase : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authFirebase = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = authFirebase.currentUser
        if (currentUser != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
    }

    private fun signUp(email: String, password: String) {
        authFirebase.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                } else {
                    // If sign in fails, display a message to the user.

                }
            }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        authFirebase.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = authFirebase.currentUser
                } else {
                    // If sign in fails, display a message to the user.

                }
            }
        // [END sign_in_with_email]
    }
}