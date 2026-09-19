package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.BattleParticipant
import com.example.data.LiveBattleState
import com.example.data.TopGifter
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed

/**
 * Top Battle Header displaying Red vs Blue animated score bar,
 * central glowing VS swords badge, countdown timer, speed-up indicator,
 * and top 3 contributors for each team.
 */
@Composable
fun LiveBattleScoreHeader(
    battleState: LiveBattleState,
    onBattleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalScore = (battleState.redTeamScore + battleState.blueTeamScore).coerceAtLeast(1)
    val redFraction = (battleState.redTeamScore.toFloat() / totalScore.toFloat()).coerceIn(0.12f, 0.88f)
    val animatedRedFraction by animateFloatAsState(
        targetValue = redFraction,
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "red_fraction"
    )

    val minutes = battleState.timeRemainingSeconds / 60
    val seconds = battleState.timeRemainingSeconds % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_vs")
    val vsScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vs_scale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // Top Gifters Row & Timer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Red Team Top Gifters
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val redLeader = battleState.redParticipants.firstOrNull()
                redLeader?.topGifters?.take(3)?.forEach { gifter ->
                    GifterAvatarBadge(gifter = gifter, teamColor = TikTokRed)
                }
            }

            // Central Timer & Speed-Up Badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xCC000000))
                    .clickable { onBattleClick() }
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (battleState.isSpeedUpActive) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Speed Up",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Text(
                        text = if (battleState.isPunishmentPeriod) "DËNIMI" else formattedTime,
                        color = if (battleState.isSpeedUpActive) Color(0xFFFFD700) else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                if (battleState.isSpeedUpActive) {
                    Text(
                        text = "x2 PIKË 🔥",
                        color = Color(0xFFFF5252),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Blue Team Top Gifters
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val blueLeader = battleState.blueParticipants.firstOrNull()
                blueLeader?.topGifters?.take(3)?.forEach { gifter ->
                    GifterAvatarBadge(gifter = gifter, teamColor = Color(0xFF2979FF))
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Red vs Blue Dynamic Battle Bar with central VS Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF14151B))
                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
        ) {
            // Split progress bar
            Row(modifier = Modifier.fillMaxSize()) {
                // Red Score Segment
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(animatedRedFraction)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFFF1744), Color(0xFFD50000))
                            )
                        )
                        .padding(start = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "%,d".format(battleState.redTeamScore),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Blue Score Segment
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f - animatedRedFraction)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF0091EA), Color(0xFF2979FF))
                            )
                        )
                        .padding(end = 10.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "%,d".format(battleState.blueTeamScore),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Central VS Emblem
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = vsScale
                        scaleY = vsScale
                    }
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFFFD700), Color(0xFFFF6D00), Color(0xFFB71C1C))
                        )
                    )
                    .border(1.5.dp, Color.White, CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VS",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

/**
 * Contributor MVP circular badge for battle gifters
 */
@Composable
private fun GifterAvatarBadge(
    gifter: TopGifter,
    teamColor: Color
) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .border(1.2.dp, teamColor, CircleShape)
    ) {
        AsyncImage(
            model = gifter.avatar,
            contentDescription = gifter.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Rank pill
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(11.dp)
                .clip(CircleShape)
                .background(
                    when (gifter.rank) {
                        1 -> Color(0xFFFFD700)
                        2 -> Color(0xFFE0E0E0)
                        else -> Color(0xFFCD7F32)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${gifter.rank}",
                color = Color.Black,
                fontSize = 7.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

/**
 * Individual Camera Feed Box for Battle Participants.
 * Features realistic creator video simulation, team border glow,
 * audio/microphone indicator, individual score badge, and tap interaction.
 */
@Composable
fun BattleCameraFeedBox(
    participant: BattleParticipant,
    onTapCheer: () -> Unit,
    onMuteToggle: () -> Unit,
    modifier: Modifier = Modifier,
    isTeamRed: Boolean = true
) {
    val borderColor = if (isTeamRed) TikTokRed else Color(0xFF2979FF)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F1117))
            .border(1.5.dp, borderColor.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures {
                    onTapCheer()
                }
            }
    ) {
        // Atmospheric stream background visual for participant
        AsyncImage(
            model = participant.avatar,
            contentDescription = participant.name,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.85f },
            contentScale = ContentScale.Crop
        )

        // Subtle gradient overlay for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.65f)
                        )
                    )
                )
        )

        // Top Participant Info Pill
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x88000000))
                .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isTeamRed) TikTokRed else Color(0xFF2979FF))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = participant.name,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Top-right Microphone / Mute Button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color(0x88000000))
                .clickable { onMuteToggle() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (participant.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                contentDescription = "Mute",
                tint = if (participant.isMuted) Color(0xFFFF5252) else Color.White,
                modifier = Modifier.size(13.dp)
            )
        }

        // Bottom Score & Likes Pill
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x99000000))
                .padding(horizontal = 7.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🪙", fontSize = 10.sp)
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "%,d".format(participant.score),
                color = Color(0xFFFFD700),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Tap hint indicator on bottom right
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(6.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x66000000))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "👆 Tap +1",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * End of Battle Banner announcing winner or punishment
 */
@Composable
fun BattleWinnerPunishmentBanner(
    battleState: LiveBattleState,
    onRematchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRedWinner = battleState.winnerTeam == "RED"
    val isDraw = battleState.winnerTeam == "DRAW"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    if (isDraw) listOf(Color(0xFF424242), Color(0xFF212121))
                    else if (isRedWinner) listOf(Color(0xFF8B0000), Color(0xFFB71C1C), Color(0xFF2E0707))
                    else listOf(Color(0xFF0D47A1), Color(0xFF1565C0), Color(0xFF092042))
                )
            )
            .border(
                1.5.dp,
                if (isDraw) Color.White.copy(alpha = 0.3f) else Color(0xFFFFD700),
                RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when {
                    isDraw -> "🤝 BARAZIM NË BETEJË! 🤝"
                    isRedWinner -> "🏆 FITORE PËR SKUADRËN E KUQE! 🔴"
                    else -> "🏆 FITORE PËR SKUADRËN BLU! 🔵"
                },
                color = Color(0xFFFFD700),
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Dënimi i Humbësit: ${battleState.punishment}",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFD700))
                        .clickable { onRematchClick() }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "⚔️ Revanç (Rematch)",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
