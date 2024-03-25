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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.activity.ui.theme.ProjetDevMobileTheme
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainsModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopeModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopesModel
import fr.isen.m1.devlogiciel.projetdevmobile.samples.ConnectionDatabaseSample

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedTabIndex by remember { mutableStateOf(0) }
            val slopesModel = remember { mutableStateOf<SlopesModel?>(null) }
            val isLoading = remember { mutableStateOf(true) }
            val statusFilter = remember { mutableStateOf<Boolean?>(null) }
            val colorFilter = remember { mutableStateOf<SlopeModel.Companion.SlopeColorEnum?>(null) }
            val mountainsModel = remember { mutableStateOf<MountainsModel?>(null) }
            LaunchedEffect(Unit) {
                ConnectionDatabaseSample().getSlopes().observe(this@HomeActivity) { data ->
                    slopesModel.value = data
                    isLoading.value = false
                }
                ConnectionDatabaseSample().getMountains().observe(this@HomeActivity) { data ->
                    mountainsModel.value = data
                    isLoading.value = false
                }
            }
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

                            if (isLoading.value) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                var tmp by remember { mutableStateOf(slopesModel.value?.slopes) }
                                Column (
                                    modifier = Modifier.padding(content)
                                ){
                                    Column(
                                        modifier = Modifier.padding(5.dp)
                                    ) {
                                        SearchBar(onSearchTextChanged = { searchText ->
                                            tmp = slopesModel.value?.slopes?.filter { slope ->
                                                slope.name?.contains("^${Regex.escape(searchText)}.*".toRegex(RegexOption.IGNORE_CASE)) ?: false
                                            }
                                        })
                                        FilterStatus { status ->
                                            statusFilter.value = status
                                            tmp = slopesModel.value?.slopes?.filter { slope ->
                                                (statusFilter.value == null || slope.status == statusFilter.value) &&
                                                        (colorFilter.value == null || slope.color == colorFilter.value)
                                            }
                                        }
                                        FilterColor(onStateChange = { color ->
                                            colorFilter.value = color
                                            tmp = slopesModel.value?.slopes?.filter { slope ->
                                                (statusFilter.value == null || slope.status == statusFilter.value) &&
                                                        (colorFilter.value == null || slope.color == colorFilter.value)
                                            }
                                        })
                                    }
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        tmp?.let { typeTmp ->
                                            items(typeTmp) { slope ->
                                                val color: Color = when (slope.color) {
                                                    SlopeModel.Companion.SlopeColorEnum.BLUE -> Color.Blue
                                                    SlopeModel.Companion.SlopeColorEnum.GREEN -> Color(0xFF1EAB05)
                                                    SlopeModel.Companion.SlopeColorEnum.RED -> Color.Red
                                                    SlopeModel.Companion.SlopeColorEnum.BLACK -> Color.Black
                                                    else -> Color.Transparent
                                                }
                                                CardSlope(slope, color)
                                            }
                                        }
                                    }
                                }
                            }
                        } else {

                            if (isLoading.value) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                var tmp by remember { mutableStateOf(mountainsModel.value?.mountains) }
                                Column(
                                    modifier = Modifier.padding(content)
                                ) {
                                    Column (
                                        modifier = Modifier.padding(5.dp)
                                    ){
                                        SearchBar(onSearchTextChanged = { searchText ->
                                            tmp = mountainsModel.value?.mountains?.filter { mountain ->
                                                mountain.name?.contains(
                                                    "^${Regex.escape(searchText)}.*".toRegex(
                                                        RegexOption.IGNORE_CASE
                                                    )
                                                ) ?: false
                                            }
                                        })
                                        FilterStatus { status ->
                                            statusFilter.value = status
                                            tmp = if (status != null) {
                                                mountainsModel.value?.mountains?.filter { mountain ->
                                                    mountain.status == status
                                                }
                                            } else {
                                                mountainsModel.value?.mountains
                                            }
                                        }
                                    }
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        tmp?.let { typeTmp ->
                                            items(typeTmp) { mountain ->
                                                val icon = when (mountain.type) {
                                                    MountainModel.Companion.MoutainTypeEnum.TELESKI -> R.drawable.ski_lift
                                                    MountainModel.Companion.MoutainTypeEnum.TELESIEGE -> R.drawable.telesiege
                                                    else -> R.drawable.baseline_error_24
                                                }
                                                CardMountain(mountain, icon)
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