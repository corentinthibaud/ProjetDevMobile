package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.m1.devlogiciel.projetdevmobile.R

@Composable
fun Header(text: String) {
    val context = LocalContext.current
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.LightGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = text,
            fontSize = 20.sp
        )
    }
}

@Composable
fun NavBar(activity: String) {
    val context = LocalContext.current

    val selectedActivity: ButtonNav = when(activity) {
        "Home" -> ButtonNav.Home
        "Piste" -> ButtonNav.Pistes
        "Remontee" -> ButtonNav.Remontees
        else -> {
            ButtonNav.Home
        }
    }

    val items = listOf(
        ButtonNav.Home,
        ButtonNav.Pistes,
        ButtonNav.Remontees,
    )

    NavigationBar (
        modifier = Modifier.height(80.dp)
    ){
        items.forEach { item ->
            AddItem(
                button = item,
                isSelected = item == selectedActivity
            )
        }
    }
}

sealed class ButtonNav(var title: String, var icon: Int, var activity: Class<*>) {
    object Home :
        ButtonNav(
            "Home",
            R.drawable.baseline_home_24,
            HomeActivity::class.java
        )

    object Pistes :
        ButtonNav(
            "Pistes",
            R.drawable.slope_black,
            PistesActivity::class.java
        )

    object Remontees :
        ButtonNav(
            "Remontées",
            R.drawable.telesiege,
            RemontesActivity::class.java
        )
}

@Composable
fun RowScope.AddItem(button: ButtonNav, isSelected: Boolean) {
    val context = LocalContext.current
    NavigationBarItem(
        label = {
                Text(text = button.title)
        },
        modifier = Modifier.padding(top = 8.dp),
        selected = isSelected,
        alwaysShowLabel = true,
        onClick = {
            val intent = Intent(context, button.activity)
            context.startActivity(intent)
        },
        icon = { 
            Icon(
                painterResource(id = button.icon),
                contentDescription = button.title,
                modifier = Modifier
                    .size(30.dp)
            )
        })
}