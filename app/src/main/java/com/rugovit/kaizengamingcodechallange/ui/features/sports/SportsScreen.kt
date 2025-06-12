package com.rugovit.kaizengamingcodechallange.ui.features.sports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.domain.models.Sport
import com.rugovit.kaizengamingcodechallange.ui.components.SportSection
import com.rugovit.kaizengamingcodechallange.ui.components.SportsToolbar
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
import com.rugovit.kaizengamingcodechallange.ui.components.getDefaultSport
import com.rugovit.kaizengamingcodechallange.ui.components.getDefaultTime


@Composable
fun SportsScreen(viewModel: SportsViewModel = koinViewModel()) {
    val sportsState by viewModel.sportsState.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()

    Scaffold(
        topBar = { SportsToolbar() },
        containerColor = Color(0xFF343434)
    ) { paddingValues ->
        when (sportsState) {
            is Either.Left -> ErrorState(
                error = (sportsState as Either.Left<AppError>).value,
                onRetry = { viewModel.syncData() },
                modifier = Modifier.padding(paddingValues)
            )
            is Either.Right -> {
                val sports = (sportsState as Either.Right<List<Sport>>).value
                if (sports.all { it.events.isEmpty() }) {
                    EmptyState(modifier = Modifier.padding(paddingValues))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(sports) { sport ->
                            SportSection(
                                sport = sport,
                                currentTime = currentTime,
                                onToggleFavorite = viewModel::toggleFavorite
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No events",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ErrorState(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: ${error.message}",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SportsScreenPreview() {
    val sports = listOf(
        getDefaultSport(),
        getDefaultSport().copy(
            id = "sport2",
            name = "Preview Sport 2",
            events = listOf() // empty to show different states
        )
    )
    val currentTime = getDefaultTime()

    Scaffold(
        topBar = { SportsToolbar() },
        containerColor = Color(0xFF343434)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(sports) { sport ->
                SportSection(
                    sport = sport,
                    currentTime = currentTime,
                    onToggleFavorite = { /* no-op */ }
                )
            }
        }
    }
}
