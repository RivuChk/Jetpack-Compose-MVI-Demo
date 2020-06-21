package dev.rivu.mvijetpackcomposedemo.moviesearch.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.ContextAmbient
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.graphics.imageFromResource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.lang.UnsupportedOperationException

@Composable
fun loadPicture(url: String, @DrawableRes placeholderRes: Int, @DrawableRes errorImageRes: Int = placeholderRes): ImageState {

    val placeHolderImage = imageFromResource(ContextAmbient.current.resources, placeholderRes)
    val errorImage = if (errorImageRes == placeholderRes) placeHolderImage else imageFromResource(ContextAmbient.current.resources, errorImageRes)

    var bitmapState: ImageState by state<ImageState> { ImageState.Loading(placeHolderImage) }

    Glide.with(ContextAmbient.current)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState = ImageState.Success(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })

    return bitmapState
}

sealed class ImageState {
    abstract val image: ImageAsset

    data class Loading(override val image: ImageAsset) : ImageState()

    data class Success(val data: Bitmap) : ImageState() {
        override val image: ImageAsset = data.asImageAsset()
    }

    data class Error(override val image: ImageAsset, val exception: Exception) : ImageState()
}
