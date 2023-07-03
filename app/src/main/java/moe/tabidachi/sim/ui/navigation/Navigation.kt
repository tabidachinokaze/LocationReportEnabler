package moe.tabidachi.sim.ui.navigation

import androidx.navigation.NavHostController
import moe.tabidachi.sim.ui.navigation.Screens.COUNTRY_MANAGER_SCREEN
import moe.tabidachi.sim.ui.navigation.Screens.HOME_SCREEN

private object Screens {

    const val HOME_SCREEN = "home"
    const val COUNTRY_MANAGER_SCREEN = "country_manager"
}

object DestinationArgs

object Destinations {
    const val HOME_ROUTE = HOME_SCREEN
    const val COUNTRY_MANAGER_ROUTE = COUNTRY_MANAGER_SCREEN
}

class NavigationActions(
    private val navHostController: NavHostController
) {
    fun navigateUp() {
        navHostController.navigateUp()
    }

    fun navigateToHome() {
        navHostController.navigate(HOME_SCREEN)
    }

    fun navigateToCountryManager() {
        navHostController.navigate(COUNTRY_MANAGER_SCREEN)
    }
}