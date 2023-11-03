package yc.dev.newsapi.ui.route

sealed class Route(val route: String) {
    object Home: Route("homeScreen")
    object Xml: Route("xmlScreen")
    object Compose: Route("composeScreen")
}