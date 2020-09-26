package uz.mymax.savvyenglish.utils.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import uz.mymax.savvyenglish.utils.px

class LinearVerticalDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

        if (itemPosition == 0) {
            with(outRect) {
                right = 0.px
                left = 0.px
                top = 16.px
                bottom = 8.px
            }
        } else {
            with(outRect) {
                left = 0.px
                right = 0.px
                top = 8.px
                bottom = 8.px
            }
        }

    }
}

