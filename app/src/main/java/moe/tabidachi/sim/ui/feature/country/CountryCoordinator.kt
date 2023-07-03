package moe.tabidachi.sim.ui.feature.country

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Screen's coordinator which is responsible for handling actions from the UI layer
 * and one-shot actions based on the new UI state
 */
class CountryCoordinator(
    val viewModel: CountryViewModel
) {
    val screenStateFlow = viewModel.stateFlow

    fun doStuff() {
        // TODO Handle UI Action
    }
}

@Composable
fun rememberCountryCoordinator(
    viewModel: CountryViewModel = hiltViewModel()
): CountryCoordinator {
    return remember(viewModel) {
        CountryCoordinator(
            viewModel = viewModel
        )
    }
}