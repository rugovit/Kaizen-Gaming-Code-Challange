package com.rugovit.kaizengamingcodechallange.ui.features.sports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.domain.models.Event
import com.rugovit.kaizengamingcodechallange.domain.models.Sport
import com.rugovit.kaizengamingcodechallange.ui.features.sports.SportsViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun SportsScreen(viewModel: SportsViewModel = koinViewModel()) {
    val sportsState by viewModel.sportsState.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()

    when (sportsState) {
        is Either.Left -> {
            val error = (sportsState as Either.Left<AppError>).value
            Text(
                text = "Error: ${error.message}",
                modifier = Modifier.fillMaxSize().wrapContentSize()
            )
        }
        is Either.Right -> {
            val sports = (sportsState as Either.Right<List<Sport>>).value
            if (sports.isEmpty()) {
                Text(
                    text = "No sports available",
                    modifier = Modifier.fillMaxSize().wrapContentSize()
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(sports.size) { index ->
                        SportSection(
                            sport = sports[index],
                            currentTime = currentTime,
                            onToggleFavorite = viewModel::toggleFavorite
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SportSection(sport: Sport, currentTime: Long, onToggleFavorite: (String) -> Unit) {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        Text(text = sport.name, style = MaterialTheme.typography.headlineSmall)
        sport.events.forEach { event ->
            EventItem(event = event, currentTime = currentTime, onToggleFavorite = onToggleFavorite)
        }
    }
}

@Composable
fun EventItem(event: Event, currentTime: Long, onToggleFavorite: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val timeRemaining = (event.startTime - currentTime).coerceAtLeast(0) / 1000
        Text(text = "${event.name} - Starts in $timeRemaining s")
        Button(onClick = { onToggleFavorite(event.id) }) {
            Text(if (event.isFavorite) "Unfavorite" else "Favorite")
        }
    }
}