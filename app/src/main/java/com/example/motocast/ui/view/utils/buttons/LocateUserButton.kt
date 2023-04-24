package com.example.motocast.ui.view.utils.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.motocast.R

@Composable
fun LocateUserButton(
    modifier: Modifier = Modifier,
    active: Boolean,
    buttonSize: ButtonSize = ButtonSize.Small,
    buttonType: ButtonType = ButtonType.White,
    onClick: () -> Unit,
) {
    BasicButton(
        modifier = modifier,
        content = { _, contentSize, color, _ ->
            if (active) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_my_location_24),
                    contentDescription = stringResource(R.string.active_posstion_icon),
                    modifier = modifier
                        .size(contentSize),
                    tint = color,
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_location_searching_24),
                    contentDescription = stringResource(R.string.inactive_posstion_icon),
                    modifier = modifier
                        .size(contentSize),
                    tint = color,
                )
            }
        },
        onClick = onClick,
        circle = true,
        buttonSize = buttonSize,
        buttonType = buttonType,
    )
}