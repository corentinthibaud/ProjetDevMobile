package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.model.CommentModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainModel
import fr.isen.m1.devlogiciel.projetdevmobile.samples.ConnectionDatabaseSample
import kotlinx.coroutines.launch

class MountainDetailsActivity: ComponentActivity() {
    private lateinit var mountain : MountainModel
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (intent.getSerializableExtra("mountain") !== null && intent.getSerializableExtra("index") !== null) {
                mountain = intent.getSerializableExtra("mountain") as MountainModel
            } else {
                val intent = Intent(this@MountainDetailsActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            val index = intent.getSerializableExtra("index") as Int
            Surface {
                Scaffold(
                    bottomBar = {
                        NavBar("Home")
                    },
                    topBar = {
                        Column(
                            modifier = Modifier.padding(bottom = 5.dp)
                        ) {
                            Header("Home")
                        }
                    }
                ) { content ->
                    val comments: List<CommentModel> = ArrayList()
                    comments.plus(CommentModel("User1", "Comment1", "PisteTest"))
                    val sheetState = rememberModalBottomSheetState()
                    var showEditionForm by remember { mutableStateOf(false) }
                    var showBottomSheet by remember { mutableStateOf(false) }
                    val status = remember { mutableStateOf(mountain.status ?: false) }
                    val scope = rememberCoroutineScope()
                    LazyColumn(
                        Modifier
                            .padding(content)
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        item(mountain) {
                            Box(Modifier.fillMaxWidth()) {
                                Text(
                                    mountain.name ?: "No name",
                                    modifier = Modifier.align(Alignment.Center),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                IconButton(onClick = {
                                    showEditionForm = true
                                    showBottomSheet = true
                                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_create_24),
                                        contentDescription = "Edit"
                                    )
                                }
                            }
                            Row {
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
                                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.12f
                                        ),
                                        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.38f
                                        ),
                                        disabledLeadingIconContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.38f
                                        ),
                                        disabledTrailingIconContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.38f
                                        ),
                                    )

                                )
                                SuggestionChip(
                                    label = { Text(mountain.type?.string.toString()) },
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Box(Modifier.fillMaxWidth()) {
                                Text(
                                    "Comments",
                                    modifier = Modifier.align(Alignment.Center),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        itemsIndexed(comments) { _, it ->
                            ListItem(
                                headlineContent = { Text(text = it.user ?: "Unknown") },
                                supportingContent = {
                                    Text(text = it.comment ?: "No comment")
                                })
                        }
                    }
                    Box(
                        modifier = Modifier.run { fillMaxSize().padding(20.dp) },
                    ) {
                        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
                            ExtendedFloatingActionButton(onClick = {
                                showBottomSheet = true
                                showEditionForm = false
                            },
                                icon = {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_add_24),
                                        contentDescription = "FAB"
                                    )
                                },
                                text = { Text("Add a comment") }
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(20.dp)
                            .background(color = MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        if (showBottomSheet) {
                            val tmpStatus = remember { mutableStateOf(status.value) }
                            ModalBottomSheet(
                                onDismissRequest = { showBottomSheet = false },
                                sheetState = sheetState
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
                                ) {
                                    val ctaLabel = if (showEditionForm) {
                                        EditionForm(tmpStatus)
                                        "Edit"
                                    } else {
                                        CommentForm()
                                        "Comment"
                                    }
                                    Row {
                                        Button(onClick = {
                                            if (tmpStatus.value != status.value) {
                                                status.value = tmpStatus.value
                                                ConnectionDatabaseSample().sendStatus(
                                                    status.value,
                                                    false,
                                                    index
                                                )
                                            }
                                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    showBottomSheet = false
                                                }
                                            }
                                        }, modifier = Modifier.padding(end = 10.dp)) {
                                            Text(text = ctaLabel)
                                        }
                                        OutlinedButton(onClick = {
                                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    showBottomSheet = false
                                                }
                                            }
                                        }) {
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
    private fun EditionForm(status: MutableState<Boolean>) {
        Text("Edition form", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 20.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Status", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(end = 10.dp))
            Switch(checked = status.value, onCheckedChange = {status.value = it} )
        }
    }

    @Composable
    private fun CommentForm () {
        var text by remember { mutableStateOf("") }
        Text("Add a comment", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 20.dp))
        TextField(value = text, onValueChange = {text = it}, minLines = 3, modifier = Modifier.fillMaxWidth())
    }
}