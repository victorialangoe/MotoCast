package com.example.motocast.ui.view.route_planner.add_destinations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.motocast.ui.viewmodel.address.Address

/**
 * A composable that displays a single address result
 *
 * @param address The address to display
 * @param onClick The callback to invoke when the address is clicked
 */
@Composable
fun AddressResult(
    address: Address,
    onClick: (address: Address) -> Unit
) {

    Button(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth(),
        onClick = { onClick(address) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
        )
    )
    {
        Column {

            Text(text = address.addressText, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = address.municipality ?: "",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(0.8f, fill = true)
                )

                Spacer(modifier = Modifier.width(8.dp))

                if (address.distanceFromUser != null) {
                    Text(
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.2f, fill = true),
                        text = if (address.distanceFromUser!! > 1000) {
                            "${address.distanceFromUser / 1000} km"
                        } else {
                            "${address.distanceFromUser} m"
                        },
                    )
                } else {
                    Text(text = "hei")
                }

            }
        }
    }
}