package yc.dev.newsapi.ui.screen.xmlscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import yc.dev.newsapi.R
import yc.dev.newsapi.databinding.FragmentContainerBinding
import yc.dev.newsapi.databinding.FragmentNewsBinding

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {
    private val binding by viewBinding(FragmentNewsBinding::bind)

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