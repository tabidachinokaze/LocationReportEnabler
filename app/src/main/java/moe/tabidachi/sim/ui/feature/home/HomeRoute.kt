package moe.tabidachi.sim.ui.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import moe.tabidachi.sim.ui.navigation.NavigationActions

@Composable
fun HomeRoute(
    coordinator: HomeCoordinator = rememberHomeCoordinator(),
    navigationActions: NavigationActions
) {
    // State observing and declarations
    val uiState by coordinator.screenStateFlow.collectAsStateWithLifecycle()

    // UI Actions
    val actions = rememberHomeActions(coordinator, navigationActions)

    // UI Rendering
    HomeScreen(uiState, actions)
}


@Composable
fun rememberHomeActions(coordinator: HomeCoordinator, navigationActions: NavigationActions): HomeActions {
    return remember(coordinator, navigationActions) {
        HomeActions(
            navigateUp = navigationActions::navigateUp,
            onClick = coordinator::doStuff,
            navigateToCountryManager = navigationActions::navigateToCountryManager,
            countryExpandedChange = coordinator.viewModel::countryExpandedChange,
            operatorExpandedChange = coordinator.viewModel::operatorExpandedChange,
            dialogVisibleChange = coordinator.viewModel::dialogVisibleChange,
            setCountry = coordinator.viewModel::changeCountry,
            setOperator = coordinator.viewModel::setOperator,
            done = coordinator.viewModel::done,
            menuExpandedChange = coordinator.viewModel::menuExpandedChange,
            aboutDialogVisibleChange = coordinator.viewModel::aboutDialogVisibleChange,
            aboutClick = coordinator.viewModel::aboutClick
        )
    }
}