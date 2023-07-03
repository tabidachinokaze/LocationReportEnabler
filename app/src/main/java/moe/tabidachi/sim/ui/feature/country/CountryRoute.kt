package moe.tabidachi.sim.ui.feature.country

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import moe.tabidachi.sim.ui.navigation.NavigationActions

@Composable
fun CountryRoute(
    coordinator: CountryCoordinator = rememberCountryCoordinator(),
    navigationActions: NavigationActions
) {
    // State observing and declarations
    val uiState by coordinator.screenStateFlow.collectAsStateWithLifecycle(CountryState())

    // UI Actions
    val actions = rememberCountryActions(coordinator, navigationActions)

    // UI Rendering
    CountryScreen(uiState, actions)
}

@Composable
fun rememberCountryActions(coordinator: CountryCoordinator, navigationActions: NavigationActions): CountryActions {
    return remember(coordinator) {
        CountryActions(
            onClick = coordinator::doStuff,
            onFilterChange = coordinator.viewModel::onFilterChange,
            onFilterClear = coordinator.viewModel::onFilterClear,
            navigateUp = navigationActions::navigateUp,
            addFavorite = coordinator.viewModel::addCountry,
            removeFavorite = coordinator.viewModel::removeCountry
        )
    }
}