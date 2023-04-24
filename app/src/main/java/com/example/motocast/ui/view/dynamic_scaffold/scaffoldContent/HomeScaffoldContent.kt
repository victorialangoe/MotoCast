package com.example.motocast.ui.view.dynamic_scaffold.scaffoldContent

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.motocast.R
import java.util.Calendar

@Composable
fun HomeScaffoldContent(
    userName: String,
) {
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "${stringResource(R.string.good_morning)} $userName"
        in 12..17 -> "${stringResource(R.string.good_afternoon)} $userName"
        else -> "${stringResource(R.string.good_evening)} $userName"
    }

    Text(
        text = greeting,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}