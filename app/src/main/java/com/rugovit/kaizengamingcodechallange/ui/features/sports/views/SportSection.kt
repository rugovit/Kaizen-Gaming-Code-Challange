package com.rugovit.kaizengamingcodechallange.ui.features.sports.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rugovit.kaizengamingcodechallange.ui.components.FavoriteToggle
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Event
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Sport

@Composable
fun SportSection(
    sport: Sport,
    currentTime: Long,
    onToggleFavorite: (String) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(true) }
    var showOnlyFavorites by rememberSaveable { mutableStateOf(false) }

    val displayedEvents = if (showOnlyFavorites) {
        sport.events.filter { it.isFavorite }
    } else {
        sport.events
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = sport.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Row {
                    FavoriteToggle(
                        isFavorite = showOnlyFavorites,
                        onToggle = { showOnlyFavorites = it }
                    )
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        if (isExpanded && displayedEvents.isNotEmpty()) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    displayedEvents.forEach { event: Event ->
                        EventItem(
                            event = event,
                            currentTime = currentTime,
                            onToggleFavorite = { onToggleFavorite(event.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SportSectionPreview() {
    val sport = Sport(
        id = "sport1",
        name = "Preview Sport",
        events = listOf(
            Event(id = "e1", sportId = "sport1", startTime = getRandTime()  , competitor1 = "Team A", competitor2 = "Team B",isFavorite = false),
            Event(id = "e2", sportId = "sport1",  startTime = getRandTime() ,competitor1 = "Team C", competitor2 = "Team D", isFavorite = true)
        )
    )
    SportSection(
        sport = sport,
        currentTime = System.currentTimeMillis()/1000,
        onToggleFavorite = {}
    )
}
