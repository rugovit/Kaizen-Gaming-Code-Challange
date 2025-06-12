package com.rugovit.kaizengamingcodechallange.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rugovit.kaizengamingcodechallange.domain.models.Event
import com.rugovit.kaizengamingcodechallange.domain.models.Sport

@Composable
fun EventItem(
    event: Event,
    currentTime: Long,
    onToggleFavorite: (String) -> Unit
) {
    val competitors = event.name.split("-")
    val competitor1 = competitors.getOrNull(0)?.trim() ?: ""
    val competitor2 = competitors.getOrNull(1)?.trim() ?: ""
    val secondsRemaining = (event.startTime - currentTime).coerceAtLeast(0) / 1000
    val timeText = if (secondsRemaining > 0) {
        formatTimeRemaining(secondsRemaining)
    } else {
        "Started"
    }

    Column(
        modifier = Modifier
            .padding(all = 8.dp)
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(1.dp, Color(0xFF0094FF), shape = RoundedCornerShape(4.dp))
                .padding(all = 4.dp)
        ) {
            Text(
                text = timeText,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(onClick = { onToggleFavorite(event.id) }) {
            Icon(
                imageVector = if (event.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (event.isFavorite) "Unfavorite" else "Favorite",
                tint = if (event.isFavorite) Color(0xFFFFAB30) else Color.White
            )
        }
        Text(text = competitor1, color = Color.White, style = MaterialTheme.typography.bodyMedium)
        Text(text = "vs", color = Color(0xFFE7410F), style = MaterialTheme.typography.bodyMedium)
        Text(text = competitor2, color = Color.White, style = MaterialTheme.typography.bodyMedium)
    }
}

fun formatTimeRemaining(seconds: Long): String {
    val days = seconds / 86400
    val hours = (seconds % 86400) / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return buildString {
        if (days > 0) append("${days}d ")
        if (hours > 0 || days > 0) append("${hours}h ")
        if (minutes > 0 || hours > 0 || days > 0) append("${minutes}m ")
        append("${secs}s")
    }.trim()
}
@Preview(showBackground = true)
@Composable
fun EventItemPreview(event: Event = getDefaultEvent(),
                     currentTime: Long = getDefaultTime()) {
    // Use a simple Event with a 1-hour countdown and toggle off
    EventItem(
        event = event,
        currentTime = currentTime,
        onToggleFavorite = { /* no-op */ }
    )
}
fun getDefaultEvent(): Event {
    return Event(
                id = "e1",
                sportId = "sport ID",
                name = "Team A - Team B",
                startTime = 3_600_000L, // 1 hour ahead
                isFavorite = false
            )
}
fun getDefaultTime(): Long {
    return System.currentTimeMillis() + (60_000L * (60 + (0..480).random())) // 1 minute to 8 minutes ahead
}

