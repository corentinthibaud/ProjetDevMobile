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
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.m1.devlogiciel.projetdevmobile.activity.ui.theme.ProjetDevMobileTheme
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteColorEnum
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteStateEnum
import fr.isen.m1.devlogiciel.projetdevmobile.model.PistesModel
import java.util.Random
import androidx.compose.material3.OutlinedCard as OutlinedCard

class PistesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pistesModel = PistesModel(
            pistes = listOf(
                PisteModel(
                    name = "Piste A",
                    color = PisteColorEnum.RED,
                    state = PisteStateEnum.UNREPORTED,
                    status = false
                ),
                PisteModel(
                    name = "Piste B",
                    color = PisteColorEnum.BLACK,
                    state = PisteStateEnum.UNREPORTED,
                    status = true
                ),
                PisteModel(
                    name = "Piste C",
                    color = PisteColorEnum.values().random(),
                    state = PisteStateEnum.UNREPORTED,
                    status = Random().nextBoolean()
                ),
                PisteModel(
                    name = "Piste A",
                    color = PisteColorEnum.RED,
                    state = PisteStateEnum.UNREPORTED,
                    status = false
                ),
                PisteModel(
                    name = "Piste B",
                    color = PisteColorEnum.BLACK,
                    state = PisteStateEnum.UNREPORTED,
                    status = true
                ),
                PisteModel(
                    name = "Piste C",
                    color = PisteColorEnum.values().random(),
                    state = PisteStateEnum.UNREPORTED,
                    status = Random().nextBoolean()
                ),
                PisteModel(
                    name = "Piste A",
                    color = PisteColorEnum.RED,
                    state = PisteStateEnum.UNREPORTED,
                    status = false
                ),
                PisteModel(
                    name = "Piste B",
                    color = PisteColorEnum.BLACK,
                    state = PisteStateEnum.UNREPORTED,
                    status = true
                ),
                PisteModel(
                    name = "Piste C",
                    color = PisteColorEnum.values().random(),
                    state = PisteStateEnum.UNREPORTED,
                    status = Random().nextBoolean()
                ),
                PisteModel(
                    name = "Piste A",
                    color = PisteColorEnum.RED,
                    state = PisteStateEnum.UNREPORTED,
                    status = false                            ),
                PisteModel(
                    name = "Piste B",
                    color = PisteColorEnum.BLACK,
                    state = PisteStateEnum.UNREPORTED,
                    status = true
                ),
                PisteModel(
                    name = "Piste C",
                    color = PisteColorEnum.values().random(),
                    state = PisteStateEnum.UNREPORTED,
                    status = Random().nextBoolean()
                ),
                PisteModel(
                    name = "Piste A",
                    color = PisteColorEnum.RED,
                    state = PisteStateEnum.UNREPORTED,
                    status = false                            ),
                PisteModel(
                    name = "Piste B",
                    color = PisteColorEnum.BLACK,
                    state = PisteStateEnum.UNREPORTED,
                    status = true
                ),
                PisteModel(
                    name = "Piste C",
                    color = PisteColorEnum.values().random(),
                    state = PisteStateEnum.UNREPORTED,
                    status = Random().nextBoolean()
                ),
                PisteModel(
                    name = "Piste A",
                    color = PisteColorEnum.RED,
                    state = PisteStateEnum.UNREPORTED,
                    status = false                            ),
                PisteModel(
                    name = "Piste B",
                    color = PisteColorEnum.BLACK,
                    state = PisteStateEnum.UNREPORTED,
                    status = true
                ),
                PisteModel(
                    name = "Piste C",
                    color = PisteColorEnum.values().random(),
                    state = PisteStateEnum.UNREPORTED,
                    status = Random().nextBoolean()
                )
            )
        )
        setContent {
            ProjetDevMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Header("Pistes")
                        LazyColumn (
                            modifier = Modifier.fillMaxSize()
                                .padding(top = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(pistesModel.pistes) { piste ->
                                val color: Color = when (piste.color) {
                                    PisteColorEnum.BLUE -> Color.Blue
                                    PisteColorEnum.GREEN -> Color(0xFF1EAB05)
                                    PisteColorEnum.RED -> Color.Red
                                    PisteColorEnum.BLACK -> Color.Black
                                }
                                CardPiste(piste, color)
                            }
                        }
                    }
                }
            }
        }
    }
}

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
fun CardPiste(piste : PisteModel, color: Color) {
    val context = LocalContext.current

    OutlinedCard(
        border = BorderStroke(1.dp, Color.Gray),
        modifier = Modifier
            .fillMaxWidth(0.8f)
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
            Text(text = piste.name, modifier = Modifier.padding(start = 20.dp))
            Spacer(modifier = Modifier.weight(1f))
            if(piste.status) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1EAB05))
                        .padding(10.dp)
                        .fillMaxWidth(0.35f)
                ) {
                    Text(text = "Ouverte", color = Color.White)
                }
            } else {
                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .padding(10.dp)
                        .fillMaxWidth(0.35f)
                ) {
                    Text(text = "Fermée", color = Color.White)
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