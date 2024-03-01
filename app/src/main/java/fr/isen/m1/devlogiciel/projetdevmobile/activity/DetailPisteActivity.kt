package fr.isen.m1.devlogiciel.projetdevmobile.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfallPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
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
import fr.isen.m1.devlogiciel.projetdevmobile.model.CommentModel
import fr.isen.m1.devlogiciel.projetdevmobile.model.PisteModel
import kotlinx.coroutines.launch

class DetailPisteActivity: AppCompatActivity() {
   // private var piste = intent.getSerializableExtra("piste") as? PisteModel
    private val piste = PisteModel("PisteTest", PisteModel.Companion.PisteColorEnum.BLUE, PisteModel.Companion.PisteStateEnum.UNREPORTED, true)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                LazyColumn(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()) {
                    item(piste) {
                        Box(Modifier.fillMaxWidth()) {
                            Text(piste?.name ?: "No name", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.titleMedium)
                        }
                        Row {
                            SuggestionChip(
                                label = { Text(piste?.color?.string.toString()) },
                                onClick = { /*TODO*/ },
                                modifier = Modifier.padding(10.dp)
                            )
                            val open = if (piste?.status == true) "Open" else "Closed"

                            SuggestionChip(
                                label = { Text(open) },
                                onClick = { /*TODO*/ },
                                modifier = Modifier.padding(10.dp),
                                colors = ChipColors(
                                    containerColor = if (piste?.status == true) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                                    labelColor = if (piste?.status == true) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                                    leadingIconContentColor = MaterialTheme.colorScheme.onSurface,
                                    trailingIconContentColor = MaterialTheme.colorScheme.onSurface,
                                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                    disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                    disabledLeadingIconContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                    disabledTrailingIconContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                )

                            )
                            SuggestionChip(
                                label = { Text(piste?.state?.string.toString()) },
                                onClick = { /*TODO*/ },
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                    }
                }
                val comments: List<CommentModel> = ArrayList()
                val tooltipState = remember { TooltipState() }
                val scope = rememberCoroutineScope()
                LazyColumn(modifier = Modifier.waterfallPadding()) {
                    item(piste) {
                        Box(Modifier.fillMaxWidth()) {
                            Text("Comments", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    itemsIndexed(comments) {_, it ->
                        ListItem(headlineContent = { Text(text = it.user ?: "Unknown") }, supportingContent = {
                            Text(text = it.comment ?: "No comment")
                        })
                    }
                }
                Box(
                    modifier = Modifier.run { fillMaxSize().padding(20.dp) },
                ){
                    Row(modifier = Modifier.align(Alignment.BottomEnd)){
                        ExtendedFloatingActionButton(onClick = {
                            if (!tooltipState.isVisible) {
                                scope.launch {tooltipState.show()}
                            }
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
                    var text by remember { mutableStateOf("") }
                    TooltipBox(positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                        tooltip = {
                            RichTooltip(
                                title = {
                                    Text(text = "Add a comment")
                                },
                                text = {
                                    TextField(value = text, onValueChange = {text = it})
                                },
                                action = {
                                    Row {
                                        TextButton(onClick = {
                                        }) {
                                            Text(text = "Add")
                                        }
                                        TextButton(onClick = { scope.launch {tooltipState.dismiss()} }) {
                                            Text(text = "Cancel")
                                        }
                                    }
                                }
                            )
                        }, state = tooltipState) {
                    }
                }
            }
        }
    }
}