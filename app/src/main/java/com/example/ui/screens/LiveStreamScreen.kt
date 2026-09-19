package com.example.ui.screens

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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.LiveStreamItem
import com.example.data.TikTokRepository
import com.example.data.VirtualGift
import com.example.ui.components.GiftBottomSheet
import com.example.ui.components.UckEmblemBadge
import com.example.ui.components.WalletDialog
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkCard
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay
import kotlin.random.Random

data class FlyingHeart(
    val id: Long = System.currentTimeMillis() + Random.nextLong(1000),
    val startX: Float,
    val color: Color
)

@Composable
fun LiveStreamScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val liveStreams by TikTokRepository.liveStreams.collectAsState()
    val activeStream = liveStreams.firstOrNull() ?: return
    var showGiftSheet by remember { mutableStateOf(false) }
    var showWalletDialog by remember { mutableStateOf(false) }
    var showStartLiveDialog by remember { mutableStateOf(false) }
    var chatInput by remember { mutableStateOf("") }
    var activeGiftBanner by remember { mutableStateOf<Pair<VirtualGift, String>?>(null) }

    val flyingHearts = remember { mutableStateListOf<FlyingHeart>() }
    val chatListState = rememberLazyListState()

    // Scroll chat to bottom on new message
    LaunchedEffect(activeStream.comments.size) {
        if (activeStream.comments.isNotEmpty()) {
            chatListState.animateScrollToItem(activeStream.comments.size - 1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val heartColors = listOf(TikTokRed, TikTokCyan, Color(0xFFFFD700), Color(0xFFFF4081))
                    flyingHearts.add(
                        FlyingHeart(
                            startX = offset.x,
                            color = heartColors.random()
                        )
                    )
                }
            }
    ) {
        // Stream Atmospheric Visual Background
        LiveBackgroundAnimation()

        // Top Header Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp, start = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Host Information & Follow Button
            Row(
                modifier = Modifier
                    .background(Color(0x88000000), shape = RoundedCornerShape(24.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = activeStream.hostAvatar,
                    contentDescription = "Host Avatar",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = activeStream.hostName,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "❤️ 38.4K",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(TikTokRed, shape = RoundedCornerShape(16.dp))
                        .clickable { TikTokRepository.toggleFollow(activeStream.hostHandle) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("+ Ndjek", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Viewers Pill & Close Action
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(Color(0x88000000), shape = RoundedCornerShape(16.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(TikTokRed, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${activeStream.viewersCount} shikues",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0x88000000), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Stream Category / Title Pill
        Box(
            modifier = Modifier
                .padding(top = 96.dp, start = 16.dp)
                .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${activeStream.category} • ${activeStream.title}",
                color = Color.White,
                fontSize = 11.sp
            )
        }

        // Animated Flying Hearts Container
        flyingHearts.forEach { heart ->
            FlyingHeartAnimation(
                heart = heart,
                onFinished = { flyingHearts.remove(heart) }
            )
        }

        // Gift Banner Animation across screen
        AnimatedVisibility(
            visible = activeGiftBanner != null,
            enter = fadeIn() + scaleIn(initialScale = 0.7f),
            exit = fadeOut() + scaleOut(targetScale = 1.3f),
            modifier = Modifier.align(Alignment.Center)
        ) {
            val giftInfo = activeGiftBanner
            if (giftInfo != null) {
                val isUck = giftInfo.first.isSpecialUck
                Box(
                    modifier = Modifier
                        .background(
                            brush = if (isUck) {
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF8B0000),
                                        Color(0xFFB71C1C),
                                        Color(0xFF3E0000)
                                    )
                                )
                            } else {
                                Brush.horizontalGradient(
                                    listOf(TikTokRed.copy(alpha = 0.9f), TikTokCyan.copy(alpha = 0.9f))
                                )
                            },
                            shape = RoundedCornerShape(28.dp)
                        )
                        .then(
                            if (isUck) {
                                Modifier.border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(
                                        listOf(Color(0xFFFFD700), Color(0xFFFF8C00), Color(0xFFFFD700))
                                    ),
                                    shape = RoundedCornerShape(28.dp)
                                )
                            } else Modifier
                        )
                        .padding(horizontal = 24.dp, vertical = 14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isUck) {
                            UckEmblemBadge(size = 54.dp)
                        } else {
                            Text(text = giftInfo.first.emoji, fontSize = 42.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${giftInfo.second} dërgoi",
                                    color = if (isUck) Color(0xFFFFE082) else Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp,
                                    fontWeight = if (isUck) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isUck) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "👑 10,000 🪙",
                                        color = Color(0xFFFFD700),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                            Text(
                                text = if (isUck) "🦅 UÇK - LEGJENDA KOMBËTARE 🇦🇱" else giftInfo.first.name,
                                color = Color.White,
                                fontSize = if (isUck) 16.sp else 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Bottom Left Scrolling Chat Section
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.82f)
                .padding(start = 12.dp, bottom = 76.dp)
        ) {
            LazyColumn(
                state = chatListState,
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(activeStream.comments) { comment ->
                    Box(
                        modifier = Modifier
                            .background(Color(0x77000000), shape = RoundedCornerShape(14.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${comment.user}: ",
                                color = TikTokCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = comment.text,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                            if (comment.giftEmoji != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = comment.giftEmoji, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        // Bottom Interaction Bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Chat Input
            OutlinedTextField(
                value = chatInput,
                onValueChange = { chatInput = it },
                placeholder = { Text("Shkruaj një koment...", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White.copy(alpha = 0.4f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color(0x66000000),
                    unfocusedContainerColor = Color(0x66000000)
                ),
                shape = RoundedCornerShape(20.dp),
                trailingIcon = {
                    if (chatInput.isNotBlank()) {
                        IconButton(onClick = {
                            TikTokRepository.addLiveComment(activeStream.id, chatInput)
                            chatInput = ""
                        }) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = TikTokCyan)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            )

            // Gift Button (🎁)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        brush = Brush.radialGradient(listOf(Color(0xFFFFD700), Color(0xFFFF8C00))),
                        shape = CircleShape
                    )
                    .clickable { showGiftSheet = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CardGiftcard,
                    contentDescription = "Send Gift",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Creator "Start My Live" Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                    .clickable { showStartLiveDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Go Live",
                    tint = TikTokRed,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    // Gift Bottom Sheet
    if (showGiftSheet) {
        GiftBottomSheet(
            recipientName = activeStream.hostName,
            onDismiss = { showGiftSheet = false },
            onGiftSent = { gift ->
                val commentText = if (gift.isSpecialUck) {
                    "🦅🇦🇱 [DHURATË SUPREME]: Dërgoi UÇK (10,000 🪙) me Krenari Kombëtare! 🇦🇱🦅"
                } else {
                    "dërgoi ${gift.name}!"
                }
                TikTokRepository.addLiveComment(
                    streamId = activeStream.id,
                    text = commentText,
                    giftEmoji = if (gift.isSpecialUck) "🦅 UÇK 🇦🇱" else gift.emoji
                )
                activeGiftBanner = Pair(gift, "Ti")
            },
            onRechargeClick = {
                showGiftSheet = false
                showWalletDialog = true
            }
        )
    }

    // Auto dismiss gift banner after 3 seconds
    LaunchedEffect(activeGiftBanner) {
        if (activeGiftBanner != null) {
            delay(3000)
            activeGiftBanner = null
        }
    }

    // Wallet Dialog
    if (showWalletDialog) {
        WalletDialog(onDismiss = { showWalletDialog = false })
    }

    // Start My Live Dialog
    if (showStartLiveDialog) {
        var liveTitle by remember { mutableStateOf("") }
        var selectedCat by remember { mutableStateOf("Bisedë & Chill") }

        androidx.compose.ui.window.Dialog(onDismissRequest = { showStartLiveDialog = false }) {
            androidx.compose.material3.Surface(
                shape = RoundedCornerShape(20.dp),
                color = TikTokDarkCard,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Fillo Transmetimin LIVE 🔴",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = liveTitle,
                        onValueChange = { liveTitle = it },
                        label = { Text("Titulli i LIVE-it tuaj") },
                        placeholder = { Text("P.sh. Diskutojmë muzikë & Q&A me ndjekësit 🔥") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = TikTokRed,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Kategoria:", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Bisedë", "Muzikë", "Gaming").forEach { cat ->
                            val sel = selectedCat == cat
                            Box(
                                modifier = Modifier
                                    .background(if (sel) TikTokRed else Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .clickable { selectedCat = cat }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(cat, color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            TikTokRepository.startLiveStream(liveTitle, selectedCat)
                            showStartLiveDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Fillo LIVE Tani 🚀", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun FlyingHeartAnimation(
    heart: FlyingHeart,
    onFinished: () -> Unit
) {
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val scale = remember { Animatable(0.6f) }

    LaunchedEffect(heart.id) {
        scale.animateTo(1.2f, tween(200, easing = FastOutSlowInEasing))
        offsetY.animateTo(-400f, tween(1200, easing = LinearEasing))
        alpha.animateTo(0f, tween(300))
        onFinished()
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(heart.startX.toInt() - 24, (offsetY.value + 600).toInt()) }
            .graphicsLayer {
                this.alpha = alpha.value
                scaleX = scale.value
                scaleY = scale.value
            }
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Flying heart",
            tint = heart.color,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun LiveBackgroundAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "live_bg")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF1F0826), Color(0xFF0D0412), Color(0xFF04101A))
            )
        )
        drawCircle(
            brush = Brush.radialGradient(
                listOf(TikTokRed.copy(alpha = 0.35f), Color.Transparent),
                center = androidx.compose.ui.geometry.Offset(w * 0.3f, h * 0.35f),
                radius = w * 0.8f * pulse
            ),
            center = androidx.compose.ui.geometry.Offset(w * 0.3f, h * 0.35f),
            radius = w * 0.8f * pulse
        )
        drawCircle(
            brush = Brush.radialGradient(
                listOf(TikTokCyan.copy(alpha = 0.3f), Color.Transparent),
                center = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.65f),
                radius = w * 0.9f * pulse
            ),
            center = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.65f),
            radius = w * 0.9f * pulse
        )
    }
}
