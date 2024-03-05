package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun SearchBar(onSearchTextChanged: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            onSearchTextChanged(newText)
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun FilterStatus(onStateChange: (Boolean?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Row {
        Text(text = "Status de la pistes :")
        Box {
            Button(
                onClick = { expanded = true }) {
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.weight(0.5f)
        ) {
            DropdownMenuItem(
                text = { Text(text = "All") },
                onClick = { onStateChange(null) }
            )
            DropdownMenuItem(
                text = { Text(text = "Ouverte") },
                onClick = { onStateChange(true) }
            )
            DropdownMenuItem(
                text = { Text(text = "Fermée") },
                onClick = { onStateChange(false) }
            )
        }
    }
}

@Composable
fun NavBar(activity: String) {

    val selectedActivity: ButtonNav = when(activity) {
        "Home" -> ButtonNav.Home
        "Piste" -> ButtonNav.Pistes
        "Remontee" -> ButtonNav.Remontees
        "Chat" -> ButtonNav.Chat
        else -> {
            ButtonNav.Home
        }
    }

    val items = listOf(
        ButtonNav.Home,
        ButtonNav.Pistes,
        ButtonNav.Remontees,
        ButtonNav.Chat
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

    object Chat:
        ButtonNav(
            "Chat",
            R.drawable.baseline_chat_24,
            ChatActivity::class.java
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