package moe.tabidachi.sim.ui.feature.home

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import moe.tabidachi.sim.data.database.entity.Country
import moe.tabidachi.sim.data.database.entity.SimOperator

/**
 * UI State that represents HomeScreen
 **/
data class HomeState(
    val operator: SimOperator? = null,
    val country: Country? = null,
    val favoriteCountry: List<Country> = emptyList(),
    val operators: List<SimOperator> = emptyList(),
    val countryExpanded: Boolean = false,
    val operatorExpanded: Boolean = false,
    val dialogVisible: Boolean = false,
    val aboutDialogVisible: Boolean = false,
    val menuExpanded: Boolean = false,
)

/**
 * Home Actions emitted from the UI Layer
 * passed to the coordinator to handle
 **/
data class HomeActions(
    val navigateUp: () -> Unit = {},
    val onClick: () -> Unit = {},
    val navigateToCountryManager: () -> Unit = {},
    val countryExpandedChange: (Boolean) -> Unit = {},
    val operatorExpandedChange: (Boolean) -> Unit = {},
    val dialogVisibleChange: (Boolean) -> Unit = {},
    val aboutDialogVisibleChange: (Boolean) -> Unit = {},
    val setCountry: (Country) -> Unit = {},
    val setOperator: (SimOperator) -> Unit = {},
    val done: () -> Unit = {},
    val menuExpandedChange: (Boolean) -> Unit = {},
    val aboutClick: (Context) -> Unit = {}
)

/**
 * Compose Utility to retrieve actions from nested components
 **/
val LocalHomeActions = staticCompositionLocalOf<HomeActions> {
    error("{NAME} Actions Were not provided, make sure ProvideHomeActions is called")
}

@Composable
fun ProvideHomeActions(actions: HomeActions, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalHomeActions provides actions) {
        content.invoke()
    }
}

