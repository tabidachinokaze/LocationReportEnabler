package moe.tabidachi.sim.ui.feature.country

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryScreen(
    state: CountryState = CountryState(),
    actions: CountryActions = CountryActions()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "国家/地区")
                }, navigationIcon = {
                    IconButton(onClick = actions.navigateUp) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            BasicTextField(
                value = state.filter,
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                onValueChange = actions.onFilterChange, decorationBox = { textField ->
                    Surface(shape = RoundedCornerShape(28.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .then(
                                        if (state.isTextEmpty) Modifier.padding(
                                            horizontal = 16.dp, vertical = 16.dp
                                        ) else Modifier.padding(
                                            start = 16.dp,
                                            top = 16.dp,
                                            end = 0.dp,
                                            bottom = 16.dp
                                        )
                                    )
                                    .weight(1f)
                                    .background(color = MaterialTheme.colorScheme.surface)
                            ) {
                                textField()
                                if (state.isTextEmpty) {
                                    Text(text = "搜索")
                                }
                            }
                            if (!state.isTextEmpty) {
                                IconButton(onClick = actions.onFilterClear) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }, singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )
            Divider()
            LazyColumn() {
                items(state.countries) { item ->
                    val booleanState = state.country.any { it.countryName == item.countryName && it.countryCode == item.countryCode }
                    Surface(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 0.5.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { }
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
//                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = item.countryName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                                    ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
                                        Text(text = item.countryCode)
                                    }
                                }
                            }
                            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                                if (booleanState) {
                                    IconButton(onClick = { actions.removeFavorite(item) }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { actions.addFavorite(item) }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Add,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.navigationBarsPadding().imePadding())
                }
            }
        }
    }
}

@Composable
@Preview(name = "Country")
private fun CountryScreenPreview() {
    CountryScreen(
        state = CountryState(
            countries = listOf(
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
                CountryItem("China", "CN"),
            )
        )
    )
}

