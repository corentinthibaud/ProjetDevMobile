package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.m1.devlogiciel.projetdevmobile.activity.ui.theme.ProjetDevMobileTheme
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteColorEnum
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.PistesModel
import fr.isen.m1.devlogiciel.projetdevmobile.samples.ConnectionDatabaseSample
import androidx.compose.material3.OutlinedCard as OutlinedCard

class PistesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetDevMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PistesActivityScreen()
                }
            }
        }
    }
}

@Composable
fun PistesActivityScreen() {
    val context = LocalContext.current
    val pistesModel = remember { mutableStateOf<PistesModel?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    val cachesPistesModel = remember {  mutableStateOf<PistesModel?>(null)  }
    if(cachesPistesModel.value == null) {
        cachesPistesModel.value = ConnectionDatabaseSample().getPisteCache(context)
    }

    LaunchedEffect(Unit) {
        if(cachesPistesModel.value == null) {
            pistesModel.value = ConnectionDatabaseSample().getPistesFromDatabase()

            pistesModel.value?.let { ConnectionDatabaseSample().insertPisteCache(it, context) }
        } else {
            pistesModel.value = cachesPistesModel.value
        }
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
        var tmp by remember { mutableStateOf(pistesModel.value?.pistes) }
        Scaffold (
            bottomBar = {
                NavBar("Piste")
            },
            topBar = {
                Column {
                    Header("Pistes")
                    SearchBar(onSearchTextChanged = { searchText ->
                        tmp = pistesModel.value?.pistes?.filter { piste ->
                            piste.name?.contains("^${Regex.escape(searchText)}.*".toRegex(RegexOption.IGNORE_CASE)) ?: false
                        }
                    })
                    FilterStatus { status ->
                        tmp = if(status != null) {
                            pistesModel.value?.pistes?.filter { piste ->
                                piste.status == status
                            }
                        } else {
                            pistesModel.value?.pistes
                        }
                    }
                }
            }
        ) { content ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(content),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                tmp?.let { typeTmp ->
                    items(typeTmp) { piste ->
                        val color: Color = when (piste.color) {
                            PisteColorEnum.BLUE -> Color.Blue
                            PisteColorEnum.GREEN -> Color(0xFF1EAB05)
                            PisteColorEnum.RED -> Color.Red
                            PisteColorEnum.BLACK -> Color.Black
                            else -> Color.Transparent
                        }
                        CardPiste(piste, color)
                    }
                }
            }
        }
    }
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjetDevMobileTheme {
        Greeting("Android")
    }
}