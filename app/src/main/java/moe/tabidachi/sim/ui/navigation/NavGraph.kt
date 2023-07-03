package moe.tabidachi.sim.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import moe.tabidachi.sim.ui.feature.country.CountryRoute
import moe.tabidachi.sim.ui.feature.home.HomeRoute

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberAnimatedNavController(),
    startDestination: String = Destinations.HOME_ROUTE,
    navigationActions: NavigationActions = remember(navHostController) {
        NavigationActions(navHostController)
    }
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                    scaleIn(
                        initialScale = 0.92f,
                        animationSpec = tween(220, delayMillis = 90)
                    )
        }, exitTransition = {
            fadeOut(animationSpec = tween(90))
        }, popEnterTransition = {
            fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                    scaleIn(
                        initialScale = 0.92f,
                        animationSpec = tween(220, delayMillis = 90)
                    )
        }, popExitTransition = {
            fadeOut(animationSpec = tween(90))
        }
    ) {
        composable(Destinations.HOME_ROUTE) {
            HomeRoute(navigationActions = navigationActions)
        }
        composable(Destinations.COUNTRY_MANAGER_ROUTE) {
            CountryRoute(navigationActions = navigationActions)
        }
    }
}