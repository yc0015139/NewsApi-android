package yc.dev.newsapi.ui.screen.xmlscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import yc.dev.newsapi.data.model.Article
import yc.dev.newsapi.databinding.HolderNewsBinding
import yc.dev.newsapi.utils.loadFromWeb

class NewsPagingDataAdapter : PagingDataAdapter<Article, NewsPagingHolder>(diffCallback = articleComparator) {
    override fun onBindViewHolder(holder: NewsPagingHolder, position: Int) {
        val article = getItem(position)
        article?.let { holder.onBind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsPagingHolder = NewsPagingHolder(
        binding = HolderNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    companion object {
        private val articleComparator = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem

        }
    }
}

class NewsPagingHolder(
    private val binding: HolderNewsBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(article: Article) {
        article.apply {
            binding.ivArticle.loadFromWeb(urlToImage)
            binding.tvTitle.text = title
        }
    }
}