package com.example.motocast.ui.view.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
import com.example.motocast.ui.view.utils.components.Header

@Composable
fun SettingsView(
    popBackStack: () -> Unit,
    setDarkMode: (Boolean) -> Unit,
    setUserName: (String) -> Unit,
    userName: String,
    darkMode: Boolean,
) {

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background
            )
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Header(onClick = { popBackStack() }, text = stringResource(R.string.settings))

        Spacer(modifier = Modifier.padding(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            ChooseScreenMode(darkMode = darkMode, setDarkMode = setDarkMode)
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.CenterStart
        ) {
            ChooseUserName(userName = userName, setUserName = setUserName)
        }
    }
}

