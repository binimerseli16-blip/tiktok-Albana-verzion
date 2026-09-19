package com.example.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.data.VideoItem
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

data class DoubleTapHeart(
    val id: Long = System.currentTimeMillis() + Random.nextLong(1000),
    val x: Float,
    val y: Float,
    val rotation: Float = Random.nextFloat() * 30f - 15f
)

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerItem(
    video: VideoItem,
    isActive: Boolean,
    onDoubleTapLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var isUserPaused by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    var videoProgress by remember { mutableFloatStateOf(0f) }

    val hearts = remember { mutableStateListOf<DoubleTapHeart>() }

    // ExoPlayer creation & lifecycle with robust HTTP headers
    val exoPlayer = remember(context) {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(15000)
            .setReadTimeoutMs(15000)

        val mediaSourceFactory = DefaultMediaSourceFactory(context)
            .setDataSourceFactory(httpDataSourceFactory)

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
                playWhenReady = false
            }
    }

    DisposableEffect(video.videoUrl) {
        val mediaItem = MediaItem.fromUri(video.videoUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    hasError = false
                }
            }
            override fun onPlayerError(error: PlaybackException) {
                android.util.Log.e("TikTokVideoPlayer", "Playback error for ${video.videoUrl}: ${error.message}", error)
                hasError = true
            }
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.stop()
            exoPlayer.release()
        }
    }

    // Active screen play / pause
    LaunchedEffect(isActive, isUserPaused) {
        if (isActive && !isUserPaused) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    // Progress updates
    LaunchedEffect(isActive, isPlaying) {
        while (isActive && isPlaying) {
            val duration = exoPlayer.duration
            val position = exoPlayer.currentPosition
            if (duration > 0) {
                videoProgress = (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
            }
            delay(100)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        isUserPaused = !isUserPaused
                        if (isUserPaused) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                    },
                    onDoubleTap = { offset ->
                        hearts.add(DoubleTapHeart(x = offset.x, y = offset.y))
                        onDoubleTapLike()
                    }
                )
            }
    ) {
        // Fallback visual / generative backdrop if video is loading or offline
        DynamicBackdrop(
            primaryColor = Color(video.primaryColor),
            secondaryColor = Color(video.secondaryColor),
            isActive = isActive
        )

        // AndroidView with PlayerView
        if (!hasError) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Retry button if network or playback error occurs
        if (hasError) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp)
                    .background(Color(0x88000000), shape = androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        hasError = false
                        exoPlayer.prepare()
                        exoPlayer.play()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Retry playback",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Play / Pause Icon overlay when user paused
        AnimatedVisibility(
            visible = isUserPaused,
            enter = fadeIn(tween(150)) + scaleIn(tween(150), initialScale = 1.3f),
            exit = fadeOut(tween(200)) + scaleOut(tween(200)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0x77000000), shape = androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(52.dp)
                )
            }
        }

        // Double tap animated heart burst at touch location
        hearts.forEach { heart ->
            AnimatedHeartBurst(
                heart = heart,
                onFinished = { hearts.remove(heart) }
            )
        }

        // Bottom progress bar scrubber line (signature TikTok scrubber)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxSize()
            ) {
                val strokeHeight = 2.dp.toPx()
                val y = size.height - strokeHeight
                // Background subtle line
                drawLine(
                    color = Color.White.copy(alpha = 0.15f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeHeight
                )
                // Active white progress
                drawLine(
                    color = Color.White.copy(alpha = 0.8f),
                    start = Offset(0f, y),
                    end = Offset(size.width * videoProgress, y),
                    strokeWidth = strokeHeight
                )
            }
        }
    }
}

@Composable
fun AnimatedHeartBurst(
    heart: DoubleTapHeart,
    onFinished: () -> Unit
) {
    val scale = remember { Animatable(0.3f) }
    val alpha = remember { Animatable(1f) }
    val density = LocalDensity.current

    LaunchedEffect(heart.id) {
        scale.animateTo(
            targetValue = 1.4f,
            animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
        )
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(durationMillis = 100)
        )
        delay(120)
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 250, easing = LinearEasing)
        )
        onFinished()
    }

    val offsetX = with(density) { (heart.x - 48.dp.toPx()).roundToInt() }
    val offsetY = with(density) { (heart.y - 48.dp.toPx()).roundToInt() }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX, offsetY) }
            .size(96.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.alpha = alpha.value
                rotationZ = heart.rotation
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Liked",
            tint = TikTokRed,
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
fun DynamicBackdrop(
    primaryColor: Color,
    secondaryColor: Color,
    isActive: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "backdrop")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        // Gradient base
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    primaryColor,
                    Color.Black,
                    secondaryColor
                )
            )
        )

        // Atmospheric lighting circles
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(TikTokCyan.copy(alpha = 0.25f), Color.Transparent),
                center = Offset(w * 0.2f, h * 0.4f),
                radius = w * 0.7f * pulse
            ),
            center = Offset(w * 0.2f, h * 0.4f),
            radius = w * 0.7f * pulse
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(TikTokRed.copy(alpha = 0.22f), Color.Transparent),
                center = Offset(w * 0.8f, h * 0.6f),
                radius = w * 0.8f * pulse
            ),
            center = Offset(w * 0.8f, h * 0.6f),
            radius = w * 0.8f * pulse
        )
    }
}
