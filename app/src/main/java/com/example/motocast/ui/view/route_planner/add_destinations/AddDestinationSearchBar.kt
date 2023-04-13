package com.example.motocast.ui.view.route_planner.add_destinations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.address.Address
import com.example.motocast.util.BackButton

@Composable
fun AddDestinationSearchBar(
    query: String = "",
    onBack: () -> Unit,
    onClear: () -> Unit,
    onValueChange: (String, Address) -> Unit,
) {
    // We use this to request focus on the text field when the screen is loaded
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BackButton(onClick = onBack)

        Spacer(modifier = Modifier.width(20.dp))

        OutlinedTextField(
            value = query,
            onValueChange = {
                val newAddress = Address(
                    addressText = it,
                    municipality = null,
                    latitude = null,
                    longitude = null
                )
                onValueChange(it, newAddress)
            },
            label = { Text("Søk etter adresse") },
            modifier = Modifier
                .weight(0.8f, fill = true)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                cursorColor = Color.Black,
                textColor = Color.Black,
            ),
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                    contentDescription = "Søk etter adresse icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (query != "") {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_close_24),
                        contentDescription = "Fjerne søk etter adresse icon",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onClear() }
                    )
                }
            }
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}