package yc.dev.newsapi.utils.compose

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


/**
 * Add the [Composable] to the [NavGraphBuilder] with lifecycle resumed check
 *
 * Inspired by this [comment](https://github.com/google/accompanist/issues/1320#issuecomment-1238539724)
 *
 * Warning: If your composable is the startDestination, do NOT set it to [safeComposable]. Instead, just use [composable], or your composable will not be displayed.
 *
 * @see [androidx.navigation.compose.composable]
 *
 * @param route route for the destination
 * @param arguments list of arguments to associate with destination
 * @param deepLinks list of deep links to associate with the destinations
 * @param enterTransition callback to determine the destination's enter transition
 * @param exitTransition callback to determine the destination's exit transition
 * @param popEnterTransition callback to determine the destination's popEnter transition
 * @param popExitTransition callback to determine the destination's popExit transition
 * @param content composable for the destination
 */
fun NavGraphBuilder.safeComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =
        enterTransition,
    popExitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =
        exitTransition,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(route, arguments, deepLinks, enterTransition, exitTransition, popEnterTransition, popExitTransition) {
        if (it.lifecycleIsResumed()) {
            content(this, it)
        }
    }
}

fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
