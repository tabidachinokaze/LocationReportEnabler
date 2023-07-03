package moe.tabidachi.sim.ui.feature.country

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import moe.tabidachi.sim.data.database.entity.Country

/**
 * UI State that represents CountryScreen
 **/
data class CountryState(
    val countries: List<CountryItem> = emptyList(),
    val isTextEmpty: Boolean = true,
    val filter: String = "",
    val country: List<Country> = emptyList()
)

data class CountryItem(
    val countryName: String,
    val countryCode: String,
)

/**
 * Country Actions emitted from the UI Layer
 * passed to the coordinator to handle
 **/
data class CountryActions(
    val onClick: () -> Unit = {},
    val onFilterChange: (String) -> Unit = {},
    val onFilterClear: () -> Unit = {},
    val navigateUp: () -> Unit = {},
    val addFavorite: (CountryItem) -> Unit = {},
    val removeFavorite: (CountryItem) -> Unit = {},
)

/**
 * Compose Utility to retrieve actions from nested components
 **/
val LocalCountryActions = staticCompositionLocalOf<CountryActions> {
    error("{NAME} Actions Were not provided, make sure ProvideCountryActions is called")
}

@Composable
fun ProvideCountryActions(actions: CountryActions, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalCountryActions provides actions) {
        content.invoke()
    }
}

