package com.example.motocast.ui.view.route_planner.add_destinations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.address.Address
import com.example.motocast.util.buttons.BackButton

@Composable
fun AddDestinationSearchBar(
    query: String = "",
    onBack: () -> Unit,
    onClear: () -> Unit,
    onSetCurrentLocation: () -> Unit,
    onValueChange: (String, Address) -> Unit,
) {
    // We use this to request focus on the text field when the screen is loaded
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceTint)
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
            modifier = Modifier
                .weight(0.8f, fill = true)
                .focusRequester(focusRequester),
            shape = MaterialTheme.shapes.extraLarge,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                textColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,

            )
            ,
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                    contentDescription = "Søk etter adresse icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (query != "") {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_close_24),
                        contentDescription = "Fjerne søk etter adresse icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onClear() }
                    )
                } else {
                    // TODO: Implement search icon
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_location_searching_24),
                        contentDescription = "Søk etter adresse icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onSetCurrentLocation() }
                    )
                }
            }
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}