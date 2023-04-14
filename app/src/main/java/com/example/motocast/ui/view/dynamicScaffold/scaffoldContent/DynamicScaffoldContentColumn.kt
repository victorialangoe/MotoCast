package com.example.motocast.ui.view.dynamicScaffold.scaffoldContent

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerUiState
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel

@Composable
fun DynamicScaffoldContentColumn(
    ScaffoldContent: @Composable () -> Unit,
)
{
    Column(
        modifier = Modifier
            .clip(cornerShape)
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.scaffold_dragbar),
            contentDescription = "Bar to drag scaffold up",
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .align(Alignment.CenterHorizontally)
        )

        ScaffoldContent()
    }
}

//Constants
val cornerShape = RoundedCornerShape(16.dp)