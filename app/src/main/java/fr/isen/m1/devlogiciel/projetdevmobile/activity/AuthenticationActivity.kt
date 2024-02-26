package fr.isen.m1.devlogiciel.projetdevmobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.activity.compose.setContent
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.m1.devlogiciel.projetdevmobile.R

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var authFirebase : FirebaseAuth
    private var isSignedIn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authFirebase = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = authFirebase.currentUser
        isSignedIn = currentUser != null
        if(!isSignedIn){
            setContent{
                SignInUp("signIn")
            }
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

    @Composable
    fun SignInUp(typeOfConnexion: String) {
        var email by remember { mutableStateOf(TextFieldValue()) }
        var password by remember { mutableStateOf(TextFieldValue()) }

        Column {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),

                keyboardActions = KeyboardActions(onDone = {
                    if(typeOfConnexion == "signIn"){
                        signIn(email.text, password.text)
                    }else{
                        signUp(email.text, password.text)
                    }
                })
            )
            Button(
                onClick = {
                    if(typeOfConnexion == "signIn"){
                        signIn(email.text, password.text)
                    }else{
                        signUp(email.text, password.text)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign In")
            }
        }


    }
}