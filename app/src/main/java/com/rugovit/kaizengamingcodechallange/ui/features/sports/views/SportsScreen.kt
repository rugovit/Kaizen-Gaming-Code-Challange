package com.rugovit.kaizengamingcodechallange.ui.features.sports.views
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.core.common.toUserMessage
import com.rugovit.kaizengamingcodechallange.ui.components.SportsToolbar
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Sport
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsContract
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsContract.UiEffect
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsContract.UiIntent
import com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel.SportsViewModel
import com.rugovit.kaizengamingcodechallange.ui.theme.KaizenGamingCodeChallangeTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SportsScreen(viewModel: SportsViewModel = koinViewModel()) {


    val uiState by viewModel.uiState.collectAsState()
    val currentError = uiState.error
    var pendingError by remember { mutableStateOf<AppError?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle one-off effects
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is UiEffect.ShowError -> {
                    pendingError = effect.error
                }
            }
        }
    }
    pendingError?.let { error ->
        val message = error.toUserMessage()
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "OK"
            )
            pendingError = null
        }
    }

    Scaffold(
        topBar = { SportsToolbar() },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        when {
            currentError != null -> ErrorState(
                error = currentError,
                onRetry = { viewModel.process(UiIntent.SyncData) },
                modifier = Modifier.padding(paddingValues)
            )

            uiState.sports.all { it.events.isEmpty() } -> EmptyState(
                modifier = Modifier.padding(paddingValues)
            )

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.sports) { sport: Sport ->
                    SportSection(
                        sport = sport,
                        currentTime = uiState.currentTime,
                        onToggleFavorite = { eventId ->
                            viewModel.process(UiIntent.ToggleFavorite(eventId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No events",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ErrorState(
    error: AppError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (error) {
                is AppError.NetworkError -> "No internet connection"
                else -> "Something went wrong"
            },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SportsScreenPreview() {
    val sports = listOf(
        Sport(
            id = "sport1",
            name = "Preview Sport",
            events = listOf()
        ),
        Sport(
            id = "sport2",
            name = "Preview Sport 2",
            events = listOf()
        )
    )
    val uiState = SportsContract.UiState(
        isLoading = false,
        sports = sports,
        error = null
    )

    KaizenGamingCodeChallangeTheme {
        Scaffold(
            topBar = { SportsToolbar() }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.sports) { sport ->
                    SportSection(
                        sport = sport,
                        currentTime = System.currentTimeMillis()/1000,
                        onToggleFavorite = { }
                    )
                }
            }
        }
    }
}
