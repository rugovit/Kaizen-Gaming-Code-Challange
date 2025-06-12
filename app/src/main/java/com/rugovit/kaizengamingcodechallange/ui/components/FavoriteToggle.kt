package com.rugovit.kaizengamingcodechallange.ui.components
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteToggle(
    isFavorite: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
        .width(60.dp)      // total width of the “track”
        .height(40.dp)     // height = diameter of the thumb
    ,
    color: SwitchColors = SwitchDefaults.colors(
        checkedThumbColor   = MaterialTheme.colorScheme.primary,
        uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
        checkedTrackColor   = MaterialTheme.colorScheme.surface,
        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
    )
) {
    Switch(
        checked = isFavorite,
        onCheckedChange = onToggle,
        modifier = modifier,
        thumbContent = {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
                tint = Color.White,
                modifier = Modifier.size(24.dp)  // star size, inset by default padding
            )
        },
        colors = color
    )
}


//create preview function
@Preview(showBackground = true)
@Composable
fun FavoriteTogglePreview() {
    MaterialTheme {
        FavoriteToggle(
            isFavorite = true,
            onToggle = {}
        )
    }
}