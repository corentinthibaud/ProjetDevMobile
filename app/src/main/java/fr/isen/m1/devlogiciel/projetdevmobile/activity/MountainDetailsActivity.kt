package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.SheetState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.auth.FirebaseAuth
import fr.isen.m1.devlogiciel.projetdevmobile.R
import fr.isen.m1.devlogiciel.projetdevmobile.activity.ui.theme.ProjetDevMobileTheme
import fr.isen.m1.devlogiciel.projetdevmobile.model.MountainModel
import fr.isen.m1.devlogiciel.projetdevmobile.services.MountainDatabaseService
import kotlinx.coroutines.launch

class MountainDetailsActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (intent.getSerializableExtra("mountain") === null || intent.getSerializableExtra("index") === null) {
                val intent = Intent(this@MountainDetailsActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            val index = intent.getSerializableExtra("index") as Int
            ProjetDevMobileTheme {
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
                    ) { paddingValues ->
                        val mountainModel = remember { mutableStateOf<MountainModel?>(null) }
                        val sheetState = rememberModalBottomSheetState()
                        var showEditionForm by remember { mutableStateOf(false) }
                        val showBottomSheet = remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            MountainDatabaseService().getMountain(intent.getSerializableExtra("index") as Int)
                                .observe(this@MountainDetailsActivity) { data ->
                                    mountainModel.value = data
                                    Log.i("Data", "Mountain: " + mountainModel.value)
                                }
                        }
                        LazyColumn {
                            item {
                                Box(Modifier
                                    .padding(top = paddingValues.calculateTopPadding())
                                    .fillMaxWidth()) {
                                    Text(
                                        mountainModel.value?.name ?: "No name",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    IconButton(onClick = {
                                        showEditionForm = true
                                        showBottomSheet.value = true
                                    }, modifier = Modifier.align(Alignment.CenterEnd)) {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_create_24),
                                            contentDescription = "Edit"
                                        )
                                    }
                                }
                                Row {
                                    val open = if (mountainModel.value?.status == true) "Open" else "Closed"

                                    SuggestionChip(
                                        label = { Text(open) },
                                        onClick = {  },
                                        modifier = Modifier.padding(10.dp),
                                        colors = ChipColors(
                                            containerColor = if (mountainModel.value?.status == true) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                                            labelColor = if (mountainModel.value?.status == true) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
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
                                        label = { Text(mountainModel.value?.type?.string.toString()) },
                                        onClick = {  },
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
                                Box(Modifier.fillMaxWidth()) {
                                    var i = 0
                                    mountainModel.value?.comments?.let {
                                        it.forEach { comment ->
                                            ListItem(
                                                modifier = Modifier.padding(top = (72 * i).dp),
                                                headlineContent = {
                                                    Text(
                                                        text = comment.user ?: "Unknown"
                                                    )
                                                }, supportingContent = {
                                                    Text(text = comment.message ?: "No comment")
                                                }
                                            )
                                            i++
                                        }
                                    }
                                }
                            }
                        }
                        Box(
                            modifier = Modifier.run { fillMaxSize().padding(20.dp) },
                        ) {
                            Row(modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = paddingValues.calculateBottomPadding())) {
                                ExtendedFloatingActionButton(onClick = {
                                    showBottomSheet.value = true
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
                            if (showBottomSheet.value) {
                                ModalBottomSheet(
                                    onDismissRequest = { showBottomSheet.value = false },
                                    sheetState = sheetState
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
                                    ) {
                                        if (showEditionForm) {
                                            mountainModel.value?.status?.let{ it -> EditionForm(it, index, sheetState, showBottomSheet) }
                                        } else {
                                            CommentForm(index, mountainModel.value?.comments?.size ?: 0, sheetState, showBottomSheet)
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EditionForm(status: Boolean, index: Int, sheetState: SheetState, showBottomSheet: MutableState<Boolean>) {
        val scope = rememberCoroutineScope()
        val tmpStatus = remember { mutableStateOf(status) }
        Text("Edition form", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 20.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Status", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(end = 10.dp))
            Switch(checked = status, onCheckedChange = {tmpStatus.value = it} )
        }
        Row {
            Button(onClick = {
                if (tmpStatus.value != status) {
                    MountainDatabaseService().sendStatus(
                        tmpStatus.value,
                        index
                    )
                }
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                }
            }, modifier = Modifier.padding(end = 10.dp)) {
                Text(text = "Edit")
            }
            OutlinedButton(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                }
            }) {
                Text(text = "Cancel")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CommentForm(index: Int, commentSize: Int, sheetState: SheetState, showBottomSheet: MutableState<Boolean>) {
        var text by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        if(FirebaseAuth.getInstance().currentUser?.displayName?.isEmpty() == true) {
            Text("Just one last thing", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 20.dp))
            Text("To use this functionality, you must set a username. Please go to the profile page to set it up first")
            Button(onClick = {
                val intent = Intent(this@MountainDetailsActivity, ProfileActivity::class.java)
                startActivity(intent)
            }) {
                Text(text = "Go to profile")
            }
        } else {
            Text(
                "Add a comment",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                Button(onClick = {
                    MountainDatabaseService().sendComments(
                        FirebaseAuth.getInstance().currentUser?.displayName ?: "Unknown",
                        text,
                        index,
                        commentSize
                    )
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet.value = false
                        }
                    }
                }, modifier = Modifier.padding(end = 10.dp)) {
                    Text(text = "Comment")
                }
                OutlinedButton(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet.value = false
                        }
                    }
                }) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}