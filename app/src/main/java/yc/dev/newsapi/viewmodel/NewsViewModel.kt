package yc.dev.newsapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.data.repository.NewsRepository
import yc.dev.newsapi.ui.Constants
import yc.dev.newsapi.ui.state.UiState
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcher: CoroutineDispatcher,
    private val constants: Constants,
) : ViewModel() {

    private val _articlesState: MutableStateFlow<PagingData<Article>> = MutableStateFlow(value = PagingData.empty())
    val articlesState: StateFlow<PagingData<Article>> = _articlesState.asStateFlow()

    private var refreshJob: Job? = null

    private val _loadingState: MutableStateFlow<UiState?> = MutableStateFlow(value = null)
    val loadingState: StateFlow<UiState?> = _loadingState.asStateFlow()

    /**
     * TODO: Check the edge case of this event
     * [video link for reference](https://youtu.be/njchj9d_Lf8)
     */
    private val _refreshed: MutableSharedFlow<Unit> = MutableSharedFlow()
    val refreshed: SharedFlow<Unit?> = _refreshed.asSharedFlow()

    init {
        observeArticles()
        refresh()
    }

    private fun observeArticles() {
        viewModelScope.launch(dispatcher) {
            newsRepository.getArticles(constants.newsPageSize)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _articlesState.value = it
                }
        }
    }

    fun refresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch(dispatcher) {
            _loadingState.value = null
            newsRepository.loadNewsData(COUNTRY_US).collect {
                _loadingState.value = it

                if (it == UiState.Success) {
                    _refreshed.emit(Unit)
                }
            }
        }
    }

    companion object {
        private const val COUNTRY_US = "us"
    }
}