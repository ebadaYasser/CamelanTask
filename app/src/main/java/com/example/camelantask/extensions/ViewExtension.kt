package com.example.camelantask.extensions

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.onVisible(onVisible: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener {
        if (visibility == View.VISIBLE) {
            onVisible.invoke()
        }
    }
}

fun View.setPaddingTopDimen(dimenResId: Int) {
    val paddingTop = resources.getDimensionPixelOffset(dimenResId)
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}