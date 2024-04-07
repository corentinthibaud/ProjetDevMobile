package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import fr.isen.m1.devlogiciel.projetdevmobile.model.SlopeModel
import fr.isen.m1.devlogiciel.projetdevmobile.services.SlopeDatabaseService
import kotlinx.coroutines.launch

class SlopeDetailsActivity: ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetDevMobileTheme {
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
                    ) { paddingValues ->
                        if(intent.getSerializableExtra("slope") === null || intent.getSerializableExtra("index") === null) {
                            val intent = Intent(this@SlopeDetailsActivity, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        val index = intent.getSerializableExtra("index") as Int
                        val slopeModel = remember { mutableStateOf<SlopeModel?>(null)}
                        val sheetState = rememberModalBottomSheetState()
                        var showEditionForm by remember { mutableStateOf(false) }
                        val showBottomSheet = remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            SlopeDatabaseService().getSlope(intent.getSerializableExtra("index") as Int)
                                .observe(this@SlopeDetailsActivity) { data ->
                                    slopeModel.value = data
                                    Log.i("Data", "Slope: " + slopeModel.value)
                                }
                        }
                        LazyColumn(
                            Modifier
                                .padding(paddingValues)
                                .fillMaxHeight()
                                .fillMaxWidth()) {
                            item(slopeModel) {
                                Box(Modifier.fillMaxWidth()) {
                                    Text(
                                        slopeModel.value?.name ?: "No name",
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
                                    SuggestionChip(
                                        label = { Text(slopeModel.value?.color?.string.toString()) },
                                        onClick = {  },
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    val open =
                                        if (slopeModel.value?.status == true) "Open" else "Closed"

                                    SuggestionChip(
                                        label = { Text(open) },
                                        onClick = { /*TODO*/ },
                                        modifier = Modifier.padding(10.dp),
                                        colors = ChipColors(
                                            containerColor = if (slopeModel.value?.status == true) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                                            labelColor = if (slopeModel.value?.status == true) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
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
                                        label = { Text(slopeModel.value?.state?.string ?: SlopeModel.Companion.SlopeStateEnum.UNREPORTED.string) },
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
                                Box(Modifier.fillMaxWidth()) {
                                    var i = 0
                                    slopeModel.value?.comments?.let {
                                        it.forEach { comment ->
                                            if(comment.user != null && comment.message != null) {
                                                ListItem(
                                                    modifier = Modifier.padding(top = (72 * i).dp),
                                                    headlineContent = {
                                                        Text(
                                                            text = comment.user
                                                        )
                                                    }, supportingContent = {
                                                        Text(text = comment.message)
                                                    }
                                                )
                                                i++
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Box(
                            modifier = Modifier.run { fillMaxSize().padding(20.dp) },
                        ){
                            Row(modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = paddingValues.calculateBottomPadding())){
                                ExtendedFloatingActionButton(onClick = {
                                    showBottomSheet.value = true
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
                            if(showBottomSheet.value) {
                                ModalBottomSheet(onDismissRequest = { showBottomSheet.value = false }, sheetState = sheetState) {
                                    Column(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
                                    ) {
                                        if(showEditionForm) {
                                            EditionForm(slopeModel.value?.status, slopeModel.value?.state, sheetState, showBottomSheet, index)
                                        } else {
                                            CommentForm(index, slopeModel.value?.comments?.size ?: 0, sheetState, showBottomSheet)
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
    private fun EditionForm(status: Boolean? = false, state: SlopeModel.Companion.SlopeStateEnum? = SlopeModel.Companion.SlopeStateEnum.UNREPORTED, sheetState: SheetState, showBottomSheet: MutableState<Boolean>, index: Int) {
        val scope = rememberCoroutineScope()
        var dropdownExpanded by remember { mutableStateOf(false) }
        val tmpStatus = remember { mutableStateOf(status ?: false) }
        val tmpState = remember { mutableStateOf(state ?: SlopeModel.Companion.SlopeStateEnum.UNREPORTED) }
        Text("Edition form", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 20.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Status", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(end = 10.dp))
            Switch(checked = tmpStatus.value, onCheckedChange = {tmpStatus.value = it} )
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 40.dp)) {
            Text(text = "State", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(end = 10.dp))
            Box {
                TextField(value = tmpState.value.string, onValueChange = {}, readOnly = true, trailingIcon = {
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
                        DropdownMenuItem (text = { Text(text = it.string) }, onClick = { tmpState.value = it; dropdownExpanded = false})
                    }
                }
            }
        }
        Row {
            Button(onClick = {
                if(tmpStatus.value != status) {
                    SlopeDatabaseService().sendStatus(tmpStatus.value, index)
                }
                if(tmpState.value != state) {
                    SlopeDatabaseService().sendState(tmpState.value, index)
                }
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                } }, modifier = Modifier.padding(end = 10.dp)) {
                Text(text = "Edit")
            }
            OutlinedButton(onClick = { scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    showBottomSheet.value = false
                }
            } }) {
                Text(text = "Cancel")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CommentForm(index: Int, size: Int, sheetState: SheetState, showBottomSheet: MutableState<Boolean>) {
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }
        if(FirebaseAuth.getInstance().currentUser?.displayName?.isEmpty() == true) {
            Text("Just one last thing", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 20.dp))
            Text("To use this functionality, you must set a username. Please go to the profile page to set it up first")
            Button(onClick = {
                val intent = Intent(this@SlopeDetailsActivity, ProfileActivity::class.java)
                startActivity(intent)
            }) {
                Text(text = "Go to profile")
            }
        } else {
            Text("Add a comment", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 20.dp))
            TextField(value = text, onValueChange = {text = it}, minLines = 3, modifier = Modifier.fillMaxWidth())
            Row {
                Button(onClick = {
                    SlopeDatabaseService().sendComments(
                        FirebaseAuth.getInstance().currentUser?.displayName ?: "Unknown",
                        text,
                        index,
                        size
                    )
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet.value = false
                        }
                    } }, modifier = Modifier.padding(end = 10.dp)) {
                    Text(text = "Comment")
                }
                OutlinedButton(onClick = { scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                } }) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}