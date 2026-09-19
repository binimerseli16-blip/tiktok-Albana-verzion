package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.TikTokRepository
import com.example.data.VideoItem
import com.example.ui.components.AuthSheet
import com.example.ui.components.CreatorAnalyticsDialog
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.SettingsSheet
import com.example.ui.components.WalletDialog
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkCard
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokTextSecondary

@Composable
fun ProfileScreen(
    onVideoClick: (VideoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val user by TikTokRepository.currentUser.collectAsState()
    val allVideos by TikTokRepository.videos.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var showEditProfile by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var showWallet by remember { mutableStateOf(false) }
    var showAnalytics by remember { mutableStateOf(false) }
    var showAuth by remember { mutableStateOf(false) }

    val tabIcons = listOf(
        Icons.Default.GridOn,
        Icons.Default.Lock,
        Icons.Default.Bookmark,
        Icons.Default.Favorite,
        Icons.Default.Repeat
    )

    // Filter videos depending on tab:
    // 0 -> My Videos, 1 -> Private, 2 -> Favorites, 3 -> Liked, 4 -> Reposts
    val displayedVideos = remember(selectedTabIndex, allVideos) {
        when (selectedTabIndex) {
            0 -> allVideos
            1 -> emptyList()
            2 -> allVideos.filter { it.isFavorited }
            3 -> allVideos.filter { it.isLiked }
            4 -> allVideos.filter { it.isReposted }
            else -> allVideos
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Profile Header spanning all 3 columns
        item(span = { GridItemSpan(3) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Wallet Quick Action
                    Row(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                            .clickable { showWallet = true }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${user.coins}",
                            color = Color(0xFFFFD700),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.username,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (user.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = TikTokCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Avatar
                Box(contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = user.username,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                            .clickable { showEditProfile = true },
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(TikTokCyan)
                            .clickable { showEditProfile = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit photo",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // User Handle
                Text(
                    text = user.handle,
                    color = TikTokTextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row: Following | Followers | Likes
                Row(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatColumn(count = user.followingCount, label = "Following")
                    StatColumn(count = user.followersCount, label = "Followers")
                    StatColumn(count = user.likesCount, label = "Likes")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons: Edit profile | Share profile | Wallet
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showEditProfile = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokDarkCard)
                    ) {
                        Text(
                            text = "Ndrysho Profilin",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = { showAnalytics = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokDarkCard)
                    ) {
                        Text(
                            text = "Analitika 📊",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(TikTokDarkCard)
                            .clickable { showWallet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Wallet",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bio
                Text(
                    text = user.bio,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 28.dp)
                )

                // Website Link
                if (user.website.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = "Website",
                            tint = TikTokCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user.website,
                            color = TikTokCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Video Tabs (Grid, Lock, Bookmark, Heart, Repost)
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color.White,
                            height = 2.dp
                        )
                    },
                    divider = {
                        HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 0.5.dp)
                    }
                ) {
                    tabIcons.forEachIndexed { index, icon ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (selectedTabIndex == index) Color.White else Color.White.copy(alpha = 0.4f),
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            modifier = Modifier.height(48.dp)
                        )
                    }
                }
            }
        }

        // 3-Column Video Thumbnail Grid
        if (displayedVideos.isEmpty()) {
            item(span = { GridItemSpan(3) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = tabIcons[selectedTabIndex],
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.25f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (selectedTabIndex) {
                            1 -> "Videot private janë të fshehura 🔒"
                            2 -> "Nuk keni ende video të ruajtura 🔖"
                            3 -> "Nuk keni pëlqyer ende asnjë video ❤️"
                            4 -> "Nuk keni bërë ende Repost 🔁"
                            else -> "Nuk keni postuar video ende"
                        },
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(displayedVideos, key = { it.id }) { video ->
                ProfileVideoThumbnail(video = video, onClick = { onVideoClick(video) })
            }
        }
    }

    if (showEditProfile) {
        EditProfileDialog(onDismiss = { showEditProfile = false })
    }

    if (showSettings) {
        SettingsSheet(
            onDismiss = { showSettings = false },
            onOpenWallet = { showWallet = true },
            onOpenAnalytics = { showAnalytics = true },
            onLogout = { showAuth = true }
        )
    }

    if (showWallet) {
        WalletDialog(onDismiss = { showWallet = false })
    }

    if (showAnalytics) {
        CreatorAnalyticsDialog(onDismiss = { showAnalytics = false })
    }

    if (showAuth) {
        AuthSheet(
            onDismiss = { showAuth = false },
            onSuccess = { showAuth = false }
        )
    }
}

@Composable
fun StatColumn(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = TikTokTextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ProfileVideoThumbnail(
    video: VideoItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .background(Color(video.primaryColor))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = video.soundAlbumArt,
            contentDescription = video.caption,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient at bottom for text
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0x99000000)),
                        startY = 120f
                    )
                )
        )

        // View count overlay on bottom-left of each video
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = TikTokRepository.formatCount(video.likesCount),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
