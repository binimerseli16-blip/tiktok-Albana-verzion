package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.TikTokRepository
import com.example.ui.components.GiftBottomSheet
import com.example.ui.components.WalletDialog
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkCard
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokTextSecondary

@Composable
fun ChatDetailScreen(
    conversationId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val conversations by TikTokRepository.conversations.collectAsState()
    val conversation = conversations.find { it.id == conversationId } ?: conversations.firstOrNull() ?: return
    var messageText by remember { mutableStateOf("") }
    var showGiftSheet by remember { mutableStateOf(false) }
    var showWalletDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(conversation.messages.size) {
        if (conversation.messages.isNotEmpty()) {
            listState.animateScrollToItem(conversation.messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Box {
                AsyncImage(
                    model = conversation.participantAvatar,
                    contentDescription = conversation.participantName,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00E676))
                        .align(Alignment.BottomEnd)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.participantName,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${conversation.participantHandle} • Aktiv tani",
                    color = TikTokTextSecondary,
                    fontSize = 11.sp
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Video Call",
                    tint = Color.White
                )
            }
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(conversation.messages) { msg ->
                val isMe = msg.isMe
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    if (!isMe) {
                        AsyncImage(
                            model = conversation.participantAvatar,
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .align(Alignment.Bottom),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Column(
                        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
                        modifier = Modifier.widthIn(max = 260.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isMe) TikTokCyan.copy(alpha = 0.35f) else TikTokDarkCard,
                                    shape = RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isMe) 16.dp else 4.dp,
                                        bottomEnd = if (isMe) 4.dp else 16.dp
                                    )
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Column {
                                if (msg.giftEmoji != null) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = msg.giftEmoji, fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Dhuratë!",
                                            color = Color(0xFFFFD700),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                if (msg.text.isNotBlank()) {
                                    Text(
                                        text = msg.text,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = msg.timestamp,
                            color = TikTokTextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // Input Field Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TikTokDarkSurface)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showGiftSheet = true }) {
                Icon(
                    imageVector = Icons.Default.CardGiftcard,
                    contentDescription = "Send Gift",
                    tint = Color(0xFFFFD700)
                )
            }

            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Dërgo një mesazh...", color = TikTokTextSecondary, fontSize = 13.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = TikTokCyan,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                    focusedContainerColor = Color.White.copy(alpha = 0.05f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            if (messageText.isNotBlank()) {
                IconButton(
                    onClick = {
                        TikTokRepository.sendMessage(conversation.id, messageText)
                        messageText = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = TikTokCyan
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        TikTokRepository.sendMessage(conversation.id, "🎤 [Mesazh zanor 0:04]")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice note",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }

    if (showGiftSheet) {
        GiftBottomSheet(
            recipientName = conversation.participantName,
            onDismiss = { showGiftSheet = false },
            onGiftSent = { gift ->
                val text = if (gift.isSpecialUck) {
                    "🦅🇦🇱 Të dërgova dhuratën më të çmuar: UÇK (10,000 CONSA)! 🇦🇱🦅"
                } else {
                    "Të dërgova ${gift.name} ${gift.emoji}!"
                }
                TikTokRepository.sendMessage(
                    conversationId = conversation.id,
                    text = text,
                    giftEmoji = if (gift.isSpecialUck) "🦅 UÇK 🇦🇱" else gift.emoji
                )
            },
            onRechargeClick = {
                showGiftSheet = false
                showWalletDialog = true
            }
        )
    }

    if (showWalletDialog) {
        WalletDialog(onDismiss = { showWalletDialog = false })
    }
}
