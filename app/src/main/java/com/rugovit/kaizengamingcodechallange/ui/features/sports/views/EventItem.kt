package com.rugovit.kaizengamingcodechallange.ui.features.sports.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rugovit.kaizengamingcodechallange.R
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Event

@Composable
fun EventItem(
    event: Event,
    currentTime: Long,
    onToggleFavorite: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(all = 8.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        val secondsRemaining = (event.startTime - currentTime).coerceAtLeast(0)
        val timeText = if (secondsRemaining > 0) {
            val days = secondsRemaining / 86400
            val hours = (secondsRemaining % 86400) / 3600
            val minutes = (secondsRemaining % 3600) / 60
            val secs = secondsRemaining % 60

            buildString {
                if (days > 0) append("${days}${stringResource(R.string.time_days)} ")
                if (hours > 0 || days > 0) append("${hours}${stringResource(R.string.time_hours)} ")
                if (minutes > 0 || hours > 0 || days > 0) append("${minutes}${stringResource(R.string.time_minutes)} ")
                append("${secs}${stringResource(R.string.time_seconds)} ")
            }.trim()
        } else {
            stringResource(R.string.started)
        }

        Text(
            text = timeText,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(4.dp)
        )
        IconButton(onClick = { onToggleFavorite(event.id) }) {
            Icon(
                imageVector = if (event.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (event.isFavorite) "Unfavorite" else "Favorite",
                tint = if (event.isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
            )
        }
        Text(text = event.competitor1, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
        Text(text = "vs", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyMedium)
        Text(text = event.competitor2, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun EventItemPreview() {
    val event = Event(
        id = "e1",
        sportId = "sport1",
        startTime = getRandTime(),
        competitor1 = "Team A",
        competitor2 = "Team B",
        isFavorite = false
    )
    EventItem(
        event = event,
        currentTime = System.currentTimeMillis() / 1000,
        onToggleFavorite = {}
    )
}
fun getRandTime(): Long {
    return (System.currentTimeMillis() + (60_000L * (60 + (0..480).random())))/1000 // 1 minute to 8 minutes ahead
}

