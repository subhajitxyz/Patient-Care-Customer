package com.real.patientcare.ui.presentation.emergencyVideo

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.io.File

@Composable
fun EmergencyVideoScreen(
    emergencyName: String,
    eventId: String,
    viewModel: EmergencyVideoViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.onIntent(
            EmergencyVideoContracts.EmergencyVideoIntent.LoadVideo(
                emergencyName = emergencyName,
                eventId = eventId
            )
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        when {

            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.localVideoPath != null -> {

                VideoPlayer(
                    videoPath = state.localVideoPath
                )
            }
            state.error != null -> {

                Text(
                    text = state.error ?: "Error"
                )
            }
        }
    }
}


@Composable
fun VideoPlayer(
    videoPath: String?
) {

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    var playerView: PlayerView? by remember {
        mutableStateOf(null)
    }

    // Load video
    LaunchedEffect(videoPath) {
        if (videoPath.isNullOrEmpty()) return@LaunchedEffect

        val mediaItem = MediaItem.fromUri(
            Uri.fromFile(File(videoPath))
        )

        exoPlayer.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    // Cleanup
    DisposableEffect(Unit) {

        onDispose {
            // Detach player immediately
            playerView?.player = null

            exoPlayer.apply {
                playWhenReady = false
                pause()
                stop()
                clearMediaItems()
                clearVideoSurface()
                release()
            }
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
                playerView = this
            }
        },

        update = {
            it.player = exoPlayer
        },

        modifier = Modifier.fillMaxSize()
    )
}