package com.example.motocast.ui.view

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.motocast.R

@Composable
fun WordAnimation() {
    val context = LocalContext.current
    val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.motocast}")
    AndroidView(factory = { context ->
        VideoView(context).apply {
            setVideoURI(videoUri)
            start()
        }
    }, modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .height(700.dp)
        .clip(RoundedCornerShape(1.dp))
        .scale(scale = 1.2f)
    )
}


