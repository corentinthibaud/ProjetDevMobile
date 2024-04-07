package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import fr.isen.m1.devlogiciel.projetdevmobile.activity.ui.theme.ProjetDevMobileTheme

class ProfileActivity: ComponentActivity() {
    private lateinit var currentUser : FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
        } else {
            FirebaseAuth.getInstance().currentUser?.let {
                currentUser = it
            }
        }
        setContent {
            ProjetDevMobileTheme {
                Scaffold (
                    bottomBar = {
                        NavBar("Profile")
                    },
                    topBar = {
                        Column (
                            modifier = Modifier.padding(bottom = 5.dp)
                        ){
                            Header("Profile")
                        }
                    }
                ) {
                    ProfileView(it)
                }
            }
        }
    }

    @Composable
    fun ProfileView(paddingValues: PaddingValues) {
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                start = 8.dp,
                end = 8.dp
            )
        ) {
            Text("General information", style = MaterialTheme.typography.titleLarge)
            Text("All mandatory fields are marked with an asterisk *", fontStyle = FontStyle.Italic)
            var email by remember { mutableStateOf(currentUser.email ?: "") }
            var username by remember { mutableStateOf(currentUser.displayName ?: "") }

            TextField(value = email,
                onValueChange = { email = it },
                label = {Text("Email address *")},
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth())
            TextField(value = username,
                onValueChange = { username = it },
                label = {Text("Username")},
                modifier = Modifier.fillMaxWidth())

            HorizontalDivider(modifier = Modifier.padding(0.dp, 10.dp), thickness = 2.dp)

            Text(text = "Security", style = MaterialTheme.typography.titleLarge)
            Text(text = "Please fill this section only if you want to update your password",
                fontStyle = FontStyle.Italic)

            var password by rememberSaveable { mutableStateOf("") }
            var passwordConfirmation by rememberSaveable { mutableStateOf("") }
            TextField(value = password,
                onValueChange = {password = it},
                label = {Text("Password")},
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth())
            TextField(
                value = passwordConfirmation,
                onValueChange = {passwordConfirmation = it},
                label = {Text("Confirm your password")},
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth())

            Button(onClick = { updateProfile(email, username, password, passwordConfirmation) },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()) {
                Text("Update your profile")
            }
            Button(onClick = {
                val intent = Intent(this@ProfileActivity, AuthenticationActivity::class.java)
                FirebaseAuth.getInstance().signOut()
                startActivity(intent)
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonColors(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer, MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)) {
                Text("Sign out")
            }
        }
    }

    private fun updateProfile(
        email: String,
        username: String,
        password: String,
        passwordConfirmation: String
    ) {
        if(email != currentUser.email) {
            currentUser.verifyBeforeUpdateEmail(email)
        }

        if(username != currentUser.displayName) {
            currentUser.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(username).build()
            )
        }

        if (password.isNotEmpty() && password == passwordConfirmation) {
            currentUser.updatePassword(password)
        }
    }
}