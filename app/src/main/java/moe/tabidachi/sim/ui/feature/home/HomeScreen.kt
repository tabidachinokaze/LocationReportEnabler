package moe.tabidachi.sim.ui.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.SimCard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import moe.tabidachi.sim.R
import moe.tabidachi.sim.data.database.entity.Country
import moe.tabidachi.sim.data.database.entity.SimOperator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState = HomeState(),
    actions: HomeActions = HomeActions()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }, actions = {
                    IconButton(onClick = { actions.menuExpandedChange(true) }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                        DropdownMenu(
                            expanded = state.menuExpanded,
                            onDismissRequest = { actions.menuExpandedChange(false) }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = "源码")
                                }, onClick = {
                                    actions.aboutClick()
                                }, trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Code,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            )
        }, floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    actions.done()
                }, icon = {
                    Icon(imageVector = Icons.Rounded.Done, contentDescription = null)
                }, text = {
                    Text(text = "应用")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                tonalElevation = 1.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            actions.countryExpandedChange(true)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    FilledIconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Public,
                            contentDescription = null
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                            ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
                                Text(text = "国家/地区")
                            }
                        }
                        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                                Text(text = state.country?.countryName ?: "")
                                DropdownMenu(
                                    expanded = state.countryExpanded,
                                    onDismissRequest = { actions.countryExpandedChange(false) }
                                ) {
                                    state.favoriteCountry.forEach { country ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(text = country.countryName)
                                            }, onClick = {
                                                actions.countryExpandedChange(false)
                                                actions.setCountry(country)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                        IconButton(
                            onClick = actions.navigateToCountryManager,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                        }
                    }
                }
            }
            Surface(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                tonalElevation = 1.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            actions.operatorExpandedChange(true)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    FilledIconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SimCard,
                            contentDescription = null
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                            ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
                                Text(text = "运营商")
                            }
                        }
                        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                                Text(
                                    text = with(state.operator) {
                                        "${this?.mcc ?: ""}${this?.mnc ?: ""} ${this?.operator ?: ""}"
                                    }
                                )
                                DropdownMenu(
                                    expanded = state.operatorExpanded,
                                    onDismissRequest = { actions.operatorExpandedChange(false) }
                                ) {
                                    state.operators.forEach { operator ->
                                        val text = with(operator) {
                                            "${this.mcc}${this.mnc} ${this.operator ?: ""}"
                                        }
                                        DropdownMenuItem(
                                            text = {
                                                Text(text = text)
                                            }, onClick = {
                                                actions.operatorExpandedChange(false)
                                                actions.setOperator(operator)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                        IconButton(onClick = {
                            actions.dialogVisibleChange(true)
                        }, modifier = Modifier.padding(8.dp)) {
                            Icon(imageVector = Icons.Rounded.Info, contentDescription = null)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }

    if (state.dialogVisible) AlertDialog(
        onDismissRequest = {
            actions.dialogVisibleChange(false)
        }, confirmButton = {
            TextButton(onClick = {
                actions.dialogVisibleChange(false)
            }) {
                Text(text = "确定")
            }
        }, title = {
            Text(text = "运营商详情")
        }, text = {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                state.operator?.toLabelList()?.forEach {
                    OutlinedTextField(
                        value = it.second.toString(), onValueChange = {}, readOnly = true,
                        label = {
                            Text(text = it.first)
                        }, modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

fun SimOperator.toLabelList(): List<Pair<String, String?>> {
    return listOf(
        "Type" to type,
        "Country Name" to countryName,
        "Country Code" to countryCode,
        "mcc" to mcc,
        "mnc" to mnc,
        "Brand" to brand,
        "Operator" to operator,
        "Status" to status,
        "Bands" to bands,
        "Notes" to notes
    )
}

@Composable
@Preview(name = "Home")
private fun HomeScreenPreview() {
    HomeScreen(
        state = HomeState(
            operator = SimOperator(
                "National",
                "Abkhazia",
                "GE-AB",
                "289",
                "88",
                "A-Mobile",
                "A-Mobile LLSC",
                "Operational",
                "GSM 900 / GSM 1800 / UMTS 2100 / LTE 800 / LTE 1800",
                "MCC is not listed by ITU"
            ), country = Country("China", "CN")
        )
    )
}

