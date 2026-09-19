package com.example.data

data class TikTokComment(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatar: String,
    val text: String,
    val timestamp: String,
    val likes: Long,
    val isLiked: Boolean = false,
    val isCreator: Boolean = false
)

data class VideoItem(
    val id: String,
    val videoUrl: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatar: String,
    val isVerified: Boolean = false,
    val isFollowing: Boolean = false,
    val caption: String,
    val tags: List<String>,
    val soundTitle: String,
    val soundAuthor: String,
    val soundAlbumArt: String,
    val likesCount: Long,
    val commentsCount: Long,
    val favoritesCount: Long,
    val sharesCount: Long,
    val repostsCount: Long = 1200,
    val viewsCount: Long = 48500,
    val isLiked: Boolean = false,
    val isFavorited: Boolean = false,
    val isReposted: Boolean = false,
    val isShopItem: Boolean = false,
    val shopProductTitle: String = "",
    val shopProductPrice: String = "",
    val primaryColor: Long = 0xFF111122,
    val secondaryColor: Long = 0xFF221133,
    val comments: List<TikTokComment> = emptyList()
)

data class TikTokUser(
    val username: String,
    val handle: String,
    val avatarUrl: String,
    val bio: String,
    val website: String = "linktr.ee/shqipvibes",
    val followingCount: String,
    val followersCount: String,
    val likesCount: String,
    val isVerified: Boolean = false,
    val isPrivate: Boolean = false,
    val coins: Long = 450,
    val diamondEarnings: Double = 184.50,
    val isLoggedIn: Boolean = true,
    val email: String = "creator@tiktok.al",
    val phone: String = "+383 49 123 456"
)

data class ActivityNotification(
    val id: String,
    val userHandle: String,
    val userAvatar: String,
    val actionText: String,
    val timeAgo: String,
    val videoPreview: String? = null,
    val isFollowAction: Boolean = false
)

data class VirtualGift(
    val id: String,
    val name: String,
    val emoji: String,
    val coins: Int,
    val category: String = "Klasike",
    val isSpecialUck: Boolean = false,
    val bannerText: String? = null
)

data class DirectMessage(
    val id: String,
    val senderHandle: String,
    val text: String,
    val timestamp: String,
    val isMe: Boolean,
    val giftEmoji: String? = null
)

data class ChatConversation(
    val id: String,
    val participantHandle: String,
    val participantName: String,
    val participantAvatar: String,
    val isVerified: Boolean = false,
    val messages: List<DirectMessage> = emptyList()
)

data class LiveComment(
    val id: String,
    val user: String,
    val avatar: String,
    val text: String,
    val giftEmoji: String? = null
)

data class LiveStreamItem(
    val id: String,
    val hostName: String,
    val hostHandle: String,
    val hostAvatar: String,
    val title: String,
    val viewersCount: String,
    val category: String,
    val videoUrl: String,
    val comments: List<LiveComment> = emptyList()
)

data class ShopProduct(
    val id: String,
    val title: String,
    val price: String,
    val originalPrice: String,
    val sales: String,
    val rating: Float,
    val imageUrl: String,
    val category: String
)

data class SoundTrack(
    val id: String,
    val title: String,
    val artist: String,
    val duration: String,
    val coverUrl: String,
    val usageCount: String,
    val isFavorite: Boolean = false
)

data class WithdrawalRecord(
    val id: String,
    val amountEur: Double,
    val feeEur: Double = 0.0,
    val netAmountEur: Double,
    val method: String,
    val destinationAccount: String,
    val recipientName: String,
    val status: String = "Në Proces ⏳",
    val date: String,
    val estimatedArrival: String,
    val cryptoAmount: String = "",
    val cryptoNetwork: String = "",
    val txHash: String = "",
    val explorerUrl: String = ""
)

data class CoinPurchaseRecord(
    val id: String,
    val coins: Long,
    val priceEur: Double,
    val method: String,
    val accountDetail: String,
    val date: String,
    val status: String = "Kompletuar me Sukses ✅",
    val authCode: String = "",
    val cryptoAmount: String = "",
    val cryptoNetwork: String = "",
    val txHash: String = "",
    val explorerUrl: String = ""
)


