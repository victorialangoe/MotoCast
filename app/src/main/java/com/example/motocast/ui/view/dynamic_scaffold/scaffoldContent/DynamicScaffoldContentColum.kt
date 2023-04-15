package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R

@Composable
fun DynamicScaffoldContentColum(
    modifier: Modifier = Modifier,
    routeScaffoldButtonOnClick: () -> Unit,
    homeScaffoldButtonOnClick: () -> Unit,
    routeText: String,
    showRoute: Boolean,
    date: String,
    time: String,
    duration: String,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.scaffold_dragbar),
            contentDescription = "Bar to drag scaffold up",
        )
        Spacer (modifier = Modifier.height(8.dp))

        if (showRoute) RouteScaffoldContent(
            onButtonClick = routeScaffoldButtonOnClick,
            routeText = routeText,
            date = date,
            time = time,
            duration = duration
        )
        else HomeScaffoldContent(onButtonClick = homeScaffoldButtonOnClick)

    }
}