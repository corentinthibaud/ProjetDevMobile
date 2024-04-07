package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.firebase.database.FirebaseDatabase

class LaunchActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val intent = Intent(this, AuthenticationActivity::class.java)
        Log.d("LaunchActivity", "Launching AuthenticationActivity")
        startActivity(intent)
    }
}