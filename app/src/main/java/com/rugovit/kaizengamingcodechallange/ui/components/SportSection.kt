package com.rugovit.kaizengamingcodechallange.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rugovit.kaizengamingcodechallange.domain.models.Event
import com.rugovit.kaizengamingcodechallange.domain.models.Sport

@Composable
fun SportSection(
    sport: Sport,
    currentTime: Long,
    onToggleFavorite: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    val displayedEvents = if (showOnlyFavorites) {
        sport.events.filter { it.isFavorite }
    } else {
        sport.events
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header
        Surface(
            color = Color.White,
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
                            .size(8.dp)
                            .background(Color(0xFFE7410F), shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = sport.name,
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Row {
                    IconToggleButton(
                        checked = showOnlyFavorites,
                        onCheckedChange = { showOnlyFavorites = it }
                    ) {
                        if (showOnlyFavorites) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color(0xFF0094FF), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Show only favorites",
                                    tint = Color.White
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .border(1.dp, Color.Black, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Star,
                                    contentDescription = "Show all events",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ArrowDropDown else Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = Color.Black
                        )
                    }
                }
            }
        }

        // Events FlowRow
        if (isExpanded && displayedEvents.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                displayedEvents.forEach { event ->
                    EventItem(
                        event = event,
                        currentTime = currentTime,
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SportSectionPreview(sport: Sport = getDefaultSport(),
                        currentTime: Long = getDefaultTime()) {
    SportSection(
        sport = sport,
        currentTime = currentTime,
        onToggleFavorite = { /* no-op */ }
    )
}
fun getDefaultSport(): Sport {
    return Sport(
        id = "sport ID",
        name = "Preview Sport",
        events = listOf(
            Event(
                id = "e1",
                sportId = "sport ID",
                name = "Team A - Team B",
                startTime = 3_600_000L, // 1 hour ahead
                isFavorite = false
            ),
            Event(
                id = "e2",
                sportId = "sport ID",
                name = "Team C - Team D",
                startTime = 7_200_000L, // 2 hours ahead
                isFavorite = true
            )
        )
    )
}

