package com.rugovit.kaizengamingcodechallange.ui.features.sports.views.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Event

@Composable
fun EventItem(
    event: Event,
    onToggleFavorite: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(all = 8.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = event.timeRemaining,
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
        Text(
            text = event.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventItemPreview() {
    val event = Event(
        id = "e1",
        sportId = "sport1",
        name = "Team A - Team B",
        timeRemaining = "1h 0m 0s",
        isFavorite = false
    )
    EventItem(
        event = event,
        onToggleFavorite = {}
    )
}
