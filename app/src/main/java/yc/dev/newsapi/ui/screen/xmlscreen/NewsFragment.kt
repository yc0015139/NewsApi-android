package yc.dev.newsapi.ui.screen.xmlscreen

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import yc.dev.newsapi.R
import yc.dev.newsapi.databinding.FragmentContainerBinding
import yc.dev.newsapi.databinding.FragmentNewsBinding
import yc.dev.newsapi.ui.state.UiState
import yc.dev.newsapi.utils.addTopSpacingForFirstItem
import yc.dev.newsapi.utils.launchWhenStarted
import yc.dev.newsapi.viewmodel.NewsViewModel

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {
    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val viewModel: NewsViewModel by viewModels()

    private val pagingAdapter = NewsPagingDataAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeData()
    }

    private fun setupViews() {
        setupRefresh()
        setupRecyclerView()
    }

    private fun setupRefresh() {
        binding.splNews.setOnRefreshListener {
            viewModel.refresh()
        }

        launchWhenStarted {
            viewModel.refreshed.collect {
                pagingAdapter.refresh()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvNews.addTopSpacingForFirstItem(dp = 16)
        binding.rvNews.adapter = pagingAdapter
    }

    private fun observeData() {
        observeLoadingState()
        observeArticlesState()
    }

    private fun observeLoadingState() {
        launchWhenStarted {
            viewModel.loadingState.collect {
                binding.splNews.isRefreshing = it == null
                makeNotification(it)
            }
        }
    }

    private fun makeNotification(uiState: UiState?) {
        if (uiState != UiState.Error) return

        Snackbar.make(binding.coordinatorLayout, "OOPS! Something wrong when loading :(", Snackbar.LENGTH_LONG).show()
    }

    private fun observeArticlesState() {
        launchWhenStarted {
            viewModel.articlesState.collect {
                pagingAdapter.submitData(it)
            }
        }
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}

@Composable
fun NewsFragmentScreen(fm: FragmentManager) {
    AndroidViewBinding(FragmentContainerBinding::inflate) {
        fm.commit {
            replace(fragmentContainerView.id, NewsFragment.newInstance())
        }
    }
}