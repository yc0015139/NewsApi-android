package yc.dev.newsapi.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadFromWeb(imageUrl: String) {
    Glide.with(this@loadFromWeb)
        .load(imageUrl)
        .centerCrop()
        .placeholder(android.R.color.darker_gray)
        .into(this@loadFromWeb)
}