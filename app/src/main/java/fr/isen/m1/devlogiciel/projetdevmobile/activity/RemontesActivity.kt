package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.activity.ui.theme.ProjetDevMobileTheme
import fr.isen.m1.devlogiciel.projetdevmobile.model.RemonteeModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.RemonteeTypeEnum
import fr.isen.m1.devlogiciel.projetdevmobile.model.RemonteesModel
import fr.isen.m1.devlogiciel.projetdevmobile.samples.ConnectionDatabaseSample

class RemontesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetDevMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RemonteeActivityScreen()
                }
            }
        }
    }
}

@Composable
fun RemonteeActivityScreen() {
    val remonteesModel = remember { mutableStateOf<RemonteesModel?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        remonteesModel.value = ConnectionDatabaseSample().getRemonteeFromDatabase()
        isLoading.value = false
    }

    if (isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold (
            bottomBar = {
                NavBar()
            },
            topBar = {
                Header("Remontées")
            }
        ) { content ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp, bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val tmp = remonteesModel.value
                tmp?.remontees?.let {
                    items(tmp.remontees) { remontee ->
                        val icon = when (remontee.type) {
                            RemonteeTypeEnum.TELESKI -> R.drawable.ski_lift
                            RemonteeTypeEnum.TELESIEGE -> R.drawable.telesiege
                            else -> R.drawable.baseline_error_24
                        }
                        CardRemontee(remontee, icon)
                    }
                }
            }
        }
    }
}

@Composable
fun CardRemontee(remontee : RemonteeModel, icon: Int) {
    val context = LocalContext.current

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
            remontee.name?.let { Text(text = remontee.name, modifier = Modifier.padding(start = 10.dp).fillMaxWidth(0.63f)) }
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

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ProjetDevMobileTheme {
        Greeting2("Android")
    }
}