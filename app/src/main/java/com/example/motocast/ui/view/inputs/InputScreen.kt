package com.example.motocast.ui.view.inputs


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.R
import com.example.motocast.ui.view.inputs.buttons.AddFieldButton
import com.example.motocast.ui.view.inputs.buttons.ClearAllButton
import com.example.motocast.ui.view.inputs.buttons.CreateRouteButton
import com.example.motocast.ui.viewmodel.inputs.InputViewModel

@Composable
fun InputScreen(
    inputViewModel: InputViewModel,
    onNavigateToScreen: () -> Unit
) {

    val inputUiState by inputViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Header()

        Spacer(modifier = Modifier.height(20.dp))

        InputFields(inputUiState.numberOfFields)

        Spacer(modifier = Modifier.height(20.dp))

        AddFieldButton(inputViewModel)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ){
            TimePicker()
            Spacer(modifier = Modifier.width(20.dp))
            DatePicker()
        }

        Spacer(modifier = Modifier.height(20.dp))

        CreateRouteButton(onNavigateToScreen)

        Spacer(modifier = Modifier.height(20.dp))

        ClearAllButton(inputViewModel)

    }

}

@Composable
fun Header() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow),
            contentDescription = "Go to previous screen button",
            modifier = Modifier.weight(0.1f, fill = true)
        )
        Text(
            fontSize = 25.sp,
            text = "Lag rute",
            modifier = Modifier.weight(0.8f, fill = true),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(0.1f, fill = true))
    }
}









