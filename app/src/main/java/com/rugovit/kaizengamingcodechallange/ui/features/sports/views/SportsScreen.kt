package com.rugovit.kaizengamingcodechallange.ui.features.sports.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.core.common.toUserMessage
import com.rugovit.kaizengamingcodechallange.ui.components.FavoriteToggle
import com.rugovit.kaizengamingcodechallange.ui.components.SportsToolbar
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsContract.UiEffect
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsContract.UiIntent
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsScreen(viewModel: SportsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var pendingError by remember { mutableStateOf<AppError?>(null) }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            if (effect is UiEffect.ShowError) pendingError = effect.error
        }
    }
    pendingError?.let { err ->
        val message= err.toUserMessage()
        LaunchedEffect(err) {
            snackbarHostState.showSnackbar(message = message)
            pendingError = null
        }
    }

    // set up scroll state and freeze timer during scroll
    val listState = rememberLazyListState()
    val freezeTime = remember { mutableStateOf(uiState.currentTime) }
    if (!listState.isScrollInProgress) freezeTime.value = uiState.currentTime
    val timeToUse = if (listState.isScrollInProgress) freezeTime.value else uiState.currentTime

    // per-sport UI state
    val expandedMap = remember { mutableStateMapOf<String, Boolean>() }
    val showFavsMap = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = { SportsToolbar() },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState =  snackbarHostState) { data: SnackbarData ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.medium,
            )
        } }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading && uiState.sports.isEmpty()) {
                // initial load spinner
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    uiState.sports.forEach { sport ->
                        val isExpanded = expandedMap.getOrPut(sport.id) { true }
                        val showOnlyFavs = showFavsMap.getOrPut(sport.id) { false }
                        val displayed = if (showOnlyFavs) sport.events.filter { it.isFavorite } else sport.events

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

                        if (isExpanded && displayed.isNotEmpty()) {
                            displayed.chunked(4).forEachIndexed { rowIndex, rowEvents ->
                                item(key = "row_${sport.id}_$rowIndex") {
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
                                                onToggleFavorite = { viewModel.process(UiIntent.ToggleFavorite(it)) }
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
