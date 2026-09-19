package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TikTokRepository
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkCard
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay

@Composable
fun CreateVideoScreen(
    initialSound: String? = null,
    onClose: () -> Unit,
    onVideoCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }
    var recordingProgress by remember { mutableFloatStateOf(0f) }
    var selectedDuration by remember { mutableStateOf("15s") }
    var selectedSpeed by remember { mutableStateOf("1x") }
    var isFrontCamera by remember { mutableStateOf(false) }
    var showPostDialog by remember { mutableStateOf(false) }
    var videoCaption by remember { mutableStateOf("My new TikTok video! 🔥 #vibes #trending") }
    var currentSound by remember { mutableStateOf(initialSound ?: "Add sound") }

    // Recording progress simulation
    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingProgress = 0f
            while (recordingProgress < 1f && isRecording) {
                delay(50)
                recordingProgress += 0.015f
            }
            if (isRecording) {
                isRecording = false
                showPostDialog = true
            }
        }
    }

    // Camera pulse animation for shutter button
    val infiniteTransition = rememberInfiniteTransition(label = "shutter")
    val shutterPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F12))
    ) {
        // Viewfinder simulated backdrop (gradient with subtle camera grain)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF232535), Color(0xFF0C0D11)),
                        radius = 1200f
                    )
                )
        )

        // Top Progress Line while recording
        if (isRecording) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color(0x33FFFFFF))
                    .statusBarsPadding()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(recordingProgress)
                        .height(4.dp)
                        .background(TikTokRed)
                )
            }
        }

        // Top Header Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // "Add sound" pill button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x55000000))
                    .clickable {
                        currentSound = "Viral TikTok Beat #1"
                    }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = TikTokCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentSound,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(
                onClick = { /* Flash toggle */ },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = "Flash",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Right side camera controls column
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CameraToolButton(
                icon = Icons.Default.Cameraswitch,
                label = "Flip",
                onClick = { isFrontCamera = !isFrontCamera }
            )
            CameraToolButton(
                icon = Icons.Default.Speed,
                label = selectedSpeed,
                onClick = {
                    selectedSpeed = when (selectedSpeed) {
                        "1x" -> "2x"
                        "2x" -> "3x"
                        "3x" -> "0.5x"
                        else -> "1x"
                    }
                }
            )
            CameraToolButton(
                icon = Icons.Default.Face,
                label = "Beauty",
                onClick = { }
            )
            CameraToolButton(
                icon = Icons.Default.Timer,
                label = "Timer",
                onClick = { }
            )
        }

        // Bottom Controls: Duration Selector + Shutter Button + Upload / Effects
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Duration selector tabs (15s, 60s, 10m)
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                listOf("15s", "60s", "10m").forEach { duration ->
                    Text(
                        text = duration,
                        color = if (selectedDuration == duration) Color.White else Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        fontWeight = if (selectedDuration == duration) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.clickable { selectedDuration = duration }
                    )
                }
            }

            // Bottom action row: Effects | Shutter | Upload
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Effects / Filters
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { }
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(TikTokDarkCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Effects",
                            tint = TikTokCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Effects",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }

                // Shutter / Record Button (Iconic Red Ring)
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (!isRecording) {
                                isRecording = true
                            } else {
                                isRecording = false
                                showPostDialog = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Outer Ring
                    Box(
                        modifier = Modifier
                            .size(if (isRecording) (86 * shutterPulse).dp else 86.dp)
                            .border(4.dp, TikTokRed.copy(alpha = 0.5f), CircleShape)
                    )

                    // Inner Red Circle / Square when recording
                    Box(
                        modifier = Modifier
                            .size(if (isRecording) 36.dp else 68.dp)
                            .clip(if (isRecording) RoundedCornerShape(8.dp) else CircleShape)
                            .background(TikTokRed)
                    )
                }

                // Upload from gallery
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { showPostDialog = true }
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TikTokDarkCard)
                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Upload",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Upload",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Post Video Dialog
        if (showPostDialog) {
            AlertDialog(
                onDismissRequest = { showPostDialog = false },
                title = {
                    Text(
                        text = "Post to TikTok",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Add a caption, hashtags, and sound to publish your short video.",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                        OutlinedTextField(
                            value = videoCaption,
                            onValueChange = { videoCaption = it },
                            label = { Text("Caption & #Hashtags") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            TikTokRepository.addCreatedVideo(videoCaption)
                            showPostDialog = false
                            onVideoCreated()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokRed)
                    ) {
                        Text("Post Video", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPostDialog = false }) {
                        Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                    }
                },
                containerColor = TikTokDarkCard
            )
        }
    }
}

@Composable
fun CameraToolButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
