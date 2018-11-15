package com.team4.anamnesis.component

import android.content.Context
import android.graphics.PointF
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

const val MS_PER_INCH = 50f // number of ms to smooth scroll per inch

class LinearPageLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean):
        LinearLayoutManager(context, orientation, reverseLayout) {

    /**
     * The current position (i.e. page) of the RecyclerView.
     */
    var currentPosition: Int = 0
        private set(value) {
            field = value
        }

    /**
     * A listener that fires when the page changes.
     */
    var onPageChange: ((position: Int) -> Unit)? = null

    private val c: Context = context

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        super.smoothScrollToPosition(recyclerView, state, position)

        // create a SmoothScroller to handle the scrolling
        val scroller = object: LinearSmoothScroller(c) {

            // automatically implement on instantiation
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@LinearPageLayoutManager.computeScrollVectorForPosition(targetPosition)
            }

            // calculate ms required to scroll 1 px
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return MS_PER_INCH / (displayMetrics?.densityDpi ?: 1)
            }

        }

        scroller.targetPosition = position
        startSmoothScroll(scroller)
    }

    override fun onScrollStateChanged(state: Int) {

        // update current page
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            currentPosition = this.findFirstCompletelyVisibleItemPosition()
            onPageChange?.invoke(currentPosition)
        }

        super.onScrollStateChanged(state)
    }

}