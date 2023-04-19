package com.example.motocast.util.views.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.motocast.R

@Composable
fun LocateUserButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    buttonSize: ButtonSize = ButtonSize.Small,
    buttonType: ButtonType = ButtonType.Transparent,
    onClick: () -> Unit,
) {
    BasicButton(
        modifier = modifier,
        content = { _, contentSize ->
            if (active) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_my_location_24),
                    contentDescription = "Aktiv posisjon sporing icon",
                    modifier = modifier
                        .size(contentSize)
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_location_searching_24),
                    contentDescription = "Inaktiv posisjon sporing icon",
                    modifier = modifier
                        .size(contentSize)
                )
            }
        },
        onClick = onClick,
        circle = true,
        buttonSize = buttonSize,
        buttonType = buttonType,
    )
}