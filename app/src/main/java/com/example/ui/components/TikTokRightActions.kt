package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.TikTokRepository
import com.example.data.VideoItem
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TikTokRightActions(
    video: VideoItem,
    isActive: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onSoundClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isFollowing by remember(video.authorHandle) { mutableStateOf(video.isFollowing) }
    val likeScale = remember { Animatable(1f) }
    val favScale = remember { Animatable(1f) }

    // Vinyl record continuous rotation
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Creator Avatar with Follow button
        Box(
            modifier = Modifier.size(54.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            AsyncImage(
                model = video.authorAvatar,
                contentDescription = video.authorName,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, Color.White, CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onProfileClick() },
                contentScale = ContentScale.Crop
            )

            // Red plus follow badge
            this@Column.AnimatedVisibility(
                visible = !isFollowing,
                exit = scaleOut(spring()) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(TikTokRed)
                        .clickable {
                            isFollowing = true
                            TikTokRepository.toggleFollow(video.authorHandle)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.offset(y = (-1).dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Like (Heart) Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                coroutineScope.launch {
                    likeScale.animateTo(1.4f, tween(120, easing = FastOutSlowInEasing))
                    likeScale.animateTo(1f, spring())
                }
                onLikeClick()
            }
        ) {
            Icon(
                imageVector = if (video.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (video.isLiked) TikTokRed else Color.White,
                modifier = Modifier
                    .size(38.dp)
                    .graphicsLayer {
                        scaleX = likeScale.value
                        scaleY = likeScale.value
                    }
                    .shadow(elevation = 8.dp, spotColor = Color.Black)
            )
            Text(
                text = TikTokRepository.formatCount(video.likesCount),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Comment Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onCommentClick() }
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Comments",
                tint = Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .shadow(elevation = 8.dp, spotColor = Color.Black)
            )
            Text(
                text = TikTokRepository.formatCount(video.commentsCount),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Bookmark / Favorite Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                coroutineScope.launch {
                    favScale.animateTo(1.35f, tween(120, easing = FastOutSlowInEasing))
                    favScale.animateTo(1f, spring())
                }
                onFavoriteClick()
            }
        ) {
            Icon(
                imageVector = if (video.isFavorited) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = "Favorite",
                tint = if (video.isFavorited) TikTokYellow else Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .graphicsLayer {
                        scaleX = favScale.value
                        scaleY = favScale.value
                    }
                    .shadow(elevation = 8.dp, spotColor = Color.Black)
            )
            Text(
                text = TikTokRepository.formatCount(video.favoritesCount),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Share Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onShareClick() }
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                tint = Color.White,
                modifier = Modifier
                    .size(34.dp)
                    .shadow(elevation = 8.dp, spotColor = Color.Black)
            )
            Text(
                text = TikTokRepository.formatCount(video.sharesCount),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Spinning Vinyl Disc with Floating Musical Notes
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(52.dp)
        ) {
            // Floating rising notes animation
            if (isActive) {
                FloatingMusicNotes()
            }

            // Vinyl disc
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .rotate(if (isActive) rotation else 0f)
                    .clip(CircleShape)
                    .background(Color(0xFF161616))
                    .border(2.dp, Color(0xFF2B2B2B), CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onSoundClick() },
                contentAlignment = Alignment.Center
            ) {
                // Vinyl grooves rings
                Canvas(modifier = Modifier.size(46.dp)) {
                    val radius = size.minDimension / 2f
                    drawCircle(color = Color(0xFF282828), radius = radius * 0.75f)
                    drawCircle(color = Color(0xFF1E1E1E), radius = radius * 0.65f)
                }

                // Center album thumbnail
                AsyncImage(
                    model = video.soundAlbumArt,
                    contentDescription = video.soundTitle,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun FloatingMusicNotes() {
    val transition = rememberInfiniteTransition(label = "notes")
    val offsetY by transition.animateFloat(
        initialValue = 0f,
        targetValue = -70f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offsetY"
    )
    val offsetX by transition.animateFloat(
        initialValue = 0f,
        targetValue = -35f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offsetX"
    )
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Text(
        text = "♪",
        color = Color.White.copy(alpha = alpha),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .offset(x = offsetX.dp, y = offsetY.dp)
            .graphicsLayer { rotationZ = -20f }
    )
}
