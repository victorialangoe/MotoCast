package com.example.motocast.ui.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.R


@Composable
fun WordOne(offset: Offset) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Moto",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center).offset(offset.x.dp, offset.y.dp)
        )
    }
}

@Composable
fun WordTwo(offset: Offset) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Cast",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center).offset(offset.x.dp, offset.y.dp)
        )
    }
}


@Composable
fun WordAnimation() {
    val offsetXWordOne = remember { Animatable(-200f) }
    val offsetXWordTwo = remember { Animatable(700f) }
    val offsetYBothWords = remember { Animatable(0f) }
    val imagePainter = painterResource(R.drawable.logo)
    val backgroundColor = colorResource(id = R.color.background_color)

    LaunchedEffect(Unit) {
        offsetXWordOne.animateTo(-40f, animationSpec = TweenSpec(durationMillis = 1000))
        offsetXWordTwo.animateTo(50f, animationSpec = TweenSpec(durationMillis = 1000))
        offsetYBothWords.animateTo(-1000f, animationSpec = TweenSpec(durationMillis = 1000))
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {
        WordOne(Offset(offsetXWordOne.value, offsetYBothWords.value))
        WordTwo(Offset(offsetXWordTwo.value, offsetYBothWords.value))

        Image(
            painter = imagePainter,
            contentDescription = "Your Image",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = offsetYBothWords.value.dp - 200.dp)
                .size(100.dp),
            contentScale = ContentScale.Crop
        )

    }

}