package com.example.motocast.ui.view.map

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.ui.platform.ComposeView
import java.util.*

class ComposableWrapperView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val temperature: Int,
    private val time: Calendar?,
    private val iconSymbol: String
) : FrameLayout(context, attrs, defStyleAttr) {

    init {

        val composeView = ComposeView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            setContent {
                WeatherCard(
                    temperature = temperature,
                    time = time,
                    iconSymbol = iconSymbol,
                    context = context
                )
            }
        }
        addView(composeView)

        // Set the layoutParams for the ComposableWrapperView itself
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
    }

}