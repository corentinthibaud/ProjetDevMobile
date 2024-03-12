package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.activity.ui.theme.ProjetDevMobileTheme
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteColorEnum
import fr.isen.m1.devlogiciel.projetdevmobile.model.PistesModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.RemonteeTypeEnum
import fr.isen.m1.devlogiciel.projetdevmobile.model.RemonteesModel
import fr.isen.m1.devlogiciel.projetdevmobile.samples.ConnectionDatabaseSample

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedTabIndex by remember { mutableStateOf(0) }
            ProjetDevMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold (
                        bottomBar = {
                            NavBar("Home")
                        },
                        topBar = {
                            Column (
                                modifier = Modifier.padding(bottom = 5.dp)
                            ){
                                Header("Home")
                                TabRow(
                                    selectedTabIndex = selectedTabIndex,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                                    contentColor = MaterialTheme.colorScheme.onBackground,
                                    tabs = {
                                        Tab(
                                            selected = selectedTabIndex == 0,
                                            onClick = { selectedTabIndex = 0 },
                                            text = { Text(text = "Tab 1", color = Color.Black) },
                                        )
                                        Tab(
                                            selected = selectedTabIndex == 1,
                                            onClick = { selectedTabIndex = 1 },
                                            text = { Text(text = "Tab 2", color = Color.Black) },
                                        )
                                    }
                                )
                            }
                        }
                    ) { content ->
                        if(selectedTabIndex == 0) {
                            val context = LocalContext.current
                            val pistesModel = remember { mutableStateOf<PistesModel?>(null) }
                            val isLoading = remember { mutableStateOf(true) }
                            val statusFilter = remember { mutableStateOf<Boolean?>(null) }
                            val colorFilter = remember { mutableStateOf<PisteColorEnum?>(null) }

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
                                Column (
                                    modifier = Modifier.padding(content)
                                ){
                                    Column(
                                        modifier = Modifier.padding(5.dp)
                                    ) {
                                        SearchBar(onSearchTextChanged = { searchText ->
                                            tmp = pistesModel.value?.pistes?.filter { piste ->
                                                piste.name?.contains("^${Regex.escape(searchText)}.*".toRegex(RegexOption.IGNORE_CASE)) ?: false
                                            }
                                        })
                                        FilterStatus { status ->
                                            statusFilter.value = status
                                            tmp = pistesModel.value?.pistes?.filter { piste ->
                                                (statusFilter.value == null || piste.status == statusFilter.value) &&
                                                        (colorFilter.value == null || piste.color == colorFilter.value)
                                            }
                                        }
                                        FilterColor(onStateChange = { color ->
                                            colorFilter.value = color
                                            tmp = pistesModel.value?.pistes?.filter { piste ->
                                                (statusFilter.value == null || piste.status == statusFilter.value) &&
                                                        (colorFilter.value == null || piste.color == colorFilter.value)
                                            }
                                        })
                                    }
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
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
                        } else {
                            val context = LocalContext.current
                            val remonteesModel = remember { mutableStateOf<RemonteesModel?>(null) }
                            val isLoading = remember { mutableStateOf(true) }
                            val statusFilter = remember { mutableStateOf<Boolean?>(null) }

                            val cachesRemonteesModel = remember {  mutableStateOf<RemonteesModel?>(null)  }
                            if(cachesRemonteesModel.value == null) {
                                cachesRemonteesModel.value = ConnectionDatabaseSample().getRemonteeCache(context)
                            }

                            LaunchedEffect(Unit) {
                                if(cachesRemonteesModel.value == null) {
                                    remonteesModel.value = ConnectionDatabaseSample().getRemonteeFromDatabase()

                                    remonteesModel.value?.let { ConnectionDatabaseSample().insertRemonteeCache(it, context) }
                                } else {
                                    remonteesModel.value = cachesRemonteesModel.value
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
                                var tmp by remember { mutableStateOf(remonteesModel.value?.remontees) }
                                Column(
                                    modifier = Modifier.padding(content)
                                ) {
                                    Column (
                                        modifier = Modifier.padding(5.dp)
                                    ){
                                        SearchBar(onSearchTextChanged = { searchText ->
                                            tmp = remonteesModel.value?.remontees?.filter { remontee ->
                                                remontee.name?.contains(
                                                    "^${Regex.escape(searchText)}.*".toRegex(
                                                        RegexOption.IGNORE_CASE
                                                    )
                                                ) ?: false
                                            }
                                        })
                                        FilterStatus { status ->
                                            statusFilter.value = status
                                            tmp = if (status != null) {
                                                remonteesModel.value?.remontees?.filter { remontee ->
                                                    remontee.status == status
                                                }
                                            } else {
                                                remonteesModel.value?.remontees
                                            }
                                        }
                                    }
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        tmp?.let { typeTmp ->
                                            items(typeTmp) { remontee ->
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
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    ProjetDevMobileTheme {
        Greeting3("Android")
    }
}