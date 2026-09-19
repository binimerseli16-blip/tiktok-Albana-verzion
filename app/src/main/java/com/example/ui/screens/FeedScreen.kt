package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.TikTokRepository
import com.example.data.VideoItem
import com.example.ui.components.CommentBottomSheet
import com.example.ui.components.FeedTab
import com.example.ui.components.ShareBottomSheet
import com.example.ui.components.SoundDetailSheet
import com.example.ui.components.TikTokBottomOverlay
import com.example.ui.components.TikTokRightActions
import com.example.ui.components.TikTokTopBar
import com.example.ui.components.VideoPlayerItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedScreen(
    onLiveClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCreateVideoWithSound: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val allVideos by TikTokRepository.videos.collectAsState()
    var selectedTab by remember { mutableStateOf(FeedTab.FOR_YOU) }

    // Filter by tab if "Following" vs "For You"
    val displayedVideos = remember(selectedTab, allVideos) {
        if (selectedTab == FeedTab.FOLLOWING) {
            val followed = allVideos.filter { it.isFollowing }
            if (followed.isNotEmpty()) followed else allVideos
        } else {
            allVideos
        }
    }

    val pagerState = rememberPagerState(pageCount = { displayedVideos.size })

    // Active bottom sheets
    var activeCommentVideo by remember { mutableStateOf<VideoItem?>(null) }
    var activeShareVideo by remember { mutableStateOf<VideoItem?>(null) }
    var activeSoundVideo by remember { mutableStateOf<VideoItem?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        // Vertical Pager (TikTok infinite vertical feed)
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { index -> displayedVideos.getOrNull(index)?.id ?: index }
        ) { pageIndex ->
            val video = displayedVideos[pageIndex]
            val isCurrentPage = pagerState.currentPage == pageIndex

            Box(modifier = Modifier.fillMaxSize()) {
                // Video Player with double-tap like heart burst
                VideoPlayerItem(
                    video = video,
                    isActive = isCurrentPage,
                    onDoubleTapLike = {
                        TikTokRepository.likeVideo(video.id)
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Bottom Left Video Details (Handle, Caption, Audio Ticker)
                TikTokBottomOverlay(
                    video = video,
                    onSoundClick = { activeSoundVideo = video },
                    onProfileClick = onProfileClick,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 58.dp)
                )

                // Right Action Column (Avatar, Like, Comments, Bookmark, Share, Vinyl Disc)
                TikTokRightActions(
                    video = video,
                    isActive = isCurrentPage,
                    onLikeClick = { TikTokRepository.toggleLike(video.id) },
                    onCommentClick = { activeCommentVideo = video },
                    onFavoriteClick = { TikTokRepository.toggleFavorite(video.id) },
                    onShareClick = { activeShareVideo = video },
                    onSoundClick = { activeSoundVideo = video },
                    onProfileClick = onProfileClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 10.dp, bottom = 62.dp)
                )
            }
        }

        // Top Navigation Bar (LIVE | Following - For You | Search)
        TikTokTopBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            onLiveClick = onLiveClick,
            onSearchClick = onSearchClick,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Modal Sheets
        activeCommentVideo?.let { video ->
            // Re-fetch updated video state for real-time comment additions
            val currentVid = allVideos.find { it.id == video.id } ?: video
            CommentBottomSheet(
                video = currentVid,
                onDismiss = { activeCommentVideo = null }
            )
        }

        activeShareVideo?.let { video ->
            ShareBottomSheet(
                video = video,
                onDismiss = { activeShareVideo = null }
            )
        }

        activeSoundVideo?.let { video ->
            SoundDetailSheet(
                video = video,
                onUseSound = {
                    onCreateVideoWithSound(video.soundTitle)
                },
                onDismiss = { activeSoundVideo = null }
            )
        }
    }
}
