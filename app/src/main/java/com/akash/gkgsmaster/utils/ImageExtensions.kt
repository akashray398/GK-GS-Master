package com.akash.gkgsmaster.utils

import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.akash.gkgsmaster.R

fun ImageView.loadCircleImage(url: String?) {
    load(url) {
        crossfade(true)
        placeholder(R.drawable.ic_placeholder)
        error(R.drawable.ic_error)
        transformations(CircleCropTransformation())
    }
}
