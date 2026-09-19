package com.example.data

enum class BattleMode(val label: String, val shortLabel: String, val description: String) {
    SOLO("Solo Transmetim", "Solo", "Transmetim individual pa betejë"),
    ONE_VS_ONE("1 vs 1 PK", "1vs1", "Betejë direkte mes 2 krijuesve"),
    TWO_VS_ONE("2 vs 1 PK", "2vs1", "2 krijues kundër 1 kundërshtari"),
    TWO_VS_TWO("2 vs 2 PK", "2vs2", "Betejë skuadrash (4 krijues në ekran)")
}

data class TopGifter(
    val name: String,
    val avatar: String,
    val coinsContributed: Long,
    val rank: Int
)

data class BattleParticipant(
    val id: String,
    val name: String,
    val handle: String,
    val avatar: String,
    val team: String, // "RED" or "BLUE"
    val score: Long = 0,
    val likes: Long = 0,
    val isMuted: Boolean = false,
    val topGifters: List<TopGifter> = emptyList()
)

data class LiveBattleState(
    val isActive: Boolean = false,
    val mode: BattleMode = BattleMode.ONE_VS_ONE,
    val durationSeconds: Int = 180, // Default 3 min battle
    val timeRemainingSeconds: Int = 180,
    val isSpeedUpActive: Boolean = false, // Double points period (Speed up!)
    val isPunishmentPeriod: Boolean = false, // When battle ends, punishment phase begins
    val redTeamScore: Long = 1450,
    val blueTeamScore: Long = 1200,
    val redParticipants: List<BattleParticipant> = emptyList(),
    val blueParticipants: List<BattleParticipant> = emptyList(),
    val punishment: String = "20 Pompa në dysheme 💪",
    val winnerTeam: String? = null // "RED", "BLUE", or "DRAW"
)

data class LiveGuestRequest(
    val id: String,
    val name: String,
    val handle: String,
    val avatar: String,
    val followers: String,
    val isConnected: Boolean = false
)

data class LiveGoal(
    val title: String = "Synimi i Live-it",
    val giftName: String = "Trëndafil",
    val giftEmoji: String = "🌹",
    val currentCount: Int = 38,
    val targetCount: Int = 100
)

data class LiveBeautyFilter(
    val id: String,
    val name: String,
    val emoji: String,
    val intensity: Float = 0.7f
)
