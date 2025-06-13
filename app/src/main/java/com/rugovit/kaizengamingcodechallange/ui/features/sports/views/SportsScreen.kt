package com.rugovit.kaizengamingcodechallange.ui.features.sports.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rugovit.kaizengamingcodechallange.R
import com.rugovit.kaizengamingcodechallange.ui.components.FavoriteToggle
import com.rugovit.kaizengamingcodechallange.ui.components.SportsToolbar
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Event
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Sport
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsContract
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsContract.UiIntent
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsViewModel
import com.rugovit.kaizengamingcodechallange.ui.theme.KaizenGamingCodeChallangeTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SportsScreen(viewModel: SportsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    SportsScreenContent(
        uiState = uiState,
        onToggleFavorite = { eventId -> viewModel.process(UiIntent.ToggleFavorite(eventId)) },
        snackbarHostState = snackbarHostState,
        onRetry = { viewModel.process(UiIntent.SyncData) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsScreenContent(
    uiState: SportsContract.UiState,
    onToggleFavorite: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    onRetry: () -> Unit = { }
) {
    val listState = rememberLazyListState()
    val freezeTime = remember { mutableStateOf(uiState.currentTime) }
    if (!listState.isScrollInProgress) freezeTime.value = uiState.currentTime
    val timeToUse = if (listState.isScrollInProgress) freezeTime.value else uiState.currentTime

    val expandedMap = remember { mutableStateMapOf<String, Boolean>() }
    val showFavsMap = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = { SportsToolbar() },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.medium
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding).background(MaterialTheme.colorScheme.surface)
        ) {
            if (uiState.isLoading && uiState.sports.isEmpty()) {
                // Initial load spinner
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else if(!uiState.isLoading && uiState.sports.isEmpty()){
            // Empty state view
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.sport_screen_no_events),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(8.dp)
                )
                Button(
                    onClick = onRetry,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text =  stringResource(R.string.sport_screen_retry))
                }
            }
        }
            else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    uiState.sports.forEach { sport ->
                        val isExpanded = expandedMap.getOrPut(sport.id) { true }
                        val showOnlyFavs = showFavsMap.getOrPut(sport.id) { false }
                        val displayed = if (showOnlyFavs) sport.events.filter { it.isFavorite } else sport.events

                        // Header item
                        item(key = "header_${sport.id}") {
                            Surface(
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
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
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                    Row {
                                        FavoriteToggle(
                                            isFavorite = showOnlyFavs,
                                            onToggle = { showFavsMap[sport.id] = it }
                                        )
                                        IconButton(onClick = { expandedMap[sport.id] = !isExpanded }) {
                                            Icon(
                                                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                                tint = MaterialTheme.colorScheme.onBackground
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Events item with animation
                        item(key = "events_${sport.id}") {
                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                            ) {
                                if (displayed.isNotEmpty()) {
                                    Column {
                                        displayed.chunked(4).forEach { rowEvents ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 8.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                rowEvents.forEach { event ->
                                                    EventItem(
                                                        event = event,
                                                        currentTime = timeToUse,
                                                        onToggleFavorite = onToggleFavorite
                                                    )
                                                }
                                                if (rowEvents.size < 4) repeat(4 - rowEvents.size) {
                                                    Spacer(modifier = Modifier.width(92.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SportsScreenPreview() {
    KaizenGamingCodeChallangeTheme {
        val mockSports = listOf(
            Sport(
                id = "s1",
                name = "Football",
                events = listOf(
                    Event(
                        id = "e1",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team A",
                        competitor2 = "Team B",
                        isFavorite = false
                    ),
                    Event(
                        id = "e2",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team C",
                        competitor2 = "Team D",
                        isFavorite = false
                    ),
                    Event(
                        id = "e3",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team H",
                        competitor2 = "Team SU",
                        isFavorite = false
                    ),
                    Event(
                        id = "e4",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team akaf",
                        competitor2 = "Team SLSLS",
                        isFavorite = false
                    ),
                    Event(
                        id = "e5",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team KFFF",
                        competitor2 = "Team KFFKF",
                        isFavorite = false
                    ),
                )
            ),
            Sport(
                id = "s2",
                name = "Basketball",
                events = listOf(
                    Event(
                        id = "e1",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team A",
                        competitor2 = "Team B",
                        isFavorite = false
                    ),
                    Event(
                        id = "e2",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team C",
                        competitor2 = "Team D",
                        isFavorite = false
                    ),
                    Event(
                        id = "e3",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team H",
                        competitor2 = "Team SU",
                        isFavorite = false
                    ),
                    Event(
                        id = "e4",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team akaf",
                        competitor2 = "Team SLSLS",
                        isFavorite = false
                    ),
                    Event(
                        id = "e5",
                        sportId = "sport1",
                        startTime = getRandTime(),
                        competitor1 = "Team KFFF",
                        competitor2 = "Team KFFKF",
                        isFavorite = false
                    ),
                )
            )
        )
        val mockUiState = SportsContract.UiState(
            isLoading = false,
            sports = mockSports,
            currentTime = 1000000000L
        )
        val snackbarHostState = remember { SnackbarHostState() }

        SportsScreenContent(
            uiState = mockUiState,
            onToggleFavorite = { /* No-op for static preview */ },
            snackbarHostState = snackbarHostState
        )
    }
}