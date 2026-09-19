package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TikTokRepository
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkCard
import com.example.ui.theme.TikTokRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    onDismiss: () -> Unit,
    onOpenWallet: () -> Unit,
    onOpenAnalytics: () -> Unit,
    onLogout: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val user by TikTokRepository.currentUser.collectAsState()
    val lang by TikTokRepository.appLanguage.collectAsState()
    var isPrivate by remember { mutableStateOf(user.isPrivate) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokDarkCard,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cilësimet dhe Privatësia ⚙️",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Llogaria (Account)
            SettingsSectionTitle(title = "Llogaria (Account)")

            SettingsItemRow(
                icon = Icons.Default.AccountBalanceWallet,
                title = "Kuleta (Wallet)",
                subtitle = "${user.coins} Monedha • €${String.format("%.2f", user.diamondEarnings)} Fitim",
                iconTint = Color(0xFFFFD700),
                onClick = {
                    onDismiss()
                    onOpenWallet()
                }
            )

            SettingsItemRow(
                icon = Icons.Default.Analytics,
                title = "Paneli i Krijuesit & Analitika",
                subtitle = "Shikimet, Like-t dhe rritja e audiencës",
                iconTint = TikTokCyan,
                onClick = {
                    onDismiss()
                    onOpenAnalytics()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Privatësia (Privacy)
            SettingsSectionTitle(title = "Privatësia & Siguria")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Llogari Private", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("Vetëm ndjekësit e miratuar mund të shohin videot tuaja", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                    }
                }
                Switch(
                    checked = isPrivate,
                    onCheckedChange = {
                        isPrivate = TikTokRepository.togglePrivateAccount()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = TikTokRed
                    )
                )
            }

            SettingsItemRow(
                icon = Icons.Default.Security,
                title = "Komentet & Mesazhet",
                subtitle = "Filtro spam-in dhe lejo mesazhe nga miqtë",
                onClick = { }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section 3: Gjuha (Language)
            SettingsSectionTitle(title = "Gjuha & Pamja")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Gjuha e Aplikacionit", color = Color.White, fontSize = 14.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .background(if (lang == "sq") TikTokRed else Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .clickable { TikTokRepository.setLanguage("sq") }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("🇦🇱 Shqip", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .background(if (lang == "en") TikTokRed else Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .clickable { TikTokRepository.setLanguage("en") }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("🇬🇧 English", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 4: Dalja / Logout
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(12.dp))

            SettingsItemRow(
                icon = Icons.Default.Logout,
                title = "Dil nga Llogaria (Log Out)",
                subtitle = "Përfundo sesionin aktual",
                iconTint = TikTokRed,
                onClick = {
                    TikTokRepository.logout()
                    onDismiss()
                    onLogout()
                }
            )

            SettingsItemRow(
                icon = Icons.Default.Delete,
                title = "Fshij Llogarinë (Delete Account)",
                subtitle = "Fshirje e përhershme e të dhënave",
                iconTint = Color.Gray,
                onClick = {
                    TikTokRepository.logout()
                    onDismiss()
                    onLogout()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White.copy(alpha = 0.5f),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconTint: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color.White.copy(alpha = 0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }
    }
}
