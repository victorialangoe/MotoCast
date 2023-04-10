package com.example.motocast.ui.view.inputs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.R

@Composable
fun InputFields(numberOfFields: Int) {

    LazyColumn {
        item{
            inputTextField(header = "Fra",labelText = "Reise fra", icon = R.drawable.line_end_arrow_rounded, description = "Arrow icon")
        }
        items(numberOfFields) {
            inputTextField(header = "Via", labelText = "Reise via", icon = R.drawable.format_line_spacing_rounded, description = "Reorder icon")
        }
        item{
            inputTextField(header = "Til",labelText = "Reise til", icon = R.drawable.mdi_goal, description = "Goal icon")
        }

    }

}

@Composable
fun inputTextField(header: String, labelText: String, icon: Int, description: String) : String {
    var inputFromUser by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.width(340.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = header,
            fontSize = 20.sp,
            modifier = Modifier.weight(0.12f, fill = true)
        )
        OutlinedTextField(
            value = inputFromUser,
            onValueChange = { inputFromUser = it },
            leadingIcon = { Icon(imageVector = ImageVector.vectorResource(id = icon), contentDescription = description) },
            label = { Text(text = labelText) },
            modifier = Modifier.weight(0.88f, fill = true)
        )
    }
    return inputFromUser
}