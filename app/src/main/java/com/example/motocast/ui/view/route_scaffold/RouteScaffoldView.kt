package com.example.motocast.ui.view.route_scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.dynamicScaffold.cards.CardsColumn

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RouteScaffoldView(
    content: @Composable (Modifier) -> Unit = { modifier ->
        Box(modifier) {
            Text("Scaffold Content")
        }
    },
    onNavigateToScreen: () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val contextForToast = LocalContext.current.applicationContext

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = minHeight,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight, max = maxHeight)
                    .clip(cornerShape),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                ContentColumn(onNavigateToScreen)
            }
        },
        content = {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red)){
                content(Modifier)
            }
        }
    )
}

@Composable
fun ContentColumn(onNavigateToScreen: () -> Unit){
    Column(
        modifier = Modifier
            .clip(cornerShape)
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
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

        RouteText()

        DateAndTimeRow()

        EditRouteButton(onNavigateToScreen)

        CardsColumn()
    }
}

@Composable
fun DateAndTimeRow(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        TimeBadge()
        Spacer(modifier = Modifier.width(50.dp))
        DateBadge()
    }
}

// Constants
private val cornerShape = RoundedCornerShape(16.dp)
private val maxHeight = 500.dp
private val minHeight = 190.dp

@Preview
@Composable
fun RouteScaffoldViewPreview(){
    RouteScaffoldView() {}
}