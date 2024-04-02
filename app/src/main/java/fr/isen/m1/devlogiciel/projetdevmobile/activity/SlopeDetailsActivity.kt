package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ChipColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopeModel
import fr.isen.m1.devlogiciel.projetdevmobile.samples.ConnectionDatabaseSample
import kotlinx.coroutines.launch

class SlopeDetailsActivity: ComponentActivity() {
    private lateinit var slope : SlopeModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                Scaffold (
                    bottomBar = {
                        NavBar("Home")
                    },
                    topBar = {
                        Column (
                            modifier = Modifier.padding(bottom = 5.dp)
                        ){
                            Header("Home")
                        }
                    }
                ) { content ->
                    if(intent.getSerializableExtra("slope") !== null && intent.getSerializableExtra("index") !== null) {
                        slope = intent.getSerializableExtra("slope") as SlopeModel
                    } else {
                        val intent = Intent(this@SlopeDetailsActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    val index = intent.getSerializableExtra("index") as Int
                    val sheetState = rememberModalBottomSheetState()
                    var showEditionForm by remember { mutableStateOf(false) }
                    var showBottomSheet by remember { mutableStateOf(false) }
                    val comments = remember { mutableStateOf(slope.comments) }
                    val status = remember { mutableStateOf(slope.status?: false) }
                    val state = remember { mutableStateOf(slope.state?: SlopeModel.Companion.SlopeStateEnum.UNREPORTED) }
                    val scope = rememberCoroutineScope()
                    LazyColumn(
                        Modifier
                            .padding(content)
                            .fillMaxHeight()
                            .fillMaxWidth()) {
                        item(slope) {
                            Box(Modifier.fillMaxWidth()) {
                                Text(slope.name ?: "No name", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.titleMedium)
                                IconButton(onClick = {
                                    showEditionForm = true
                                    showBottomSheet = true
                                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                                    Icon(painter = painterResource(R.drawable.baseline_create_24), contentDescription = "Edit")
                                }
                            }
                            Row {
                                SuggestionChip(
                                    label = { Text(slope.color?.string.toString()) },
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.padding(10.dp)
                                )
                                val open = if (status.value) "Open" else "Closed"

                                SuggestionChip(
                                    label = { Text(open) },
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.padding(10.dp),
                                    colors = ChipColors(
                                        containerColor = if (status.value) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                                        labelColor = if (status.value) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                                        leadingIconContentColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconContentColor = MaterialTheme.colorScheme.onSurface,
                                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                        disabledLeadingIconContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                        disabledTrailingIconContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                    )

                                )
                                SuggestionChip(
                                    label = { Text(state.value.toString()) },
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Box(Modifier.fillMaxWidth()) {
                                Text("Comments", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.titleMedium)
                            }
                        }
                        comments.value?.let {
                            itemsIndexed(it) { _, it ->
                                ListItem(headlineContent = { Text(text = it.user ?: "Unknown") }, supportingContent = {
                                    Text(text = it.comment ?: "No comment")
                                })
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.run { fillMaxSize().padding(20.dp) },
                    ){
                        Row(modifier = Modifier.align(Alignment.BottomEnd)){
                            ExtendedFloatingActionButton(onClick = {
                                showBottomSheet = true
                                showEditionForm = false
                            },
                                icon = {Icon(painter = painterResource(R.drawable.baseline_add_24), contentDescription = "FAB")},
                                text = {Text("Add a comment")}
                            )
                        }
                    }
                    Box(modifier = Modifier
                        .width(200.dp)
                        .padding(20.dp)
                        .background(color = MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                        if(showBottomSheet) {
                            val tmpStatus = remember { mutableStateOf(status.value) }
                            val tmpState = remember { mutableStateOf(state.value) }
                            ModalBottomSheet(onDismissRequest = { showBottomSheet = false }, sheetState = sheetState) {
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
                                ) {
                                    val ctaLabel = if(showEditionForm) {
                                        EditionForm(tmpStatus, tmpState)
                                        "Edit"
                                    } else {
                                        CommentForm()
                                        "Comment"
                                    }
                                    Row {
                                        Button(onClick = {
                                            if(tmpStatus.value != status.value) {
                                                status.value = tmpStatus.value
                                                ConnectionDatabaseSample().sendStatus(status.value, true, index)
                                            }
                                            if(tmpState.value != state.value) {
                                                state.value = tmpState.value
                                                ConnectionDatabaseSample().sendState(state.value, index)
                                            }
                                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    showBottomSheet = false
                                                }
                                            } }, modifier = Modifier.padding(end = 10.dp)) {
                                            Text(text = ctaLabel)
                                        }
                                        OutlinedButton(onClick = { scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        } }) {
                                            Text(text = "Cancel")
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
    private fun EditionForm(status: MutableState<Boolean>, state: MutableState<SlopeModel.Companion.SlopeStateEnum>) {
        var dropdownExpanded by remember { mutableStateOf(false) }
        Text("Edition form", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 20.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Status", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(end = 10.dp))
            Switch(checked = status.value, onCheckedChange = {status.value = it} )
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 40.dp)) {
            Text(text = "State", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(end = 10.dp))
            Box {
                TextField(value = state.value.string, onValueChange = {}, readOnly = true, trailingIcon = {
                    IconButton(onClick = { dropdownExpanded = true }) {
                        if(dropdownExpanded) {
                            Icon(painter = painterResource(id = R.drawable.baseline_arrow_drop_up_24), contentDescription = "Close dropdown")
                        } else {
                            Icon(painter = painterResource(R.drawable.baseline_arrow_drop_down_24), contentDescription = "Dropdown")
                        }
                    }
                }, modifier = Modifier.clickable(onClick = { dropdownExpanded = true }))
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }) {
                    SlopeModel.Companion.SlopeStateEnum.entries.forEach {
                        DropdownMenuItem (text = { it.string }, onClick = { state.value = it })
                    }
                }
            }
        }
    }

    @Composable
    private fun CommentForm () {
        var text by remember { mutableStateOf("") }
        Text("Add a comment", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 20.dp))
        TextField(value = text, onValueChange = {text = it}, minLines = 3, modifier = Modifier.fillMaxWidth())
    }
}