package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.components.BottomTab
import com.example.ui.components.TikTokBottomNav
import com.example.ui.screens.CreateVideoScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.InboxScreen
import com.example.ui.screens.LiveStreamScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.TikTokDarkCard
import com.example.ui.theme.TikTokRed

@Composable
fun TikTokApp() {
    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }
    var isCreateOpen by remember { mutableStateOf(false) }
    var isLiveOpen by remember { mutableStateOf(false) }
    var initialSoundForCreate by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Tab Content
        when (selectedTab) {
            BottomTab.HOME -> {
                FeedScreen(
                    onLiveClick = { isLiveOpen = true },
                    onSearchClick = { selectedTab = BottomTab.FRIENDS },
                    onProfileClick = { selectedTab = BottomTab.PROFILE },
                    onCreateVideoWithSound = { soundTitle ->
                        initialSoundForCreate = soundTitle
                        isCreateOpen = true
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            BottomTab.FRIENDS -> {
                DiscoverScreen(
                    onVideoClick = { selectedTab = BottomTab.HOME },
                    modifier = Modifier.fillMaxSize()
                )
            }
            BottomTab.CREATE -> {
                // Handled as fullscreen modal
            }
            BottomTab.INBOX -> {
                InboxScreen(modifier = Modifier.fillMaxSize())
            }
            BottomTab.PROFILE -> {
                ProfileScreen(
                    onVideoClick = { selectedTab = BottomTab.HOME },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Bottom Navigation Bar (Hidden when camera is recording/open or in Live)
        if (!isCreateOpen && !isLiveOpen) {
            TikTokBottomNav(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    if (tab == BottomTab.CREATE) {
                        initialSoundForCreate = null
                        isCreateOpen = true
                    } else {
                        selectedTab = tab
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // Fullscreen Create / Camera Studio
        AnimatedVisibility(
            visible = isCreateOpen,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            CreateVideoScreen(
                initialSound = initialSoundForCreate,
                onClose = { isCreateOpen = false },
                onVideoCreated = {
                    isCreateOpen = false
                    selectedTab = BottomTab.HOME
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Fullscreen Live Stream Screen
        AnimatedVisibility(
            visible = isLiveOpen,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            LiveStreamScreen(
                onClose = { isLiveOpen = false },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
