package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokRed

enum class FeedTab {
    FOLLOWING,
    FOR_YOU
}

@Composable
fun TikTokTopBar(
    selectedTab: FeedTab,
    onTabSelected: (FeedTab) -> Unit,
    onLiveClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // LIVE button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onLiveClick() }
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LiveTv,
                contentDescription = "LIVE",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(TikTokRed)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "LIVE",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Center Tabs: Following | For You
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Following Tab
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onTabSelected(FeedTab.FOLLOWING) }
            ) {
                Text(
                    text = "Following",
                    color = if (selectedTab == FeedTab.FOLLOWING) Color.White else Color.White.copy(alpha = 0.6f),
                    fontSize = if (selectedTab == FeedTab.FOLLOWING) 17.sp else 16.sp,
                    fontWeight = if (selectedTab == FeedTab.FOLLOWING) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(28.dp)
                        .background(
                            if (selectedTab == FeedTab.FOLLOWING) Color.White else Color.Transparent,
                            RoundedCornerShape(1.dp)
                        )
                )
            }

            // For You Tab
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onTabSelected(FeedTab.FOR_YOU) }
            ) {
                Text(
                    text = "For You",
                    color = if (selectedTab == FeedTab.FOR_YOU) Color.White else Color.White.copy(alpha = 0.6f),
                    fontSize = if (selectedTab == FeedTab.FOR_YOU) 17.sp else 16.sp,
                    fontWeight = if (selectedTab == FeedTab.FOR_YOU) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(28.dp)
                        .background(
                            if (selectedTab == FeedTab.FOR_YOU) Color.White else Color.Transparent,
                            RoundedCornerShape(1.dp)
                        )
                )
            }
        }

        // Search icon
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
