package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.VideoItem
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokYellow

data class ShareFriend(
    val name: String,
    val avatar: String
)

data class ShareAction(
    val title: String,
    val icon: ImageVector,
    val bgColor: Color,
    val iconTint: Color = Color.White
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
    video: VideoItem,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    val friends = listOf(
        ShareFriend("Anna", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&fit=crop"),
        ShareFriend("Leo", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&fit=crop"),
        ShareFriend("Sarah", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&fit=crop"),
        ShareFriend("David", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&fit=crop"),
        ShareFriend("Maya", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&fit=crop")
    )

    val actions = listOf(
        ShareAction("Repost", Icons.Default.Repeat, TikTokYellow, Color.Black),
        ShareAction("Kopjo Linkun", Icons.Default.Link, Color(0xFF2C2E3B)),
        ShareAction("Mesazh", Icons.Default.Send, TikTokCyan, Color.Black),
        ShareAction("Shkarko", Icons.Default.Download, Color(0xFF2C2E3B)),
        ShareAction("QR Kodi", Icons.Default.QrCode, Color(0xFF2C2E3B))
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokDarkSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 14.dp)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Dërgo te (Send to)",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (feedbackMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = feedbackMessage!!,
                    color = Color(0xFF00E676),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Friends Row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            ) {
                items(friends) { friend ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(60.dp)
                            .clickable {
                                feedbackMessage = "U dërgua me sukses te ${friend.name}!"
                            }
                    ) {
                        AsyncImage(
                            model = friend.avatar,
                            contentDescription = friend.name,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = friend.name,
                            color = Color.White,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Color.White.copy(alpha = 0.08f),
                thickness = 0.5.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Shpërndaj në (Share to)",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            ) {
                items(actions) { action ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(64.dp)
                            .clickable {
                                when (action.title) {
                                    "Repost" -> {
                                        com.example.data.TikTokRepository.toggleRepost(video.id)
                                        feedbackMessage = "✅ Videoja u ridërgua në profilin tuaj (Reposted)!"
                                    }
                                    "Kopjo Linkun" -> {
                                        feedbackMessage = "🔗 Linku i videos u kopjua në clipboard!"
                                    }
                                    "Shkarko" -> {
                                        feedbackMessage = "📥 Videoja u shkarkua në galeri me sukses!"
                                    }
                                    "Mesazh" -> {
                                        feedbackMessage = "💬 Zgjidhni mikun për ta dërguar si mesazh direkt."
                                    }
                                    else -> {
                                        feedbackMessage = "📱 QR Kodi u gjenerua për këtë video!"
                                    }
                                }
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(action.bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.title,
                                tint = action.iconTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = action.title,
                            color = Color.White,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
