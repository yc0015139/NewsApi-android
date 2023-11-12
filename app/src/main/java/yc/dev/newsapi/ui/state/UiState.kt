package yc.dev.newsapi.ui.state

sealed interface UiState {
    object Success: UiState
    object Error: UiState
}