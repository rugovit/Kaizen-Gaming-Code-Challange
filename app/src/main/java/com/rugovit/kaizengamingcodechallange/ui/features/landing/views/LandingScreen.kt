package com.rugovit.kaizengamingcodechallange.ui.features.landing.views
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rugovit.kaizengamingcodechallange.R
import com.rugovit.kaizengamingcodechallange.ui.components.SportsToolbar

@Composable
fun LandingScreen(onNavigateToSports: () -> Unit) {

    Scaffold(
        topBar = { SportsToolbar() },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Button(onClick = onNavigateToSports) {
                Text(stringResource(R.string.loading_screen_go_to_sports))
            }
        }
    }
}