package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import fr.isen.m1.devlogiciel.projetdevmobile.activity.ui.theme.ProjetDevMobileTheme

class AuthenticationActivity : ComponentActivity() {
    private lateinit var authFirebase : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authFirebase = Firebase.auth
        FirebaseDatabase.getInstance().apply {
            setPersistenceEnabled(true)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = authFirebase.currentUser
        if (currentUser == null) {
            setContent {
                ProjetDevMobileTheme {
                    Surface {
                        if(intent.getStringExtra("VIEW") == "SIGNUP") {
                            SignupView()
                        }
                        else {
                            LoginView()
                        }
                    }
                }
            }
        } else {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun signUp(email: String, password: String) {
        authFirebase.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AuthenticationActivity, AuthenticationActivity::class.java)
                    intent.putExtra("VIEW", "LOGIN")
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        authFirebase.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    // If sign in fails, display a message to the user.
                }
            }
        // [END sign_in_with_email]
    }

    @Composable
    private fun LoginView() {
        var email by remember{mutableStateOf("")}
        var password by rememberSaveable { mutableStateOf("") }
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Beauregard ski-resort\nLog In", style = MaterialTheme.typography.titleLarge)
            }
            TextField(value = email, onValueChange = {email = it}, label = { Text("Email Address") }, keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Email), modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 10.dp))
            TextField(value = password, onValueChange = {password = it}, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Password), modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 10.dp))
            Button(onClick = { signIn(email, password) },modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 20.dp, 16.dp)) {
                Text("Sign In")
            }
            OutlinedButton(onClick = {
                val intent = Intent(this@AuthenticationActivity, AuthenticationActivity::class.java)
                intent.putExtra("VIEW", "SIGNUP")
                startActivity(intent)
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)) {
                Text("Sign Up")
            }
        }
    }

    @Composable
    private fun SignupView() {
        var email by remember{mutableStateOf("")}
        var password by rememberSaveable { mutableStateOf("") }
        var passwordConfirmation by rememberSaveable { mutableStateOf("") }
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Beauregard ski-resort\nSign Up", style = MaterialTheme.typography.titleLarge)
            }
            TextField(value = email, onValueChange = {email = it}, label = { Text("Email Address") }, keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Email), modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 10.dp))
            TextField(value = password, onValueChange = {password = it}, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Password), modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 10.dp))
            TextField(value = passwordConfirmation, onValueChange = {passwordConfirmation = it}, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Password), modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 10.dp))
            Button(onClick = {
                if(password.equals(passwordConfirmation)) {
                    signUp(email, password)
                }
                else {
                    Toast.makeText(this@AuthenticationActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            },modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 20.dp, 16.dp)) {
                Text("Sign Up")
            }
            OutlinedButton(onClick = {
                val intent = Intent(this@AuthenticationActivity, AuthenticationActivity::class.java)
                intent.putExtra("VIEW", "LOGIN")
                startActivity(intent)
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)) {
                Text("Sign In")
            }
        }
    }
}