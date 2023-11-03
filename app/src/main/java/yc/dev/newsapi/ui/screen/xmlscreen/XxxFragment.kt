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
import yc.dev.newsapi.databinding.FragmentXxxBinding

@AndroidEntryPoint
class XxxFragment : Fragment(R.layout.fragment_xxx) {
    private val binding by viewBinding(FragmentXxxBinding::bind)

    companion object {
        fun newInstance() = XxxFragment()
    }
}

@Composable
fun XxxFragmentScreen(fm: FragmentManager) {
    AndroidViewBinding(FragmentContainerBinding::inflate) {
        fm.commit {
            replace(fragmentContainerView.id, XxxFragment.newInstance())
        }
    }
}