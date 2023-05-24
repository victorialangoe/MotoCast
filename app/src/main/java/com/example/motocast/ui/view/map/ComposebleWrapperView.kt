package com.example.motocast.ui.view.map

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.ui.platform.ComposeView
import java.util.*

/**
 * A custom [FrameLayout] that displays a weather card with specified data using Jetpack Compose.
 * This view acts as a wrapper to bridge traditional Android views and Compose.
 * Needed because Mapox SDK requires a View to be passed in for ViewAnnotations, which are used for displaying weather cards.
 *
 * @param context The [Context] the view is running in, through which it can access the current theme, resources, etc.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view.
 * Can be 0 to not look for defaults.
 * @param temperature The current temperature to be displayed on the weather card.
 * @param time The current time to be displayed on the weather card.
 * @param iconSymbol The symbol that represents the current weather condition to be displayed on the weather card.
 *
 * Once this view is initialized, it creates a [ComposeView] which displays a [WeatherCard] with the given data.
 */
@SuppressLint("ViewConstructor")
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

        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
    }

}