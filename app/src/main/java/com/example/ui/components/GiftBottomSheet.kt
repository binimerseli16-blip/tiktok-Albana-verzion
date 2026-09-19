package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TikTokRepository
import com.example.data.VirtualGift
import com.example.ui.theme.TikTokDarkCard
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokRed

/**
 * UÇK Emblem Badge - The highest prestige gift in the app (10,000 coins).
 * Features deep crimson red gradient, gold double border, eagle and bold UÇK text.
 */
@Composable
fun UckEmblemBadge(
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE53935),
                        Color(0xFFB71C1C),
                        Color(0xFF4A0000)
                    )
                )
            )
            .border(
                width = 1.8.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color(0xFFFFDF00),
                        Color(0xFFFFA000),
                        Color(0xFFFFD700)
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🦅",
                fontSize = (size.value * 0.34f).sp,
                lineHeight = (size.value * 0.34f).sp
            )
            Text(
                text = "UÇK",
                color = Color(0xFFFFDF00),
                fontSize = (size.value * 0.28f).sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
                lineHeight = (size.value * 0.28f).sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftBottomSheet(
    recipientName: String,
    onDismiss: () -> Unit,
    onGiftSent: (VirtualGift) -> Unit,
    onRechargeClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val user by TikTokRepository.currentUser.collectAsState()
    val gifts = TikTokRepository.virtualGifts

    val categories = listOf("Të Gjitha", "Kombëtare", "Popullore", "VIP", "Klasike")
    var selectedCategory by remember { mutableStateOf("Të Gjitha") }

    val filteredGifts = remember(selectedCategory, gifts) {
        if (selectedCategory == "Të Gjitha") {
            gifts
        } else {
            gifts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
    }

    // Default select UÇK or first gift
    var selectedGift by remember {
        mutableStateOf<VirtualGift?>(gifts.firstOrNull { it.isSpecialUck } ?: gifts.firstOrNull())
    }
    var sendCount by remember { mutableIntStateOf(1) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val totalCost = (selectedGift?.coins ?: 0) * sendCount
    val hasEnoughCoins = user.coins >= totalCost

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF14151B),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            // Header Row: Title & Coins Balance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Dhurata për ",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = recipientName,
                            color = TikTokRed,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Dërgo dhurata virtuale gjatë transmetimit Live",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp
                    )
                }

                // User coins chip with recharge button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF22242D))
                        .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                        .clickable { onRechargeClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🪙", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${user.coins}",
                        color = Color(0xFFFFD700),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(TikTokRed, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Recharge",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Categories Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isCatSelected = selectedCategory == category
                    val chipLabel = when (category) {
                        "Kombëtare" -> "🦅 Kombëtare (UÇK)"
                        "Popullore" -> "🔥 Popullore"
                        "VIP" -> "💎 VIP & Luksoze"
                        "Klasike" -> "🎁 Klasike"
                        else -> "⭐ Të Gjitha"
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isCatSelected) {
                                    if (category == "Kombëtare") Brush.horizontalGradient(listOf(Color(0xFFD32F2F), Color(0xFF8B0000)))
                                    else Brush.horizontalGradient(listOf(TikTokRed, Color(0xFFFF4B6E)))
                                } else {
                                    Brush.linearGradient(listOf(Color(0xFF20222C), Color(0xFF20222C)))
                                }
                            )
                            .border(
                                width = if (isCatSelected) 1.dp else 0.5.dp,
                                color = if (isCatSelected) Color.White.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                selectedCategory = category
                                errorMessage = null
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = chipLabel,
                            color = if (isCatSelected) Color.White else Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF3B1515))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFFF8A80),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Gifts Grid (Max height 280dp for comfortable scrolling)
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredGifts, key = { it.id }) { gift ->
                    val isSelected = selectedGift?.id == gift.id
                    val isUck = gift.isSpecialUck

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                when {
                                    isSelected && isUck -> Brush.verticalGradient(
                                        listOf(Color(0xFF420B0B), Color(0xFF280707))
                                    )
                                    isSelected -> Brush.verticalGradient(
                                        listOf(TikTokRed.copy(alpha = 0.28f), Color(0xFF20161C))
                                    )
                                    isUck -> Brush.verticalGradient(
                                        listOf(Color(0xFF2E0C0C), Color(0xFF1B0707))
                                    )
                                    else -> Brush.verticalGradient(
                                        listOf(Color(0xFF1F212A), Color(0xFF181A22))
                                    )
                                }
                            )
                            .border(
                                width = when {
                                    isSelected && isUck -> 2.dp
                                    isSelected -> 1.8.dp
                                    isUck -> 1.2.dp
                                    else -> 0.5.dp
                                },
                                brush = when {
                                    isSelected && isUck -> Brush.linearGradient(
                                        listOf(Color(0xFFFFD700), Color(0xFFFF8C00), Color(0xFFFFD700))
                                    )
                                    isSelected -> Brush.linearGradient(
                                        listOf(TikTokRed, Color(0xFFFF5252))
                                    )
                                    isUck -> Brush.linearGradient(
                                        listOf(Color(0xFFFFD700).copy(alpha = 0.8f), Color(0xFFD32F2F))
                                    )
                                    else -> Brush.linearGradient(
                                        listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.04f))
                                    )
                                },
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                selectedGift = gift
                                errorMessage = null
                            }
                            .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Badge Tag for UÇK or VIP
                            if (isUck) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFFFFD700), Color(0xFFFFA000))
                                            )
                                        )
                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "👑 LEGJENDË",
                                        color = Color.Black,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            // Icon: Either UÇK Custom Emblem or standard emoji
                            if (isUck) {
                                UckEmblemBadge(size = 46.dp)
                            } else {
                                Text(
                                    text = gift.emoji,
                                    fontSize = 30.sp,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = gift.name,
                                color = if (isUck) Color(0xFFFFE082) else Color.White,
                                fontSize = 11.sp,
                                fontWeight = if (isUck) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = "🪙", fontSize = 10.sp)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = if (gift.coins >= 1000) "%,d".format(gift.coins) else "${gift.coins}",
                                    color = if (isUck) Color(0xFFFFD700) else Color(0xFFFFD700).copy(alpha = 0.9f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Selected Gift Preview Banner & Combo Selector
            selectedGift?.let { gift ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (gift.isSpecialUck) Color(0xFF280B0B)
                            else Color(0xFF1E2028)
                        )
                        .border(
                            0.5.dp,
                            if (gift.isSpecialUck) Color(0xFFFFD700).copy(alpha = 0.5f)
                            else Color.White.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (gift.isSpecialUck) {
                            UckEmblemBadge(size = 28.dp)
                        } else {
                            Text(text = gift.emoji, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = gift.name,
                                    color = if (gift.isSpecialUck) Color(0xFFFFE082) else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (gift.isSpecialUck) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "🇦🇱", fontSize = 12.sp)
                                }
                            }
                            Text(
                                text = "Kosto: ${"%,d".format(gift.coins)} 🪙 x $sendCount = ${"%,d".format(totalCost)} 🪙",
                                color = if (hasEnoughCoins) Color(0xFFFFD700) else Color(0xFFFF8A80),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Multiplier / Combo Selector
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(1, 5, 10).forEach { multiplier ->
                            val isSelectedMultiplier = sendCount == multiplier
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelectedMultiplier) TikTokRed
                                        else Color.White.copy(alpha = 0.08f)
                                    )
                                    .clickable {
                                        sendCount = multiplier
                                        errorMessage = null
                                    }
                                    .padding(horizontal = 7.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "x$multiplier",
                                    color = if (isSelectedMultiplier) Color.White else Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row: Recharge & Send Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onRechargeClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(text = "🪙 Mbush Kuletën", color = Color.White, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val gift = selectedGift
                        if (gift != null) {
                            if (!hasEnoughCoins) {
                                errorMessage = if (gift.isSpecialUck) {
                                    "Ju duhen ${"%,d".format(totalCost)} monedha për dhuratën UÇK! Mbushni kuletën."
                                } else {
                                    "Nuk keni monedha të mjaftueshme (${"%,d".format(totalCost)} 🪙)! Mbushni kuletën."
                                }
                            } else {
                                val success = TikTokRepository.sendGift(gift, recipientName, count = sendCount)
                                if (success) {
                                    onGiftSent(gift)
                                    onDismiss()
                                } else {
                                    errorMessage = "Nuk keni monedha të mjaftueshme! Mbushni kuletën."
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedGift?.isSpecialUck == true) Color(0xFFC62828) else TikTokRed
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(0.65f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (selectedGift?.isSpecialUck == true) {
                            Text(text = "🦅 Dërgo UÇK (10,000 🪙)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        } else {
                            Text(
                                text = "Dërgo ${selectedGift?.emoji ?: ""} (x$sendCount)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
