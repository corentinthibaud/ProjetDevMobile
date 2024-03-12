package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteColorEnum
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.RemonteeModel

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
    BasicTextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            onSearchTextChanged(newText)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 5.dp)
            .height(30.dp),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp,
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = Color(0XFF4c8acc),
                        shape = RoundedCornerShape(10.dp)
                    )

            ) {
                Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                    if(text.isEmpty()) {
                        Text("Recherche...", modifier = Modifier.alpha(0.3f))
                    }
                    innerTextField.invoke()
                }
            }
        }
    )
}

@Composable
fun FilterStatus(onStateChange: (Boolean?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember {  mutableStateOf("Tous")  }
    Row (
        modifier = Modifier.padding(top = 5.dp)
    ){
        Text(text = "Status de la piste :",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 20.dp)
        )
        Box (
            modifier = Modifier
                .fillMaxWidth(0.5f),
            contentAlignment = Alignment.CenterEnd
        ){
            Column {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .height(30.dp),
                    shape = RoundedCornerShape(0.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.weight(0.5f)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Tous") },
                        onClick = {
                            onStateChange(null)
                            text = "Tous"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Ouverte") },
                        onClick = {
                            onStateChange(true)
                            text = "Ouverte"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Fermée") },
                        onClick = {
                            onStateChange(false)
                            text = "Fermée"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterColor(onStateChange: (PisteColorEnum?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember {  mutableStateOf("Tous")  }
    Row (
        modifier = Modifier.padding(top = 5.dp)
    ){
        Text(text = "Couleur de la piste :",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 20.dp)
        )
        Box (
            modifier = Modifier
                .fillMaxWidth(0.5f),
            contentAlignment = Alignment.CenterEnd
        ){
            Column {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .height(30.dp),
                    shape = RoundedCornerShape(0.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.weight(0.5f)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Tous") },
                        onClick = {
                            onStateChange(null)
                            text = "All"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Bleu") },
                        onClick = {
                            onStateChange(PisteColorEnum.BLUE)
                            text = "Bleu"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Verte") },
                        onClick = {
                            onStateChange(PisteColorEnum.GREEN)
                            text = "Verte"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Rouge") },
                        onClick = {
                            onStateChange(PisteColorEnum.RED)
                            text = "Rouge"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Noire") },
                        onClick = {
                            onStateChange(PisteColorEnum.BLACK)
                            text = "Noire"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NavBar(activity: String) {

    val selectedActivity: ButtonNav = when(activity) {
        "Home" -> ButtonNav.Home
        "Itinéraires" -> ButtonNav.Itinerary
        "Chat" -> ButtonNav.Chat
        else -> {
            ButtonNav.Home
        }
    }

    val items = listOf(
        ButtonNav.Home,
        ButtonNav.Itinerary,
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
    data object Home :
        ButtonNav(
            "Home",
            R.drawable.baseline_home_24,
            HomeActivity::class.java
        )

    data object Itinerary :
        ButtonNav(
            "Itinéraires",
            R.drawable.baseline_error_24,
            HomeActivity::class.java
        )

    data object Chat:
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

@Composable
fun CardPiste(piste : PisteModel, color: Color) {
    OutlinedCard(
        border = BorderStroke(1.dp, Color.Gray),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(50.dp)
            .padding(2.dp),
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 5.dp)
        ){
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color)
                    .size(42.dp)
            ) {
                Image(painter = painterResource(
                    id = R.drawable.slope_white),
                    contentDescription = null,
                    modifier = Modifier
                        .size(38.dp)
                )
            }
            piste.name?.let { Text(text = it, modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(0.63f)) }
            Spacer(modifier = Modifier.weight(0.2f))
            piste.status?.let {
                if (piste.status) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF1EAB05))
                            .padding(10.dp)
                            .fillMaxWidth(0.85f)
                    ) {
                        Text(text = "Ouverte", color = Color.White)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(Color.Red)
                            .padding(10.dp)
                            .fillMaxWidth(0.85f)
                    ) {
                        Text(text = "Fermée", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CardRemontee(remontee : RemonteeModel, icon: Int) {
    OutlinedCard(
        border = BorderStroke(1.dp, Color.Gray),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(50.dp)
            .padding(2.dp),
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Image(painter = painterResource(
                id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 5.dp)
            )
            remontee.name?.let { Text(text = remontee.name, modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(0.63f)) }
            Spacer(modifier = Modifier.weight(0.2f))
            remontee.status?.let {
                if(remontee.status) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF1EAB05))
                            .padding(10.dp)
                            .fillMaxWidth(0.85f)
                    ) {
                        Text(text = "Ouvert", color = Color.White)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(Color.Red)
                            .padding(10.dp)
                            .fillMaxWidth(0.85f)
                    ) {
                        Text(text = "Fermé", color = Color.White)
                    }
                }
            }
        }
    }
}