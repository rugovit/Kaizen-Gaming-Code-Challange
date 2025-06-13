package com.rugovit.kaizengamingcodechallange.ui.features.sports.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rugovit.kaizengamingcodechallange.R
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Event
import com.rugovit.kaizengamingcodechallange.ui.theme.KaizenGamingCodeChallangeTheme
import java.util.Locale

@Composable
fun EventItem(
    event: Event,
    currentTime: Long,
    onToggleFavorite: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 0.dp, vertical = 6.dp)
            .background(MaterialTheme.colorScheme.surface)
            .width(92.dp)
        ,

        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        val secondsRemaining = (event.startTime - currentTime).coerceAtLeast(0)
        val timeText = if (secondsRemaining > 0) {
            val days = secondsRemaining / 86400
            val hours = (secondsRemaining % 86400) / 3600
            val minutes = (secondsRemaining % 3600) / 60
            val secs = secondsRemaining % 60

            String.format(
                Locale.US,
                "%02d:%02d:%02d:%02d",
                days, hours, minutes, secs
            )
        } else {
            stringResource(R.string.started)
        }

        Text(
            text = timeText,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(4.dp)
        )
        IconButton(onClick = { onToggleFavorite(event.id) },
            modifier = Modifier.size(30.dp)) {
            Icon(
                imageVector = if (event.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (event.isFavorite) "Unfavorite" else "Favorite",
                tint = if (event.isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
            )
        }
        Text(text = event.competitor1,textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium,maxLines = 1,overflow = TextOverflow.Ellipsis)
        Text(text = "vs", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyMedium)
        Text(text = event.competitor2, textAlign = TextAlign.Center,color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium,maxLines = 1,overflow = TextOverflow.Ellipsis )
    }
}

@Preview(showBackground = true)
@Composable
fun EventItemPreview() {
    KaizenGamingCodeChallangeTheme {
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
}
fun getRandTime(): Long {
    return (System.currentTimeMillis() + (60_000L * (60 + (0..480).random())))/1000 // 1 minute to 8 minutes ahead
}

