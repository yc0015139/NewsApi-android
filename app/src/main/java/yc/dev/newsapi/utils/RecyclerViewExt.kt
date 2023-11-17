package yc.dev.newsapi.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addTopSpacingForFirstItem(dp: Int) {
    val inPx = this@addTopSpacingForFirstItem.context.dpToPx(dp)
    this.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = inPx
            }
        }
    })
}