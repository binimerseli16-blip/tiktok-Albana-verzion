package com.example.ui.components

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed

enum class BottomTab {
    HOME,
    FRIENDS,
    CREATE,
    INBOX,
    PROFILE
}

@Composable
fun TikTokBottomNav(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(
            color = Color.White.copy(alpha = 0.12f),
            thickness = 0.5.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home
            NavItem(
                title = "Home",
                selected = selectedTab == BottomTab.HOME,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                onClick = { onTabSelected(BottomTab.HOME) }
            )

            // 2. Friends
            NavItem(
                title = "Friends",
                selected = selectedTab == BottomTab.FRIENDS,
                selectedIcon = Icons.Filled.People,
                unselectedIcon = Icons.Outlined.People,
                badge = "NEW",
                onClick = { onTabSelected(BottomTab.FRIENDS) }
            )

            // 3. Iconic TikTok Create (+) Button
            TikTokCreateButton(
                onClick = { onTabSelected(BottomTab.CREATE) }
            )

            // 4. Inbox
            NavItem(
                title = "Inbox",
                selected = selectedTab == BottomTab.INBOX,
                selectedIcon = Icons.Filled.Mail,
                unselectedIcon = Icons.Outlined.Mail,
                badgeCount = 3,
                onClick = { onTabSelected(BottomTab.INBOX) }
            )

            // 5. Profile
            NavItem(
                title = "Profile",
                selected = selectedTab == BottomTab.PROFILE,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                onClick = { onTabSelected(BottomTab.PROFILE) }
            )
        }
    }
}

@Composable
fun NavItem(
    title: String,
    selected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    badge: String? = null,
    badgeCount: Int? = null,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 12.dp, vertical = 2.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = if (selected) selectedIcon else unselectedIcon,
                contentDescription = title,
                tint = if (selected) Color.White else Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )

            // Optional red notification badge
            if (badgeCount != null && badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = 8.dp, y = (-4).dp)
                        .clip(CircleShape)
                        .background(TikTokRed)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (badge != null) {
                Box(
                    modifier = Modifier
                        .offset(x = 10.dp, y = (-4).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(TikTokCyan)
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badge,
                        color = Color.Black,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = title,
            color = if (selected) Color.White else Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun TikTokCreateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 46.dp, height = 30.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Cyan layer on left
        Box(
            modifier = Modifier
                .offset(x = (-3).dp)
                .size(width = 38.dp, height = 30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TikTokCyan)
        )

        // Magenta/Red layer on right
        Box(
            modifier = Modifier
                .offset(x = 3.dp)
                .size(width = 38.dp, height = 30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TikTokRed)
        )

        // White primary button in center
        Box(
            modifier = Modifier
                .size(width = 38.dp, height = 30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
