package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.BattleMode
import com.example.data.BattleParticipant
import com.example.data.LiveBattleState
import com.example.data.LiveGoal
import com.example.data.LiveGuestRequest
import com.example.data.TikTokRepository
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkCard
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokRed

/**
 * Bottom Sheet for configuring and starting real TikTok PK Battles (VS).
 * Supports 1 vs 1, 2 vs 1 (2 eta 1), 2 vs 2, and Solo modes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleOptionsBottomSheet(
    battleState: LiveBattleState,
    onDismiss: () -> Unit,
    onSelectMode: (BattleMode) -> Unit,
    onStartBattle: (durationSeconds: Int, punishment: String) -> Unit,
    onEndBattle: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedDuration by remember { mutableIntStateOf(180) } // 3 min default
    var selectedPunishment by remember { mutableStateOf(battleState.punishment) }
    var customPunishmentInput by remember { mutableStateOf("") }

    val punishmentPresets = listOf(
        "20 Pompa në dysheme 💪",
        "Pikturo fytyrën me buzëkuq 💄",
        "Këndo këngë popullore pa muzikë 🎤",
        "Pi 1 gotë ujë me kripë pa u ndalur 🥛",
        "Mbaj 1 vezë në ballë për 1 minutë 🥚"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokDarkCard,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SportsKabaddi,
                        contentDescription = "Battle",
                        tint = TikTokRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Beteja LIVE (VS / PK) ⚔️",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mode Selector Cards (1vs1, 2vs1, 2vs2, Solo)
            Text(
                text = "Zgjidh Formatin e Betejës:",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BattleMode.values().forEach { mode ->
                    val isSelected = battleState.mode == mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) Brush.verticalGradient(listOf(TikTokRed, Color(0xFF990022)))
                                else Brush.verticalGradient(listOf(Color(0xFF232533), Color(0xFF191B26)))
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onSelectMode(mode) }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = mode.shortLabel,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = when (mode) {
                                    BattleMode.ONE_VS_ONE -> "2 Lojtarë"
                                    BattleMode.TWO_VS_ONE -> "2 vs 1"
                                    BattleMode.TWO_VS_TWO -> "4 Lojtarë"
                                    BattleMode.SOLO -> "Vetëm"
                                },
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Matchup Preview
            Text(
                text = "Pjesëmarrësit në Ekran:",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF191B26))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Red Side Creators
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("Skuadra e Kuqe 🔴", color = TikTokRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        battleState.redParticipants.forEach { p ->
                            AsyncImage(
                                model = p.avatar,
                                contentDescription = p.name,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, TikTokRed, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // Central VS
                Text(
                    text = "⚔️ VS ⚔️",
                    color = Color(0xFFFFD700),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )

                // Blue Side Creators
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("Skuadra Blu 🔵", color = Color(0xFF2979FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    if (battleState.blueParticipants.isEmpty()) {
                        Text("Pa kundërshtar", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            battleState.blueParticipants.forEach { p ->
                                AsyncImage(
                                    model = p.avatar,
                                    contentDescription = p.name,
                                    modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, Color(0xFF2979FF), CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration Selector
            Text(
                text = "Kohëzgjatja e Betejës:",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    Pair(180, "3 Minuta (Standard)"),
                    Pair(300, "5 Minuta"),
                    Pair(600, "10 Minuta")
                ).forEach { (seconds, label) ->
                    val isSel = selectedDuration == seconds
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSel) TikTokRed else Color(0xFF232533))
                            .clickable { selectedDuration = seconds }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = label,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Punishment Section
            Text(
                text = "Dënimi i Betejës (Punishment):",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                punishmentPresets.forEach { preset ->
                    val isSel = selectedPunishment == preset
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) Color.White.copy(alpha = 0.15f) else Color(0xFF1F212C))
                            .clickable { selectedPunishment = preset }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isSel) "✅ " else "▫️ ",
                            fontSize = 12.sp
                        )
                        Text(
                            text = preset,
                            color = if (isSel) Color(0xFFFFD700) else Color.White,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons: Start or End
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (battleState.isActive) {
                    Button(
                        onClick = {
                            onEndBattle()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Ndalo Betejën 🛑", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = {
                        onStartBattle(selectedDuration, selectedPunishment)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text(
                        text = if (battleState.isActive) "Rifillo Betejën ⚔️" else "Fillo Betejën Tani 🚀",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Multi-Guest sheet to invite, accept and connect co-hosts into the live.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveMultiGuestBottomSheet(
    guestRequests: List<LiveGuestRequest>,
    onDismiss: () -> Unit,
    onAcceptGuest: (String) -> Unit,
    onRejectGuest: (String) -> Unit,
    onInviteParticipant: (BattleParticipant) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokDarkCard,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lidhja me Mysafirë (Multi-Guest) 👥",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Kërkesat për t'u futur në Kamera:",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (guestRequests.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nuk ka kërkesa të reja për momentin", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    guestRequests.forEach { req ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1C1E2A))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = req.avatar,
                                    contentDescription = req.name,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(req.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("${req.handle} • ${req.followers} ndjekës", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.1f))
                                        .clickable { onRejectGuest(req.id) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text("Refuzo", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(TikTokCyan)
                                        .clickable { onAcceptGuest(req.id) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("Prano në Live", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Krijuesit Aktivë gati për PK / VS:",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(TikTokRepository.availableCreators) { creator ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF191B26))
                            .clickable { onInviteParticipant(creator) }
                            .padding(10.dp)
                    ) {
                        AsyncImage(
                            model = creator.avatar,
                            contentDescription = creator.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(1.dp, TikTokRed, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(creator.name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("+ Fto në Betejë", color = TikTokCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Creator Tools & Live Settings Sheet:
 * Beauty filters, Sound effects, Live goal, Flip camera, Audio toggle.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveToolsBottomSheet(
    liveGoal: LiveGoal,
    isMuted: Boolean,
    onDismiss: () -> Unit,
    onToggleMute: () -> Unit,
    onFlipCamera: () -> Unit,
    onPlaySoundEffect: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedFilter by remember { mutableStateOf("Glow Studio ✨") }
    var filterIntensity by remember { mutableStateOf(0.8f) }
    var filterBadWords by remember { mutableStateOf(true) }

    val filterOptions = listOf("Natyral 🌿", "Glow Studio ✨", "Shqip Kuq e Zi 🦅", "Vintage Retro 📼", "Ultra HD 🌟")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokDarkCard,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mjetet e Transmetuesit ⚙️",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Hardware Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Flip camera
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1F212C))
                        .clickable { onFlipCamera() }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip", tint = TikTokCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ndërro Kamerën", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Mic toggle
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isMuted) Color(0xFF551111) else Color(0xFF1F212C))
                        .clickable { onToggleMute() }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mic",
                            tint = if (isMuted) TikTokRed else Color(0xFF00E676),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isMuted) "Mikrofoni OFF" else "Mikrofoni ON", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Live Goal Preview
            Text(
                text = "🎯 ${liveGoal.title}:",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1F212C))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${liveGoal.giftEmoji} ${liveGoal.giftName}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("${liveGoal.currentCount} / ${liveGoal.targetCount}", color = Color(0xFFFFD700), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    // Progress bar
                    val prog = (liveGoal.currentCount.toFloat() / liveGoal.targetCount.toFloat()).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(prog)
                                .fillMaxHeight()
                                .background(Brush.horizontalGradient(listOf(TikTokRed, Color(0xFFFFD700))))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Beauty Filters
            Text(
                text = "✨ Filtra & Efekte Bukurie:",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filterOptions) { filter ->
                    val isSel = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSel) TikTokRed else Color(0xFF1F212C))
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(filter, color = Color.White, fontSize = 11.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PK Soundboard Effects
            Text(
                text = "🔊 Tinguj PK & Reagime:",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Pair("🔔 Gong Fillimi", "gong"),
                    Pair("🎺 Fitore Horn", "horn"),
                    Pair("👏 Duartrokitje", "applause"),
                    Pair("🚨 Sirena PK", "siren")
                ).forEach { (name, effect) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF232636))
                            .clickable { onPlaySoundEffect(name) }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(name, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Moderation Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1F212C))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("🛡️ Filtro Komentet Fyese", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Blloko automatikisht fjalët e papërshtatshme në chat", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                }
                Switch(
                    checked = filterBadWords,
                    onCheckedChange = { filterBadWords = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TikTokRed)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
