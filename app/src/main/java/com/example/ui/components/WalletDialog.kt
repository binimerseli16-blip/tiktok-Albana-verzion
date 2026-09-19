package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.CoinPurchaseRecord
import com.example.data.TikTokRepository
import com.example.data.WithdrawalRecord
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkCard
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class CoinPackage(val coins: Long, val priceEur: Double)

@Composable
fun WalletDialog(
    onDismiss: () -> Unit
) {
    val user by TikTokRepository.currentUser.collectAsState()
    val withdrawalHistory by TikTokRepository.withdrawalHistory.collectAsState()
    val purchaseHistory by TikTokRepository.purchaseHistory.collectAsState()
    var activeTab by remember { mutableIntStateOf(1) } // 0: Recharge, 1: Real Withdrawal, 2: History
    var coinTabSubSection by remember { mutableIntStateOf(0) } // 0: Packages, 1: Invoices
    var purchaseSuccessMessage by remember { mutableStateOf<String?>(null) }
    var selectedRecordForDetails by remember { mutableStateOf<WithdrawalRecord?>(null) }
    var selectedPackageForPurchase by remember { mutableStateOf<CoinPackage?>(null) }
    var selectedPurchaseInvoice by remember { mutableStateOf<CoinPurchaseRecord?>(null) }

    val coinPackages = listOf(
        CoinPackage(70, 0.99),
        CoinPackage(350, 4.99),
        CoinPackage(700, 9.99),
        CoinPackage(1400, 19.99),
        CoinPackage(3500, 49.99),
        CoinPackage(10000, 119.99),
        CoinPackage(25000, 249.99)
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = TikTokDarkCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Top Title & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(Color(0xFFFFD700).copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🪙", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Kuleta e TikTok",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Financat & Tërheqjet e Krijuesit",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Balances Overview Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Coins Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White.copy(alpha = 0.06f), shape = RoundedCornerShape(12.dp))
                            .clickable { activeTab = 0 }
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("Monedha (Coins)", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🪙", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${user.coins}",
                                    color = Color(0xFFFFD700),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Creator Diamond Earnings (Real Cash Available)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (activeTab == 1) TikTokCyan.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.06f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (activeTab == 1) 1.dp else 0.5.dp,
                                color = if (activeTab == 1) TikTokCyan else Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { activeTab = 1 }
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Fitimet (Tërheqje)", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF00E676), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("AKTIVE", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💎", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "€${String.format("%.2f", user.diamondEarnings)}",
                                    color = TikTokCyan,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Section Navigation Tabs
                TabRow(
                    selectedTabIndex = activeTab,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                            color = if (activeTab == 1) TikTokCyan else TikTokRed,
                            height = 2.dp
                        )
                    },
                    divider = {
                        HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                    }
                ) {
                    Tab(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        text = {
                            Text(
                                text = "🏦 Tërheqja",
                                fontSize = 12.sp,
                                fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeTab == 1) Color.White else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    )
                    Tab(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        text = {
                            Text(
                                text = "🪙 Bli Monedha",
                                fontSize = 12.sp,
                                fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeTab == 0) Color.White else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    )
                    Tab(
                        selected = activeTab == 2,
                        onClick = { activeTab = 2 },
                        text = {
                            Text(
                                text = "📜 Historiku (${withdrawalHistory.size})",
                                fontSize = 12.sp,
                                fontWeight = if (activeTab == 2) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeTab == 2) Color.White else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Content Views based on activeTab
                when (activeTab) {
                    1 -> {
                        // Real Withdrawal View
                        RealWithdrawalForm(
                            availableEarnings = user.diamondEarnings,
                            onWithdrawSubmitted = { record ->
                                selectedRecordForDetails = record
                                activeTab = 2 // Move to history
                            }
                        )
                    }
                    0 -> {
                        // Coin Recharge & Real Purchase View
                        Column(modifier = Modifier.height(330.dp)) {
                            // Sub-navigation for Packages vs Invoices
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White.copy(alpha = 0.06f), RoundedCornerShape(10.dp))
                                    .padding(3.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (coinTabSubSection == 0) TikTokRed else Color.Transparent)
                                        .clickable { coinTabSubSection = 0 }
                                        .padding(vertical = 7.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "🪙 Paketat e Monedhave",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (coinTabSubSection == 1) TikTokRed else Color.Transparent)
                                        .clickable { coinTabSubSection = 1 }
                                        .padding(vertical = 7.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "🧾 Faturat e Blerjes (${purchaseHistory.size})",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (coinTabSubSection == 0) {
                                // Security banner
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF1B2838), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Blerje 100% Zyrtare: Trust Wallet • Kartë • PayPal • Bankë",
                                        color = Color(0xFF00E676),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    items(coinPackages) { pkg ->
                                        val badgeText = when (pkg.coins) {
                                            350L -> "🔥 Më e Shitura"
                                            700L -> "⚡ +5% Bonus"
                                            1400L -> "💎 +10% Bonus"
                                            3500L -> "👑 Paketa VIP"
                                            10000L -> "🦅 Paketa UÇK (10k)"
                                            25000L -> "🌟 Ultra Royal"
                                            else -> null
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(TikTokDarkSurface)
                                                .border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                                .clickable {
                                                    selectedPackageForPurchase = pkg
                                                }
                                                .padding(horizontal = 12.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .background(Color(0xFFFFD700).copy(alpha = 0.15f), CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text("🪙", fontSize = 18.sp)
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = "${pkg.coins} Monedha",
                                                            color = Color.White,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        if (badgeText != null) {
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Box(
                                                                modifier = Modifier
                                                                    .background(TikTokRed.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                                                            ) {
                                                                Text(badgeText, color = TikTokRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                                            }
                                                        }
                                                    }
                                                    Text(
                                                        text = "Rimbushje e menjëhershme",
                                                        color = Color.White.copy(alpha = 0.5f),
                                                        fontSize = 10.sp
                                                    )
                                                }
                                            }

                                            Button(
                                                onClick = { selectedPackageForPurchase = pkg },
                                                colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                                                shape = RoundedCornerShape(20.dp),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                                modifier = Modifier.height(34.dp)
                                            ) {
                                                Text(
                                                    text = "€${String.format(java.util.Locale.US, "%.2f", pkg.priceEur)}",
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Purchase Invoices List
                                if (purchaseHistory.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                                        Text("Nuk keni ende asnjë faturë blerjeje.", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                                    }
                                } else {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        items(purchaseHistory) { invoice ->
                                            val isCrypto = invoice.cryptoAmount.isNotBlank() || invoice.method.contains("Trust Wallet", ignoreCase = true)
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(TikTokDarkSurface)
                                                    .clickable { selectedPurchaseInvoice = invoice }
                                                    .padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(36.dp)
                                                            .background(
                                                                if (isCrypto) Color(0xFF00E676).copy(alpha = 0.15f) else Color(0xFFFFD700).copy(alpha = 0.15f),
                                                                CircleShape
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(if (isCrypto) "🛡️" else "🪙", fontSize = 16.sp)
                                                    }
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Column {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Text(
                                                                text = "+${invoice.coins} Monedha",
                                                                color = Color(0xFFFFD700),
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Box(
                                                                modifier = Modifier
                                                                    .background(
                                                                        if (isCrypto) Color(0xFF00E676).copy(alpha = 0.2f) else TikTokCyan.copy(alpha = 0.2f),
                                                                        RoundedCornerShape(4.dp)
                                                                    )
                                                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                                                            ) {
                                                                Text(
                                                                    text = invoice.method.take(16),
                                                                    color = if (isCrypto) Color(0xFF00E676) else TikTokCyan,
                                                                    fontSize = 9.sp,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                            }
                                                        }
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Text(
                                                            text = invoice.date,
                                                            color = Color.White.copy(alpha = 0.5f),
                                                            fontSize = 10.sp
                                                        )
                                                    }
                                                }

                                                Column(horizontalAlignment = Alignment.End) {
                                                    Text(
                                                        text = "€${String.format(java.util.Locale.US, "%.2f", invoice.priceEur)}",
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = "Faturë ➔",
                                                        color = TikTokCyan,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    2 -> {
                        // Withdrawal History
                        Column(modifier = Modifier.height(320.dp)) {
                            if (withdrawalHistory.isEmpty()) {
                                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                                    Text("Nuk keni ende asnjë tërheqje.", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                                }
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    items(withdrawalHistory) { record ->
                                        val isCrypto = record.cryptoAmount.isNotBlank() || record.method.contains("Trust Wallet", ignoreCase = true)
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(TikTokDarkSurface)
                                                .clickable { selectedRecordForDetails = record }
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .background(
                                                            if (isCrypto) Color(0xFF00E676).copy(alpha = 0.15f) else TikTokCyan.copy(alpha = 0.15f),
                                                            CircleShape
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = if (isCrypto) Icons.Default.Shield else Icons.Default.Receipt,
                                                        contentDescription = null,
                                                        tint = if (isCrypto) Color(0xFF00E676) else TikTokCyan,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = record.method,
                                                            color = Color.White,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        if (isCrypto) {
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Box(
                                                                modifier = Modifier
                                                                    .background(Color(0xFF00E676).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                                            ) {
                                                                Text("WEB3", color = Color(0xFF00E676), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                                            }
                                                        }
                                                    }
                                                    Text(record.destinationAccount, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                                                    Text(record.date, color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                                                }
                                            }

                                            Column(horizontalAlignment = Alignment.End) {
                                                Text(
                                                    text = "€${String.format(java.util.Locale.US, "%.2f", record.amountEur)}",
                                                    color = Color(0xFF00E676),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                if (record.cryptoAmount.isNotBlank()) {
                                                    Text(
                                                        text = record.cryptoAmount,
                                                        color = Color(0xFFFFD700),
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                                Text(
                                                    text = record.status,
                                                    color = Color.White.copy(alpha = 0.7f),
                                                    fontSize = 9.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Coin Purchase Checkout Modal (Trust Wallet, Card, PayPal, Bank)
    selectedPackageForPurchase?.let { pkg ->
        RealCoinCheckoutDialog(
            coinPackage = pkg,
            onDismiss = { selectedPackageForPurchase = null },
            onPurchaseSuccess = { invoice ->
                selectedPackageForPurchase = null
                selectedPurchaseInvoice = invoice
            }
        )
    }

    // Purchase Invoice Receipt Dialog
    selectedPurchaseInvoice?.let { invoice ->
        PurchaseReceiptDialog(
            record = invoice,
            onDismiss = { selectedPurchaseInvoice = null }
        )
    }

    // Payout Receipt Details Dialog
    if (selectedRecordForDetails != null) {
        PayoutReceiptDialog(
            record = selectedRecordForDetails!!,
            onDismiss = { selectedRecordForDetails = null }
        )
    }
}

data class CryptoAsset(
    val symbol: String,
    val name: String,
    val iconEmoji: String,
    val eurRate: Double,
    val defaultNetwork: String,
    val availableNetworks: List<String>,
    val sampleAddress: String
)

data class PayoutMethodOption(
    val name: String,
    val icon: ImageVector,
    val desc: String,
    val badge: String? = null
)

@Composable
fun RealWithdrawalForm(
    availableEarnings: Double,
    onWithdrawSubmitted: (WithdrawalRecord) -> Unit
) {
    var selectedMethodIndex by remember { mutableIntStateOf(0) }
    var selectedCryptoIndex by remember { mutableIntStateOf(0) }
    var amountText by remember { mutableStateOf("50.00") }
    var recipientFullName by remember { mutableStateOf("Albanian Creator (Trust Wallet)") }
    var accountDestinationInput by remember { mutableStateOf("0x71C38B296a8498f3F6aB42b01712aE79E648F19B") }
    var bankName by remember { mutableStateOf("TEB Bank") }
    var swiftCode by remember { mutableStateOf("TEBKXKPR") }
    var pasteFeedback by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val cryptoAssets = remember {
        listOf(
            CryptoAsset(
                symbol = "USDT",
                name = "Tether USD",
                iconEmoji = "💵",
                eurRate = 1.09,
                defaultNetwork = "BNB Smart Chain (BEP-20)",
                availableNetworks = listOf(
                    "BNB Smart Chain (BEP-20) ⚡ Trust Wallet Rec",
                    "TRON (TRC-20) 🚀 Pa Komision",
                    "Polygon (POL)",
                    "Ethereum (ERC-20)"
                ),
                sampleAddress = "0x71C38B296a8498f3F6aB42b01712aE79E648F19B"
            ),
            CryptoAsset(
                symbol = "BTC",
                name = "Bitcoin",
                iconEmoji = "₿",
                eurRate = 0.000016,
                defaultNetwork = "Bitcoin Network (BTC)",
                availableNetworks = listOf("Bitcoin Network (BTC)", "BNB Smart Chain (BEP-20 BTCB)"),
                sampleAddress = "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq"
            ),
            CryptoAsset(
                symbol = "ETH",
                name = "Ethereum",
                iconEmoji = "Ξ",
                eurRate = 0.00039,
                defaultNetwork = "Ethereum (ERC-20)",
                availableNetworks = listOf("Ethereum (ERC-20)", "Arbitrum One", "BNB Smart Chain (BEP-20)"),
                sampleAddress = "0x89A32CeB4B8756F98Ac0f89812E3c4A01844F21A"
            ),
            CryptoAsset(
                symbol = "SOL",
                name = "Solana",
                iconEmoji = "◎",
                eurRate = 0.0072,
                defaultNetwork = "Solana Mainnet",
                availableNetworks = listOf("Solana Mainnet"),
                sampleAddress = "7xKXtg2CW87d97TXJSDpbD5jBkheTqA83TZRuJosgAsU"
            ),
            CryptoAsset(
                symbol = "BNB",
                name = "Binance Coin",
                iconEmoji = "🟡",
                eurRate = 0.0019,
                defaultNetwork = "BNB Smart Chain (BEP-20)",
                availableNetworks = listOf("BNB Smart Chain (BEP-20)"),
                sampleAddress = "0x52c6f14022A214a1A3347fD40026e68Af4051D92"
            )
        )
    }

    val selectedCrypto = cryptoAssets[selectedCryptoIndex]
    var selectedNetwork by remember(selectedCryptoIndex) { mutableStateOf(selectedCrypto.availableNetworks.first()) }

    val methods = remember {
        listOf(
            PayoutMethodOption(
                name = "Trust Wallet (Kripto)",
                icon = Icons.Default.Shield,
                desc = "USDT, BTC, ETH direkt në Trust Wallet",
                badge = "⚡ INSTANT / 0% TAX"
            ),
            PayoutMethodOption(
                name = "Transfer Bankar (IBAN)",
                icon = Icons.Default.AccountBalance,
                desc = "Llogari bankare (Kosovë/Shqipëri/SEPA)"
            ),
            PayoutMethodOption(
                name = "PayPal",
                icon = Icons.Default.Paid,
                desc = "Dërgim i menjëhershëm në llogarinë PayPal"
            ),
            PayoutMethodOption(
                name = "Kartë Bankare",
                icon = Icons.Default.CreditCard,
                desc = "Tërheqje direkte në Visa ose Mastercard"
            ),
            PayoutMethodOption(
                name = "Western Union / Ria",
                icon = Icons.Default.AccountBalanceWallet,
                desc = "Tërheqje kesh në agjenci me ID"
            )
        )
    }

    val parsedAmount = amountText.replace(",", ".").toDoubleOrNull() ?: 0.0
    val convertedCryptoAmount = parsedAmount * selectedCrypto.eurRate
    val formattedCryptoAmount = when (selectedCrypto.symbol) {
        "USDT" -> String.format(java.util.Locale.US, "%.2f USDT", convertedCryptoAmount)
        "BTC" -> String.format(java.util.Locale.US, "%.6f BTC", convertedCryptoAmount)
        "ETH" -> String.format(java.util.Locale.US, "%.5f ETH", convertedCryptoAmount)
        else -> String.format(java.util.Locale.US, "%.4f %s", convertedCryptoAmount, selectedCrypto.symbol)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Step 1: Select Payment Method
        Text(
            text = "1. Zgjidhni Metodën e Pagesës:",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            methods.forEachIndexed { index, method ->
                val isSelected = selectedMethodIndex == index
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) TikTokCyan.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.04f))
                        .border(
                            width = if (isSelected) 1.5.dp else 0.5.dp,
                            color = if (isSelected) TikTokCyan else Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            selectedMethodIndex = index
                            pasteFeedback = null
                            when (index) {
                                0 -> {
                                    accountDestinationInput = selectedCrypto.sampleAddress
                                    recipientFullName = "Albanian Creator (Trust Wallet)"
                                }
                                1 -> {
                                    accountDestinationInput = "XK05 1234 5678 9012 3456"
                                    bankName = "TEB Bank"
                                    recipientFullName = "Albanian Creator"
                                }
                                2 -> {
                                    accountDestinationInput = "creator@gmail.com"
                                    recipientFullName = "Albanian Creator"
                                }
                                3 -> {
                                    accountDestinationInput = "4111 2222 3333 4444"
                                    recipientFullName = "Albanian Creator"
                                }
                                4 -> {
                                    accountDestinationInput = "Prishtinë, Kosovë (ID: 117283921)"
                                    recipientFullName = "Albanian Creator"
                                }
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                if (isSelected) {
                                    if (index == 0) Color(0xFF00E676) else TikTokCyan
                                } else Color.White.copy(alpha = 0.1f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = method.icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.Black else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(method.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            if (method.badge != null) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF00E676).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(method.badge, color = Color(0xFF00E676), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Text(method.desc, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                    }
                    if (isSelected) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = TikTokCyan, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // Dedicated Trust Wallet & Crypto Configuration Area
        if (selectedMethodIndex == 0) {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0A2218))
                    .border(1.dp, Color(0xFF00E676).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Konfigurimi i Trust Wallet Web3", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF00E676).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("LIVE BLOCKCHAIN", color = Color(0xFF00E676), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Zgjidhni Kriptomonedhën:", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                Spacer(modifier = Modifier.height(4.dp))

                // Crypto Asset Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    cryptoAssets.forEachIndexed { idx, asset ->
                        val isCryptoSelected = selectedCryptoIndex == idx
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isCryptoSelected) Color(0xFF00E676) else Color.White.copy(alpha = 0.08f))
                                .clickable {
                                    selectedCryptoIndex = idx
                                    accountDestinationInput = asset.sampleAddress
                                    pasteFeedback = null
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(asset.iconEmoji, fontSize = 12.sp)
                                Text(
                                    text = asset.symbol,
                                    color = if (isCryptoSelected) Color.Black else Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Rrjeti i Trust Wallet (Network):", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                Spacer(modifier = Modifier.height(4.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    selectedCrypto.availableNetworks.forEach { net ->
                        val isNetSelected = selectedNetwork == net
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isNetSelected) Color(0xFF00E676).copy(alpha = 0.15f) else Color.White.copy(alpha = 0.04f))
                                .border(
                                    width = if (isNetSelected) 1.dp else 0.5.dp,
                                    color = if (isNetSelected) Color(0xFF00E676) else Color.White.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { selectedNetwork = net }
                                .padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isNetSelected) Icons.Default.CheckCircle else Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = if (isNetSelected) Color(0xFF00E676) else Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = net,
                                color = if (isNetSelected) Color.White else Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                fontWeight = if (isNetSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Real-time Crypto Exchange Preview Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Shuma në Euro:", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                        Text("€${amountText.ifBlank { "0.00" }}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(Color(0xFF00E676).copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(16.dp))
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Pranoni në Trust Wallet:", color = Color(0xFF00E676), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(formattedCryptoAmount, color = Color(0xFFFFD700), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tarifa e Rrjetit (Gas Fee): 0.00 € (Falas) 🎁", color = Color(0xFF00E676), fontSize = 9.sp)
                    Text("Koha: 1-3 min ⚡", color = Color.White.copy(alpha = 0.6f), fontSize = 9.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Step 2: Amount Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2. Shuma për tërheqje:",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "E mundur: €${String.format(java.util.Locale.US, "%.2f", availableEarnings)}",
                color = TikTokCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        // Quick Amount Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(20.0, 50.0, 100.0).forEach { amt ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .clickable { amountText = String.format(java.util.Locale.US, "%.2f", amt) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("€${amt.toInt()}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1.3f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TikTokCyan.copy(alpha = 0.2f))
                    .clickable { amountText = String.format(java.util.Locale.US, "%.2f", availableEarnings) }
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Tërheq Krejt", color = TikTokCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = {
                amountText = it
                errorMessage = null
            },
            label = { Text("Shuma (€ Euro)", fontSize = 11.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = TikTokCyan,
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Step 3: Destination Input depending on method
        Text(
            text = "3. Të dhënat e marrësit:",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = recipientFullName,
            onValueChange = { recipientFullName = it },
            label = {
                Text(
                    text = if (selectedMethodIndex == 0) "Emri i Profilit Web3 ose Marrësit" else "Emri dhe Mbiemri i Plotë (Legal Name)",
                    fontSize = 11.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = TikTokCyan,
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = accountDestinationInput,
            onValueChange = {
                accountDestinationInput = it
                pasteFeedback = null
            },
            label = {
                Text(
                    text = when (selectedMethodIndex) {
                        0 -> "Adresa e Kuletës në Trust Wallet (${selectedCrypto.symbol})"
                        1 -> "IBAN (psh. XK05... ose AL45...)"
                        2 -> "Email i llogarisë PayPal"
                        3 -> "Numri i Kartës Bankare (16 shifra)"
                        else -> "Qyteti & Numri Personal / ID"
                    },
                    fontSize = 11.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = if (selectedMethodIndex == 0) Color(0xFF00E676) else TikTokCyan,
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (selectedMethodIndex == 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF00E676).copy(alpha = 0.12f))
                    .clickable {
                        accountDestinationInput = selectedCrypto.sampleAddress
                        pasteFeedback = "Adresa e Trust Wallet u ngjit me sukses! ✅"
                    }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("📋 Ngjit Adresën e Trust Wallet (Auto-fill)", color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            pasteFeedback?.let { fb ->
                Spacer(modifier = Modifier.height(3.dp))
                Text(fb, color = Color(0xFF00E676), fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
        }

        if (selectedMethodIndex == 1) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(
                    value = bankName,
                    onValueChange = { bankName = it },
                    label = { Text("Emri i Bankës", fontSize = 11.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = TikTokCyan,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.weight(1.2f)
                )
                OutlinedTextField(
                    value = swiftCode,
                    onValueChange = { swiftCode = it },
                    label = { Text("SWIFT / BIC", fontSize = 11.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = TikTokCyan,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.weight(0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Security PIN / 2FA Simulation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.04f), RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = if (selectedMethodIndex == 0) Color(0xFF00E676) else TikTokCyan,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (selectedMethodIndex == 0) "Mbrojtje e Dyfishtë me Kontratë Inteligjente (Smart Contract)" else "Verifikimi i Sigurisë TikTok Creator",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("PIN: 7890 (I verifikuar me sukses)", color = Color(0xFF00E676), fontSize = 10.sp)
            }
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(16.dp))
        }

        errorMessage?.let { err ->
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = err,
                color = TikTokRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Submit Button
        Button(
            onClick = {
                val parsed = amountText.replace(",", ".").toDoubleOrNull()
                if (parsed == null || parsed <= 0) {
                    errorMessage = "Ju lutem vendosni një shumë të vlefshme."
                    return@Button
                }
                if (parsed > availableEarnings) {
                    errorMessage = "Nuk keni balancë të mjaftueshme. E mundur: €${String.format(java.util.Locale.US, "%.2f", availableEarnings)}"
                    return@Button
                }
                if (parsed < 5.0) {
                    errorMessage = "Shuma minimale për tërheqje është €5.00."
                    return@Button
                }
                if (recipientFullName.isBlank() || accountDestinationInput.isBlank()) {
                    errorMessage = "Ju lutem plotësoni të gjitha të dhënat e marrësit."
                    return@Button
                }

                val isCrypto = selectedMethodIndex == 0
                val (cryptoAmtStr, netStr, txStr, expStr) = if (isCrypto) {
                    val hexChars = "0123456789abcdef"
                    val tx = "0x" + (1..64).map { hexChars.random() }.joinToString("")
                    val explorer = if (selectedNetwork.contains("TRON", ignoreCase = true)) {
                        "https://tronscan.org/#/transaction/${tx.removePrefix("0x")}"
                    } else {
                        "https://bscscan.com/tx/$tx"
                    }
                    listOf(formattedCryptoAmount, selectedNetwork, tx, explorer)
                } else {
                    listOf("", "", "", "")
                }

                val createdRecord = TikTokRepository.withdrawEarnings(
                    amount = parsed,
                    method = if (isCrypto) "Trust Wallet (${selectedCrypto.symbol})" else methods[selectedMethodIndex].name,
                    rawDestination = accountDestinationInput,
                    recipientName = recipientFullName,
                    cryptoAmount = cryptoAmtStr,
                    cryptoNetwork = netStr,
                    txHash = txStr,
                    explorerUrl = expStr
                )

                if (createdRecord != null) {
                    onWithdrawSubmitted(createdRecord)
                } else {
                    errorMessage = "Gabim gjatë procesimit të tërheqjes."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = if (selectedMethodIndex == 0) Color(0xFF00E676) else TikTokCyan),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(44.dp)
        ) {
            Text(
                text = if (selectedMethodIndex == 0) {
                    "Tërheq në Trust Wallet ($formattedCryptoAmount)"
                } else {
                    "Konfirmo Tërheqjen (€${amountText.ifBlank { "0.00" }})"
                },
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun PayoutReceiptDialog(
    record: WithdrawalRecord,
    onDismiss: () -> Unit
) {
    val isCrypto = record.cryptoAmount.isNotBlank() || record.method.contains("Trust Wallet", ignoreCase = true)
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    var copiedTx by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = TikTokDarkCard,
            modifier = Modifier.fillMaxWidth().padding(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Official Checkmark
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(Color(0xFF00E676).copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isCrypto) Icons.Default.Shield else Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isCrypto) "Tërheqja me Kripto u Depozitua me Sukses!" else "Tërheqja u Aprovua me Sukses!",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isCrypto) "Pagesë Zyrtare e Depozituar në Trust Wallet" else "Pagesë Zyrtare e TikTok Creator Fund",
                    color = if (isCrypto) Color(0xFF00E676) else TikTokCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Receipt Card Details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TikTokDarkSurface)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReceiptRow("Numri i Referencës:", record.id, isAccent = true)
                    ReceiptRow("Shuma në Euro:", "€${String.format(java.util.Locale.US, "%.2f", record.amountEur)}", isBold = true)
                    if (record.cryptoAmount.isNotBlank()) {
                        ReceiptRow("Pranuar në Trust Wallet:", record.cryptoAmount, isGreen = true, isBold = true)
                    }
                    ReceiptRow("Tarifa e Transferit / Gas Fee:", "€0.00 (Falas)", isGreen = true)
                    ReceiptRow("Shuma Neto:", "€${String.format(java.util.Locale.US, "%.2f", record.netAmountEur)}", isBold = true)
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
                    ReceiptRow("Metoda:", record.method)
                    if (record.cryptoNetwork.isNotBlank()) {
                        ReceiptRow("Rrjeti Blockchain:", record.cryptoNetwork)
                    }
                    ReceiptRow("Destinacioni / Kuleta:", record.destinationAccount)
                    ReceiptRow("Marrësi:", record.recipientName)
                    ReceiptRow("Data & Ora:", record.date)
                    ReceiptRow("Koha e Mbërritjes:", record.estimatedArrival, isGreen = true)
                    ReceiptRow("Statusi:", record.status)
                }

                // TxHash Box for Crypto
                if (record.txHash.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.04f))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Hash i Transaksionit (TxHash):", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White.copy(alpha = 0.08f))
                                    .clickable {
                                        clipboardManager.setText(AnnotatedString(record.txHash))
                                        copiedTx = true
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = if (copiedTx) Color(0xFF00E676) else TikTokCyan,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (copiedTx) "U kopjua! ✅" else "Kopjo TxHash",
                                    color = if (copiedTx) Color(0xFF00E676) else TikTokCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = record.txHash,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 9.sp,
                            lineHeight = 13.sp
                        )
                    }

                    if (record.explorerUrl.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF00E676).copy(alpha = 0.12f))
                                .clickable {
                                    try {
                                        uriHandler.openUri(record.explorerUrl)
                                    } catch (_: Exception) {}
                                }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Shiko në Blockchain Explorer ↗", color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = if (isCrypto) Color(0xFF00E676) else TikTokCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                ) {
                    Text("Në rregull / Shiko Historikun", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String, isAccent: Boolean = false, isBold: Boolean = false, isGreen: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
        Text(
            text = value,
            color = when {
                isGreen -> Color(0xFF00E676)
                isAccent -> TikTokCyan
                else -> Color.White
            },
            fontSize = 11.sp,
            fontWeight = if (isBold || isAccent) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun RealCoinCheckoutDialog(
    coinPackage: CoinPackage,
    onDismiss: () -> Unit,
    onPurchaseSuccess: (CoinPurchaseRecord) -> Unit
) {
    var selectedMethodIndex by remember { mutableIntStateOf(0) } // 0: Trust Wallet, 1: Card, 2: PayPal, 3: Bank
    var selectedCryptoIndex by remember { mutableIntStateOf(0) }

    // Card inputs
    var cardNumber by remember { mutableStateOf("4532 8921 7340 4242") }
    var cardHolder by remember { mutableStateOf("ALBANIAN CREATOR") }
    var cardExpiry by remember { mutableStateOf("11/28") }
    var cardCvv by remember { mutableStateOf("821") }

    // PayPal inputs
    var paypalEmail by remember { mutableStateOf("creator@gmail.com") }

    // Bank inputs
    var userBankIban by remember { mutableStateOf("XK05 1234 5678 9012 3456") }
    var userBankName by remember { mutableStateOf("TEB Bank") }

    var isProcessing by remember { mutableStateOf(false) }
    var addressCopiedFeedback by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    val coroutineScope = rememberCoroutineScope()

    val cryptoOptions = remember {
        listOf(
            CryptoAsset(
                symbol = "USDT",
                name = "Tether USD",
                iconEmoji = "💵",
                eurRate = 1.09,
                defaultNetwork = "BNB Smart Chain (BEP-20)",
                availableNetworks = listOf("BNB Smart Chain (BEP-20) ⚡ Trust Wallet Rec", "TRON (TRC-20) 🚀 Pa Komision", "Polygon (POL)", "Ethereum (ERC-20)"),
                sampleAddress = "0x19B8c7F563B34691E885e34D41bEF681640E1B09"
            ),
            CryptoAsset(
                symbol = "BTC",
                name = "Bitcoin",
                iconEmoji = "₿",
                eurRate = 0.000016,
                defaultNetwork = "Bitcoin Network (BTC)",
                availableNetworks = listOf("Bitcoin Network (BTC)", "BNB Smart Chain (BEP-20)"),
                sampleAddress = "bc1q9d7q5u8z6y7x0w2a4b6c8e0f1g3h5j7k9m1n3p"
            ),
            CryptoAsset(
                symbol = "ETH",
                name = "Ethereum",
                iconEmoji = "Ξ",
                eurRate = 0.00039,
                defaultNetwork = "Ethereum (ERC-20)",
                availableNetworks = listOf("Ethereum (ERC-20)", "BNB Smart Chain (BEP-20)", "Arbitrum One"),
                sampleAddress = "0x19B8c7F563B34691E885e34D41bEF681640E1B09"
            ),
            CryptoAsset(
                symbol = "SOL",
                name = "Solana",
                iconEmoji = "◎",
                eurRate = 0.0072,
                defaultNetwork = "Solana Mainnet",
                availableNetworks = listOf("Solana Mainnet"),
                sampleAddress = "7xKXtg2CW87d97TXJSDpbD5jBkheTqA83TZRuJosgAsU"
            ),
            CryptoAsset(
                symbol = "BNB",
                name = "Binance Coin",
                iconEmoji = "🟡",
                eurRate = 0.0019,
                defaultNetwork = "BNB Smart Chain (BEP-20)",
                availableNetworks = listOf("BNB Smart Chain (BEP-20)"),
                sampleAddress = "0x19B8c7F563B34691E885e34D41bEF681640E1B09"
            )
        )
    }

    val selectedCrypto = cryptoOptions[selectedCryptoIndex]
    var selectedNetwork by remember(selectedCryptoIndex) { mutableStateOf(selectedCrypto.availableNetworks.first()) }
    val cryptoAmount = coinPackage.priceEur * selectedCrypto.eurRate
    val formattedCrypto = when (selectedCrypto.symbol) {
        "USDT" -> String.format(java.util.Locale.US, "%.2f USDT", cryptoAmount)
        "BTC" -> String.format(java.util.Locale.US, "%.6f BTC", cryptoAmount)
        "ETH" -> String.format(java.util.Locale.US, "%.5f ETH", cryptoAmount)
        else -> String.format(java.util.Locale.US, "%.4f %s", cryptoAmount, selectedCrypto.symbol)
    }

    val paymentMethods = listOf(
        PayoutMethodOption("Trust Wallet", Icons.Default.Shield, "Kripto Web3 (USDT, BTC, ETH)", "⚡ 0% KOMISION"),
        PayoutMethodOption("Kartë Bankare", Icons.Default.CreditCard, "Visa / Mastercard 3D Secure", "3D SECURE"),
        PayoutMethodOption("PayPal", Icons.Default.Paid, "One-Touch Checkout", "100% GARANCI"),
        PayoutMethodOption("Transfer Bankar", Icons.Default.AccountBalance, "SEPA / IBAN / E-Banking", "SEPA")
    )

    Dialog(onDismissRequest = { if (!isProcessing) onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = TikTokDarkCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFFFFD700).copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🪙", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Blerja e Monedhave (Checkout)",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Pagesë e Sigurt Zyrtare TikTok Store",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        enabled = !isProcessing,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.7f))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Package Details Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🪙 ${coinPackage.coins} Monedha TikTok",
                            color = Color(0xFFFFD700),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "TVSH e përfshirë • Aktivizim i menjëhershëm",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 10.sp
                        )
                    }

                    Text(
                        text = "€${String.format(java.util.Locale.US, "%.2f", coinPackage.priceEur)}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Zgjidh Metodën e Pagesës:",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Payment Method Selector Grid / Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    paymentMethods.forEachIndexed { index, method ->
                        val isSelected = selectedMethodIndex == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) {
                                        when (index) {
                                            0 -> Color(0xFF00E676).copy(alpha = 0.15f)
                                            2 -> Color(0xFF0070BA).copy(alpha = 0.15f)
                                            else -> TikTokRed.copy(alpha = 0.15f)
                                        }
                                    } else Color.White.copy(alpha = 0.04f)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) {
                                        when (index) {
                                            0 -> Color(0xFF00E676)
                                            2 -> Color(0xFF0070BA)
                                            else -> TikTokRed
                                        }
                                    } else Color.White.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedMethodIndex = index }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = method.icon,
                                    contentDescription = method.name,
                                    tint = if (isSelected) {
                                        when (index) {
                                            0 -> Color(0xFF00E676)
                                            2 -> Color(0xFF009CDE)
                                            else -> TikTokRed
                                        }
                                    } else Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = method.name,
                                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dynamic Body based on selected payment method
                when (selectedMethodIndex) {
                    0 -> {
                        // Trust Wallet (Web3 Kripto)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0A1F16), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF00E676).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Trust Wallet Web3 Pay", color = Color(0xFF00E676), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF00E676), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("0% TAX / GAS FREE", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Zgjidh Kriptovalutën:", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(4.dp))

                            // Crypto Pills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                cryptoOptions.forEachIndexed { i, c ->
                                    val isCryptoSel = selectedCryptoIndex == i
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isCryptoSel) Color(0xFF00E676) else Color.White.copy(alpha = 0.06f))
                                            .clickable { selectedCryptoIndex = i }
                                            .padding(vertical = 5.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${c.iconEmoji} ${c.symbol}",
                                            color = if (isCryptoSel) Color.Black else Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Conversion preview
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Vlera e saktë për pagesë:", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                                    Text(formattedCrypto, color = Color(0xFF00E676), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Text("€1 ≈ ${selectedCrypto.eurRate} ${selectedCrypto.symbol}", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Network selector
                            Text("Rrjeti i Transferit (Trust Wallet):", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                selectedCrypto.availableNetworks.take(2).forEach { net ->
                                    val isNetSel = selectedNetwork == net
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isNetSel) Color(0xFF00E676).copy(alpha = 0.25f) else Color.White.copy(alpha = 0.04f))
                                            .border(1.dp, if (isNetSel) Color(0xFF00E676) else Color.Transparent, RoundedCornerShape(6.dp))
                                            .clickable { selectedNetwork = net }
                                            .padding(vertical = 5.dp, horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = net.take(18),
                                            color = if (isNetSel) Color(0xFF00E676) else Color.White.copy(alpha = 0.7f),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Official TikTok Deposit Address Box
                            Text("Adresa Zyrtare e Depozitimit TikTok Web3:", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${selectedCrypto.sampleAddress.take(14)}...${selectedCrypto.sampleAddress.takeLast(10)}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = if (addressCopiedFeedback) "U Kopjua! ✓" else "📋 Kopjo",
                                    color = Color(0xFF00E676),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable {
                                            clipboardManager.setText(AnnotatedString(selectedCrypto.sampleAddress))
                                            addressCopiedFeedback = true
                                        }
                                        .padding(4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { uriHandler.openUri("https://link.trustwallet.com") }
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.OpenInNew, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Hap direkt në aplikacionin Trust Wallet ↗", color = Color(0xFF00E676), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    1 -> {
                        // Kartë Bankare (Visa / Mastercard)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.04f), RoundedCornerShape(12.dp))
                                .border(1.dp, TikTokRed.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            // Visual Card Preview
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF1E232A))
                                    .padding(10.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("TIKTOK PAY", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Text("💳 VISA / MC", color = Color(0xFFFFD700), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = cardNumber.ifEmpty { "4532 •••• •••• 4242" },
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.5.sp
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(cardHolder.ifEmpty { "EMRI MBI KARTË" }, color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Medium)
                                        Text("SKADON: $cardExpiry", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Inputs
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { if (it.length <= 19) cardNumber = it },
                                label = { Text("Numri i Kartës (16 Shifra)", fontSize = 10.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = TikTokRed,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedLabelColor = TikTokRed
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = cardHolder,
                                onValueChange = { cardHolder = it },
                                label = { Text("Emri dhe Mbiemri mbi Kartë", fontSize = 10.sp) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = TikTokRed,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedLabelColor = TikTokRed
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(
                                    value = cardExpiry,
                                    onValueChange = { if (it.length <= 5) cardExpiry = it },
                                    label = { Text("Skadimi (MM/YY)", fontSize = 10.sp) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = TikTokRed,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        focusedLabelColor = TikTokRed
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = cardCvv,
                                    onValueChange = { if (it.length <= 4) cardCvv = it },
                                    label = { Text("CVV / CVC", fontSize = 10.sp) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = TikTokRed,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        focusedLabelColor = TikTokRed
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("3D Secure 2.0 & Verified by Visa / Mastercard ID Check", color = Color.White.copy(alpha = 0.6f), fontSize = 9.sp)
                            }
                        }
                    }

                    2 -> {
                        // PayPal
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF001F3F), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF0070BA), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🅿️ PayPal Express Checkout", color = Color(0xFF009CDE), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF0070BA), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("ONE-TOUCH", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Paguaj shpejt dhe në mënyrë të sigurt përmes llogarisë tënde të PayPal me mbrojtje 100% të blerësit.",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = paypalEmail,
                                onValueChange = { paypalEmail = it },
                                label = { Text("Email i Llogarisë PayPal", fontSize = 10.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF009CDE),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedLabelColor = Color(0xFF009CDE)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Llogari e verifikuar për pagesa të menjëhershme", color = Color(0xFF00E676), fontSize = 10.sp)
                            }
                        }
                    }

                    else -> {
                        // Transfer Bankar (SEPA / IBAN)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.04f), RoundedCornerShape(12.dp))
                                .border(1.dp, TikTokCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🏦 Transfer Bankar Zyrtar (IBAN)", color = TikTokCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Box(
                                    modifier = Modifier
                                        .background(TikTokCyan.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("SEPA / E-BANKING", color = TikTokCyan, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                ReceiptRow("Përfituesi:", "TikTok Live Europe S.a.r.l.", isBold = true)
                                ReceiptRow("IBAN i TikTok:", "XK05 1200 0000 8921 4567", isBold = true, isAccent = true)
                                ReceiptRow("Banka:", "TEB Bank Sh.A. / Raiffeisen", isBold = true)
                                ReceiptRow("Kodi SWIFT / BIC:", "TEBKXKPR")
                                ReceiptRow("Qëllimi i Pagesës:", "TKCOINS-${coinPackage.coins}", isBold = true)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = userBankIban,
                                onValueChange = { userBankIban = it },
                                label = { Text("IBAN i Dërguesit (Llogaria Juaj)", fontSize = 10.sp) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = TikTokCyan,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedLabelColor = TikTokCyan
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = userBankName,
                                onValueChange = { userBankName = it },
                                label = { Text("Emri i Bankës Tuaj (p.sh. TEB, Raiffeisen, NLB)", fontSize = 10.sp) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = TikTokCyan,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedLabelColor = TikTokCyan
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Security note
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Enkriptim Bankar 256-bit • Faturë Zyrtare e Gjeneruar Menjëherë",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 9.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Purchase Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isProcessing = true
                            delay(1200) // Realistic secure gateway response
                            val record = when (selectedMethodIndex) {
                                0 -> {
                                    val hexChars = "0123456789abcdef"
                                    val tx = "0x" + (1..64).map { hexChars.random() }.joinToString("")
                                    val explorer = if (selectedNetwork.contains("TRON", ignoreCase = true)) {
                                        "https://tronscan.org/#/transaction/${tx.removePrefix("0x")}"
                                    } else {
                                        "https://bscscan.com/tx/$tx"
                                    }
                                    TikTokRepository.buyCoinsReal(
                                        coinsCount = coinPackage.coins,
                                        priceEur = coinPackage.priceEur,
                                        method = "Trust Wallet (${selectedCrypto.symbol})",
                                        accountDetail = "${selectedCrypto.sampleAddress.take(6)}...${selectedCrypto.sampleAddress.takeLast(4)} ($selectedNetwork)",
                                        cryptoAmount = formattedCrypto,
                                        cryptoNetwork = selectedNetwork,
                                        txHash = tx,
                                        explorerUrl = explorer
                                    )
                                }
                                1 -> {
                                    val lastFour = cardNumber.filter { it.isDigit() }.takeLast(4).ifEmpty { "4242" }
                                    TikTokRepository.buyCoinsReal(
                                        coinsCount = coinPackage.coins,
                                        priceEur = coinPackage.priceEur,
                                        method = "Kartë Bankare (Visa)",
                                        accountDetail = "•••• $lastFour • $cardHolder"
                                    )
                                }
                                2 -> {
                                    TikTokRepository.buyCoinsReal(
                                        coinsCount = coinPackage.coins,
                                        priceEur = coinPackage.priceEur,
                                        method = "PayPal",
                                        accountDetail = paypalEmail
                                    )
                                }
                                else -> {
                                    TikTokRepository.buyCoinsReal(
                                        coinsCount = coinPackage.coins,
                                        priceEur = coinPackage.priceEur,
                                        method = "Transfer Bankar (IBAN)",
                                        accountDetail = "$userBankName • ${userBankIban.take(4)}...${userBankIban.takeLast(4)}"
                                    )
                                }
                            }
                            isProcessing = false
                            onPurchaseSuccess(record)
                        }
                    },
                    enabled = !isProcessing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (selectedMethodIndex) {
                            0 -> Color(0xFF00E676)
                            2 -> Color(0xFF0070BA)
                            3 -> TikTokCyan
                            else -> TikTokRed
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            color = if (selectedMethodIndex == 0 || selectedMethodIndex == 3) Color.Black else Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Duke autorizuar pagesën e sigurt...",
                            color = if (selectedMethodIndex == 0 || selectedMethodIndex == 3) Color.Black else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        val methodName = paymentMethods[selectedMethodIndex].name
                        Text(
                            text = "Paguaj €${String.format(java.util.Locale.US, "%.2f", coinPackage.priceEur)} me $methodName ➔",
                            color = if (selectedMethodIndex == 0 || selectedMethodIndex == 3) Color.Black else Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseReceiptDialog(
    record: CoinPurchaseRecord,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    var hashCopied by remember { mutableStateOf(false) }
    var invoiceCopied by remember { mutableStateOf(false) }
    val isCrypto = record.cryptoAmount.isNotBlank() || record.method.contains("Trust Wallet", ignoreCase = true)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = TikTokDarkCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            if (isCrypto) Color(0xFF00E676).copy(alpha = 0.15f) else Color(0xFFFFD700).copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = if (isCrypto) Color(0xFF00E676) else Color(0xFFFFD700),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Blerja u Krye me Sukses! ✅",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Faturë Zyrtare e Blerjes • TikTok Store",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Card container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.04f), RoundedCornerShape(12.dp))
                        .border(1.dp, if (isCrypto) Color(0xFF00E676).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Headline summary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Monedha të Kredituara:", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                            Text(
                                text = "+${record.coins} Monedha 🪙",
                                color = Color(0xFFFFD700),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Shuma e Paguar:", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                            Text(
                                text = "€${String.format(java.util.Locale.US, "%.2f", record.priceEur)}",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)

                    ReceiptRow("Numri i Faturës:", record.id, isBold = true)
                    ReceiptRow("Metoda e Pagesës:", record.method, isAccent = true)
                    ReceiptRow("Llogaria / Burimi:", record.accountDetail)
                    ReceiptRow("Kodi i Autorizimit:", record.authCode)
                    ReceiptRow("Data & Ora:", record.date)
                    ReceiptRow("Statusi:", record.status, isGreen = true, isBold = true)

                    // If Trust Wallet / Crypto
                    if (isCrypto) {
                        HorizontalDivider(color = Color(0xFF00E676).copy(alpha = 0.2f), thickness = 0.5.dp)

                        if (record.cryptoAmount.isNotBlank()) {
                            ReceiptRow("Shuma në Kripto:", record.cryptoAmount, isGreen = true, isBold = true)
                        }
                        if (record.cryptoNetwork.isNotBlank()) {
                            ReceiptRow("Rrjeti Blockchain:", record.cryptoNetwork)
                        }

                        if (record.txHash.isNotBlank()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("TxHash (Hash Transaksioni):", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                                    Text(
                                        text = if (hashCopied) "Kopjuar! ✓" else "Kopjo 📋",
                                        color = Color(0xFF00E676),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.clickable {
                                            clipboardManager.setText(AnnotatedString(record.txHash))
                                            hashCopied = true
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "${record.txHash.take(16)}...${record.txHash.takeLast(16)}",
                                    color = Color(0xFF00E676),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (record.explorerUrl.isNotBlank()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF00E676).copy(alpha = 0.12f))
                                    .clickable { uriHandler.openUri(record.explorerUrl) }
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.OpenInNew, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Shiko në Blockchain Explorer ↗", color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString("Fatura: ${record.id} - ${record.coins} Monedha (€${record.priceEur})"))
                            invoiceCopied = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).height(42.dp)
                    ) {
                        Text(
                            text = if (invoiceCopied) "Kopjuar! ✓" else "Kopjo Nr.",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCrypto) Color(0xFF00E676) else TikTokRed
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1.5f).height(42.dp)
                    ) {
                        Text(
                            text = "Vazhdo te Dhuratat ➔",
                            color = if (isCrypto) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}


