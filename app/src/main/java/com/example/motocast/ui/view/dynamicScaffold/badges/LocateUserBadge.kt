package com.example.motocast.ui.view.dynamicScaffold.badges

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.motocast.R


@Composable
fun LocateUserBadge(
    onLocateUserClick: () -> Unit,
    active: Boolean,
) {

    Button(
        onClick = {
            onLocateUserClick()
        },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .padding(4.dp)
            .size(50.dp),
    ) {

        if (active) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_my_location_24),
                contentDescription = "Aktiv posisjon sporing icon",
                tint = Color.Black,
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_location_searching_24),
                contentDescription = "Inaktiv posisjon sporing icon",
                tint = Color.Black,
            )
        }

    }
}


@Preview
@Composable
fun LocateUserBadgePreview() {
    LocateUserBadge(
        onLocateUserClick = {},
        active = true
    )
}
