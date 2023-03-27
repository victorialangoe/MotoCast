package com.example.motocast.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.motocast.data.datasource.MetAlertsDataSource
import com.example.motocast.data.model.MetAlertsDataModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetAlertsScreen(viewModel: MetAlertsDataSource) {
    var metAlertsData by remember { mutableStateOf<MetAlertsDataModel?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getMetAlertData(
            onSuccess = { data -> metAlertsData = data },
            onError = { errorMessage -> error = errorMessage }
        )
    }

    if (error != null) {
        Text(text = "Error: $error")
    } else if (metAlertsData != null) {
        LazyColumn {
            items(metAlertsData!!.features.size) { index ->
                val feature = metAlertsData!!.features[index]
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = feature.properties.title, fontWeight = FontWeight.Bold)
                        Text(text = feature.properties.description)
                    }
                }
            }
        }


    } else {
        CircularProgressIndicator()
    }
}
