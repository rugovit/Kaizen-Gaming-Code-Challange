package com.rugovit.kaizengamingcodechallange.core.base.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.core.common.toUserMessage

// AppScaffold.kt
@Composable
fun AppScaffold(
    topBar: @Composable () -> Unit,
    appError: AppError,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val snackHost = remember { SnackbarHostState() }

    // latest AppError, if any
    var pendingError by remember { mutableStateOf<AppError?>(null) }



    // compute the localized message in Compose land
    val errorMessage = appError.toUserMessage()

    // show the snackbar when an error arrives
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotBlank()) {
            snackHost.showSnackbar(
                message     = errorMessage,
                actionLabel = "OK"
            )
            pendingError = null
        }
    }

    Scaffold(
        modifier       = modifier,
        topBar         = topBar,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost   = { SnackbarHost(hostState = snackHost) }
    ) { padding ->
        content(padding)
    }
}
