package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object TikTokRepository {

    private val initialVideos = listOf(
        VideoItem(
            id = "vid_1",
            videoUrl = "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_1MB.mp4",
            authorName = "Elena Dance",
            authorHandle = "@elenadance",
            authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&fit=crop",
            isVerified = true,
            isFollowing = false,
            caption = "New choreo dropped! Tag someone who needs to learn this routine 🔥💃 #dance #fyp #viral #trending #groove",
            tags = listOf("#dance", "#fyp", "#viral", "#trending", "#groove"),
            soundTitle = "Original Sound - Beat Wave",
            soundAuthor = "Beat Wave",
            soundAlbumArt = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200&fit=crop",
            likesCount = 1240500,
            commentsCount = 38400,
            favoritesCount = 89200,
            sharesCount = 52100,
            isLiked = false,
            isFavorited = false,
            primaryColor = 0xFF0D1B2A,
            secondaryColor = 0xFF415A77,
            comments = listOf(
                TikTokComment("c1", "Marcus J", "@marcus_j", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&fit=crop", "That footwork at 0:04 is insane!! 🔥", "2h ago", 1420),
                TikTokComment("c2", "Sarah Vibe", "@sarah_vibes", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&fit=crop", "Already learned the first 8 counts! Let's duet!", "4h ago", 982),
                TikTokComment("c3", "Liam Tech", "@liam_creates", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&fit=crop", "The video transitions are so smooth clean edit 👏", "6h ago", 412)
            )
        ),
        VideoItem(
            id = "vid_2",
            videoUrl = "https://test-videos.co.uk/vids/jellyfish/mp4/h264/720/Jellyfish_720_10s_1MB.mp4",
            authorName = "Chef Marco",
            authorHandle = "@chef_marco",
            authorAvatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&fit=crop",
            isVerified = true,
            isFollowing = true,
            caption = "Secret 3-ingredient crispy pasta chips you need to try tonight! 🍝✨ #foodtiktok #recipes #easycooking #foodie",
            tags = listOf("#foodtiktok", "#recipes", "#easycooking", "#foodie"),
            soundTitle = "Cooking In Paris - Lofi Vibes",
            soundAuthor = "Lofi Vibes",
            soundAlbumArt = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=200&fit=crop",
            likesCount = 892100,
            commentsCount = 14320,
            favoritesCount = 152000,
            sharesCount = 38900,
            isLiked = true,
            isFavorited = true,
            primaryColor = 0xFF3D0C11,
            secondaryColor = 0xFF7A1C28,
            comments = listOf(
                TikTokComment("c4", "Foodie Queen", "@foodie_queen", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&fit=crop", "Making this right now brb 🏃‍♀️", "1h ago", 2400),
                TikTokComment("c5", "Alex Baker", "@alexbakes", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150&fit=crop", "Air fryer time and temp please!!", "3h ago", 750)
            )
        ),
        VideoItem(
            id = "vid_3",
            videoUrl = "https://test-videos.co.uk/vids/sintel/mp4/h264/1080/Sintel_1080_10s_1MB.mp4",
            authorName = "Alex Skate",
            authorHandle = "@skater_alex",
            authorAvatar = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=200&fit=crop",
            isVerified = false,
            isFollowing = false,
            caption = "Took 47 attempts but finally nailed the laser flip down the stair set! 🛹 Never give up! #skate #lifestyle #progress",
            tags = listOf("#skate", "#lifestyle", "#progress", "#hype"),
            soundTitle = "Bass Boosted - Urban Beats",
            soundAuthor = "Skate Tribe",
            soundAlbumArt = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=200&fit=crop",
            likesCount = 2450300,
            commentsCount = 67200,
            favoritesCount = 210000,
            sharesCount = 98400,
            isLiked = false,
            isFavorited = false,
            primaryColor = 0xFF1C2541,
            secondaryColor = 0xFF0B132B,
            comments = listOf(
                TikTokComment("c6", "Jordan R", "@jordan_skates", "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150&fit=crop", "Clean catch bro! Looked effortless on camera", "30m ago", 3100),
                TikTokComment("c7", "Chloe M", "@chloe_art", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&fit=crop", "The determination is inspiring 🔥", "2h ago", 1200)
            )
        ),
        VideoItem(
            id = "vid_4",
            videoUrl = "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_2MB.mp4",
            authorName = "Milo The Golden",
            authorHandle = "@milogoldenretriever",
            authorAvatar = "https://images.unsplash.com/photo-1552053831-71594a27632d?w=200&fit=crop",
            isVerified = true,
            isFollowing = true,
            caption = "Milo heard the cheese wrapper from 3 rooms away 😂 Wait for the head tilt! 🧀🐶 #dogsoftiktok #cute #goldenretriever",
            tags = listOf("#dogsoftiktok", "#cute", "#goldenretriever", "#wholesome"),
            soundTitle = "Cute Whistling - Happy Tunes",
            soundAuthor = "Pet Records",
            soundAlbumArt = "https://images.unsplash.com/photo-1450778869180-41d0601e046e?w=200&fit=crop",
            likesCount = 3810400,
            commentsCount = 94100,
            favoritesCount = 430500,
            sharesCount = 184000,
            isLiked = true,
            isFavorited = false,
            primaryColor = 0xFF283618,
            secondaryColor = 0xFF606C38,
            comments = listOf(
                TikTokComment("c8", "Emma Watson", "@emma_reads", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&fit=crop", "GIVE HIM THE CHEESE RIGHT NOW 😭🧀", "15m ago", 8900),
                TikTokComment("c9", "Sam P", "@sam_travels", "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=150&fit=crop", "That ultrasonic hearing is unmatched haha", "1h ago", 2100)
            )
        ),
        VideoItem(
            id = "vid_5",
            videoUrl = "https://test-videos.co.uk/vids/jellyfish/mp4/h264/720/Jellyfish_720_10s_2MB.mp4",
            authorName = "Tech Universe",
            authorHandle = "@techuniverse",
            authorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&fit=crop",
            isVerified = true,
            isFollowing = false,
            caption = "Is this the future of phones? Transparent holographic screen test! 📱✨ Rate this 1-10! #tech #gadgets #future #ai",
            tags = listOf("#tech", "#gadgets", "#future", "#ai"),
            soundTitle = "Cyber Wave 2099 - Tech Beats",
            soundAuthor = "Future Beats",
            soundAlbumArt = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=200&fit=crop",
            likesCount = 1670900,
            commentsCount = 42900,
            favoritesCount = 98100,
            sharesCount = 63400,
            isLiked = false,
            isFavorited = true,
            primaryColor = 0xFF051923,
            secondaryColor = 0xFF006466,
            comments = listOf(
                TikTokComment("c10", "Dev Dave", "@coder_dave", "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=150&fit=crop", "Battery life must be 12 minutes max haha", "45m ago", 3400),
                TikTokComment("c11", "Maya K", "@maya_designs", "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150&fit=crop", "The UI animations look unreal though! Need one!", "3h ago", 1520)
            )
        )
    )

    val virtualGifts = listOf(
        // UÇK - Top Most Expensive Gift (10,000 Coins)
        VirtualGift("g_uck", "UÇK", "🛡️ UÇK", 10000, category = "Kombëtare", isSpecialUck = true, bannerText = "🦅🇦🇱 UÇK - LEGJENDA KOMBËTARE U DËRGUA! 🇦🇱🦅"),

        // Kombëtare (Albanian National Gifts)
        VirtualGift("g_shqiponja", "Shqiponja Kuq e Zi", "🦅", 499, category = "Kombëtare", bannerText = "🇦🇱 Shqiponja Kuq e Zi Fluturoi! 🇦🇱"),
        VirtualGift("g_plis", "Plisi i Bardhë", "👑", 299, category = "Kombëtare"),
        VirtualGift("g_flamuri", "Flamuri Shqiptar", "🇦🇱", 199, category = "Kombëtare"),
        VirtualGift("g_kafe", "Kafe Shqiptare", "☕", 10, category = "Kombëtare"),
        VirtualGift("g_qeleshe", "Dafina Shqiptare", "🌿", 45, category = "Kombëtare"),

        // Popullore (Popular TikTok Live Gifts)
        VirtualGift("g1", "Trëndafil", "🌹", 1, category = "Popullore"),
        VirtualGift("g2", "TikTok Heart", "💖", 5, category = "Popullore"),
        VirtualGift("g3", "Panda Simpatike", "🐼", 15, category = "Popullore"),
        VirtualGift("g4", "Konfeti Party", "🎉", 25, category = "Popullore"),
        VirtualGift("g5", "Fishekzjarre", "🎆", 50, category = "Popullore"),
        VirtualGift("g6", "Super Raketë", "🚀", 99, category = "Popullore"),
        VirtualGift("g7", "Luani i Artë", "🦁", 500, category = "Popullore", bannerText = "🦁 LUANI I ARTË U DËRGUA ME MADHËSHTI!"),
        VirtualGift("g8", "TikTok Galaxy", "🌌", 999, category = "Popullore", bannerText = "🌌 GALAXY E SHNDRITSHME E TIKTOK!"),

        // VIP Luksoze (Luxury & Elite Gifts)
        VirtualGift("g_meteor", "Meteori Kozmik", "☄️", 8999, category = "VIP", bannerText = "☄️ METEOR KOZMIK NË LIVE STREAM!"),
        VirtualGift("g_crown", "Kurorë Perandorake", "👑", 6999, category = "VIP", bannerText = "👑 KURORA PERANDORAKE PËR KRIJUESIN!"),
        VirtualGift("g_phoenix", "Feniksi i Zjarrit", "🔥", 5999, category = "VIP", bannerText = "🔥 FENIKSI LEGJENDAR U ZGJUA!"),
        VirtualGift("g_universe", "Universi TikTok", "🪐", 4999, category = "VIP", bannerText = "🪐 UNIVERSI TIKTOK NDRIÇOI SKENËN!"),
        VirtualGift("g_dragon", "Dragua Mistik", "🐉", 3999, category = "VIP", bannerText = "🐉 DRAGUA MISTIK I LIVE-IT!"),
        VirtualGift("g_castle", "Kështjellë Mbretërore", "🏰", 2999, category = "VIP"),
        VirtualGift("g_falcon", "Falcon i Shpejtë", "🦅", 2499, category = "VIP"),
        VirtualGift("g_yacht", "Jaht Luksoz", "🛥️", 1999, category = "VIP"),
        VirtualGift("g_car", "Supercar Ferrari", "🏎️", 1499, category = "VIP"),

        // Klasike & Emocione (Classic / Mood Gifts)
        VirtualGift("g_kiss", "Puthje Dashurie", "💋", 20, category = "Klasike"),
        VirtualGift("g_cherry", "Qershi e Ëmbël", "🍒", 30, category = "Klasike"),
        VirtualGift("g_flower", "Kurorë Lulesh", "🌸", 40, category = "Klasike"),
        VirtualGift("g_bee", "Bletë Mjalti", "🐝", 60, category = "Klasike"),
        VirtualGift("g_star", "Yll me Shkëlqim", "⭐", 80, category = "Klasike"),
        VirtualGift("g_nazar", "Syri i Kaltër", "🧿", 120, category = "Klasike"),
        VirtualGift("g_guitar", "Kitarë Rock", "🎸", 180, category = "Klasike"),
        VirtualGift("g_ring", "Unazë Diamanti", "💍", 350, category = "Klasike"),
        VirtualGift("g_champagne", "Shampanjë VIP", "🍾", 750, category = "Klasike")
    )

    val initialShopProducts = listOf(
        ShopProduct("p1", "Wireless Noise-Canceling RGB Headphones", "€24.99", "€59.99", "1.4k sold", 4.9f, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=300&fit=crop", "Tech"),
        ShopProduct("p2", "TikTok Pro Ring Light & Tripod Stand", "€18.50", "€34.00", "3.8k sold", 4.8f, "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=300&fit=crop", "Creator Gear"),
        ShopProduct("p3", "Crispy Pasta Cutter & Chef Rolling Pin", "€12.99", "€22.00", "890 sold", 4.7f, "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=300&fit=crop", "Kitchen"),
        ShopProduct("p4", "Streetwear Oversized Graphic Hoodie", "€34.99", "€65.00", "2.1k sold", 4.9f, "https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=300&fit=crop", "Fashion"),
        ShopProduct("p5", "Custom Pro Skateboard Grip Tape & Bearings", "€15.00", "€25.00", "640 sold", 4.6f, "https://images.unsplash.com/photo-1520045892732-304bc3ac5d8e?w=300&fit=crop", "Sports")
    )

    val initialSounds = listOf(
        SoundTrack("s1", "Beat Wave - Summer Dance", "Beat Wave", "0:15", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200&fit=crop", "1.2M videos", isFavorite = true),
        SoundTrack("s2", "Cooking In Paris - Lofi Beats", "Lofi Vibes", "0:30", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=200&fit=crop", "840K videos", isFavorite = false),
        SoundTrack("s3", "Bass Boosted - Urban Anthem", "Skate Tribe", "0:12", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=200&fit=crop", "2.4M videos", isFavorite = true),
        SoundTrack("s4", "Cute Whistling - Happy Melody", "Pet Records", "0:20", "https://images.unsplash.com/photo-1450778869180-41d0601e046e?w=200&fit=crop", "3.9M videos", isFavorite = false),
        SoundTrack("s5", "Cyber Wave 2099 - Future Tech", "Future Beats", "0:18", "https://images.unsplash.com/photo-1518770660439-4636190af475?w=200&fit=crop", "510K videos", isFavorite = false),
        SoundTrack("s6", "Shqip Trap Beat 2026", "Tirana Sound Lab", "0:25", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200&fit=crop", "690K videos", isFavorite = true)
    )

    val initialLiveStreams = listOf(
        LiveStreamItem(
            id = "live_1",
            hostName = "Elena Dance 💃",
            hostHandle = "@elenadance",
            hostAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&fit=crop",
            title = "🔴 Mësojmë kërcimin e ri LIVE bashkë! Q&A + Battle 🔥",
            viewersCount = "4.2K",
            category = "Dance & Music",
            videoUrl = "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_1MB.mp4",
            comments = listOf(
                LiveComment("lc1", "Marcus", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&fit=crop", "Përshëndetje Elena nga Prishtina! 🇦🇱"),
                LiveComment("lc2", "Arta K", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&fit=crop", "Super energji sonte! 💃✨"),
                LiveComment("lc3", "Besnik", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&fit=crop", "Dërgova Rose! 🌹", giftEmoji = "🌹")
            )
        ),
        LiveStreamItem(
            id = "live_2",
            hostName = "Chef Marco 👨‍🍳",
            hostHandle = "@chef_marco",
            hostAvatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&fit=crop",
            title = "Gatuajmë recetën sekrete LIVE! Pyetjet tuaja në chat 🍝",
            viewersCount = "1.8K",
            category = "Food & Cooking",
            videoUrl = "https://test-videos.co.uk/vids/jellyfish/mp4/h264/720/Jellyfish_720_10s_1MB.mp4",
            comments = listOf(
                LiveComment("lc4", "Valon", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&fit=crop", "Çfarë djathi përdor? 🧀"),
                LiveComment("lc5", "Lina", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&fit=crop", "Duket fantastike masterchef! 👏")
            )
        )
    )

    val initialConversations = listOf(
        ChatConversation(
            id = "chat_1",
            participantHandle = "@elenadance",
            participantName = "Elena Dance",
            participantAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&fit=crop",
            isVerified = true,
            messages = listOf(
                DirectMessage("m1", "@elenadance", "Hej! Pashë videon tënde, shumë bukur! 🔥", "10:24 AM", isMe = false),
                DirectMessage("m2", "@albanian_vibes", "Faleminderit shumë Elena! A bëjmë një duet së shpejti? 💃", "10:26 AM", isMe = true),
                DirectMessage("m3", "@elenadance", "Patjetër! Të dërgoj idenë e zërit sonte 🎵", "10:30 AM", isMe = false)
            )
        ),
        ChatConversation(
            id = "chat_2",
            participantHandle = "@chef_marco",
            participantName = "Chef Marco",
            participantAvatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&fit=crop",
            isVerified = true,
            messages = listOf(
                DirectMessage("m4", "@albanian_vibes", "Përshëndetje Marco, receta e pastës doli perfekt! 🍝", "Dje", isMe = true),
                DirectMessage("m5", "@chef_marco", "Bravo mik! Më bëj tag në video që ta bëj Repost! ✨", "Dje", isMe = false)
            )
        ),
        ChatConversation(
            id = "chat_3",
            participantHandle = "@skater_alex",
            participantName = "Alex Skate",
            participantAvatar = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=200&fit=crop",
            isVerified = false,
            messages = listOf(
                DirectMessage("m6", "@skater_alex", "Bro ai kickflip në fillim ishte i çmendur 🛹", "2d më parë", isMe = false)
            )
        )
    )

    private val _videos = MutableStateFlow<List<VideoItem>>(initialVideos)
    val videos: StateFlow<List<VideoItem>> = _videos.asStateFlow()

    private val _currentUser = MutableStateFlow(
        TikTokUser(
            username = "Albanian Creator",
            handle = "@albanian_vibes",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&fit=crop",
            bio = "🇦🇱 Shqip vibe | Krijues videosh & muzike | Daily uploads 🔥",
            followingCount = "142",
            followersCount = "28.5K",
            likesCount = "394.2K",
            isVerified = true,
            isPrivate = false,
            coins = 450,
            diamondEarnings = 184.50
        )
    )
    val currentUser: StateFlow<TikTokUser> = _currentUser.asStateFlow()

    private val _conversations = MutableStateFlow<List<ChatConversation>>(initialConversations)
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    private val _liveStreams = MutableStateFlow<List<LiveStreamItem>>(initialLiveStreams)
    val liveStreams: StateFlow<List<LiveStreamItem>> = _liveStreams.asStateFlow()

    private val _shopProducts = MutableStateFlow<List<ShopProduct>>(initialShopProducts)
    val shopProducts: StateFlow<List<ShopProduct>> = _shopProducts.asStateFlow()

    private val _sounds = MutableStateFlow<List<SoundTrack>>(initialSounds)
    val sounds: StateFlow<List<SoundTrack>> = _sounds.asStateFlow()

    private val initialWithdrawals = listOf(
        WithdrawalRecord(
            id = "TK-CRYPTO-981240",
            amountEur = 60.00,
            feeEur = 0.00,
            netAmountEur = 60.00,
            method = "Trust Wallet (USDT - BEP20)",
            destinationAccount = "0x89A3...4F21 (Trust Wallet)",
            recipientName = "Web3 Creator",
            status = "Përfunduar në Blockchain ✅",
            date = "18 Shtator 2026, 21:10",
            estimatedArrival = "Depozituar në Trust Wallet ⚡",
            cryptoAmount = "65.40 USDT",
            cryptoNetwork = "BNB Smart Chain (BEP-20)",
            txHash = "0x7c94b219e83214f09a13b6540b07b1d3ef983ca438b4447a550d510257321e84",
            explorerUrl = "https://bscscan.com/tx/0x7c94b219e83214f09a13b6540b07b1d3ef983ca438b4447a550d510257321e84"
        ),
        WithdrawalRecord(
            id = "TK-WD-941824",
            amountEur = 85.00,
            feeEur = 0.00,
            netAmountEur = 85.00,
            method = "Transfer Bankar (IBAN)",
            destinationAccount = "XK05 **** **** **** 8291",
            recipientName = "Albanian Creator",
            status = "Përfunduar me Sukses ✅",
            date = "12 Shtator 2026, 14:20",
            estimatedArrival = "Depozituar në llogari"
        ),
        WithdrawalRecord(
            id = "TK-WD-781903",
            amountEur = 50.00,
            feeEur = 0.00,
            netAmountEur = 50.00,
            method = "PayPal",
            destinationAccount = "al***@gmail.com",
            recipientName = "Albanian Creator",
            status = "Përfunduar me Sukses ✅",
            date = "28 Gusht 2026, 18:45",
            estimatedArrival = "Depozituar në llogari"
        )
    )

    private val _withdrawalHistory = MutableStateFlow<List<WithdrawalRecord>>(initialWithdrawals)
    val withdrawalHistory: StateFlow<List<WithdrawalRecord>> = _withdrawalHistory.asStateFlow()

    private val initialPurchases = listOf(
        CoinPurchaseRecord(
            id = "INV-78491023",
            coins = 350,
            priceEur = 4.99,
            method = "Trust Wallet (USDT)",
            accountDetail = "0x71C3...F19B (BEP-20)",
            date = "12 Shtator 2026, 14:10",
            status = "Kompletuar me Sukses ✅",
            authCode = "AUTH-918234",
            cryptoAmount = "5.44 USDT",
            cryptoNetwork = "BNB Smart Chain (BEP-20)",
            txHash = "0x4b7f8c12e9876a543210fe9876543210abcedf0123456789abcdef0123456789",
            explorerUrl = "https://bscscan.com/tx/0x4b7f8c12e9876a543210fe9876543210abcedf0123456789abcdef0123456789"
        ),
        CoinPurchaseRecord(
            id = "INV-55219482",
            coins = 700,
            priceEur = 9.99,
            method = "Kartë Bankare (Visa)",
            accountDetail = "•••• 4242",
            date = "05 Shtator 2026, 20:30",
            status = "Kompletuar me Sukses ✅",
            authCode = "AUTH-441920"
        )
    )

    private val _purchaseHistory = MutableStateFlow<List<CoinPurchaseRecord>>(initialPurchases)
    val purchaseHistory: StateFlow<List<CoinPurchaseRecord>> = _purchaseHistory.asStateFlow()

    private val _appLanguage = MutableStateFlow("sq") // "sq" or "en"
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    fun setLanguage(lang: String) {
        _appLanguage.value = lang
    }

    val availableCreators = listOf(
        BattleParticipant(
            id = "cr_1",
            name = "Elena Dance 💃",
            handle = "@elenadance",
            avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&fit=crop",
            team = "RED",
            score = 3840,
            likes = 24500,
            topGifters = listOf(
                TopGifter("Marcus", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&fit=crop", 1800, 1),
                TopGifter("Arta K", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&fit=crop", 1240, 2),
                TopGifter("Besnik", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&fit=crop", 800, 3)
            )
        ),
        BattleParticipant(
            id = "cr_2",
            name = "Don Xhoni 🔥",
            handle = "@donxhoni_official",
            avatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&fit=crop",
            team = "BLUE",
            score = 4210,
            likes = 31200,
            topGifters = listOf(
                TopGifter("Valon", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&fit=crop", 2200, 1),
                TopGifter("Krenar", "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150&fit=crop", 1100, 2),
                TopGifter("Ema", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&fit=crop", 910, 3)
            )
        ),
        BattleParticipant(
            id = "cr_3",
            name = "Lina Gamer 🎮",
            handle = "@lina_gaming",
            avatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200&fit=crop",
            team = "RED",
            score = 2950,
            likes = 17800,
            topGifters = listOf(
                TopGifter("Beni", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150&fit=crop", 1400, 1),
                TopGifter("Dardan", "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=150&fit=crop", 950, 2)
            )
        ),
        BattleParticipant(
            id = "cr_4",
            name = "Chef Marco 👨‍🍳",
            handle = "@chef_marco",
            avatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&fit=crop",
            team = "BLUE",
            score = 3100,
            likes = 19200,
            topGifters = listOf(
                TopGifter("Sara", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&fit=crop", 1600, 1),
                TopGifter("Noli", "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=150&fit=crop", 900, 2),
                TopGifter("Vera", "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150&fit=crop", 600, 3)
            )
        )
    )

    private fun createInitialBattleState(mode: BattleMode): LiveBattleState {
        return when (mode) {
            BattleMode.SOLO -> LiveBattleState(
                isActive = false,
                mode = BattleMode.SOLO,
                redParticipants = listOf(availableCreators[0]),
                blueParticipants = emptyList(),
                redTeamScore = 0,
                blueTeamScore = 0
            )
            BattleMode.ONE_VS_ONE -> LiveBattleState(
                isActive = true,
                mode = BattleMode.ONE_VS_ONE,
                timeRemainingSeconds = 180,
                redParticipants = listOf(availableCreators[0]),
                blueParticipants = listOf(availableCreators[1]),
                redTeamScore = 3840,
                blueTeamScore = 4210,
                punishment = "20 Pompa në dysheme 💪"
            )
            BattleMode.TWO_VS_ONE -> LiveBattleState(
                isActive = true,
                mode = BattleMode.TWO_VS_ONE,
                timeRemainingSeconds = 180,
                redParticipants = listOf(availableCreators[0], availableCreators[2]),
                blueParticipants = listOf(availableCreators[1]),
                redTeamScore = 3840 + 2950,
                blueTeamScore = 5820,
                punishment = "Pikturo fytyrën me buzëkuq 💄"
            )
            BattleMode.TWO_VS_TWO -> LiveBattleState(
                isActive = true,
                mode = BattleMode.TWO_VS_TWO,
                timeRemainingSeconds = 180,
                redParticipants = listOf(availableCreators[0], availableCreators[2]),
                blueParticipants = listOf(availableCreators[1], availableCreators[3]),
                redTeamScore = 3840 + 2950,
                blueTeamScore = 4210 + 3100,
                punishment = "Këndo një këngë popullore pa muzikë 🎤"
            )
        }
    }

    private val _battleState = MutableStateFlow(createInitialBattleState(BattleMode.ONE_VS_ONE))
    val battleState: StateFlow<LiveBattleState> = _battleState.asStateFlow()

    private val _liveGoal = MutableStateFlow(LiveGoal())
    val liveGoal: StateFlow<LiveGoal> = _liveGoal.asStateFlow()

    private val _guestRequests = MutableStateFlow(
        listOf(
            LiveGuestRequest("g_req_1", "Arta Vibe", "@arta_vibe", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&fit=crop", "14.2K"),
            LiveGuestRequest("g_req_2", "Valon King", "@valon_king", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&fit=crop", "42.8K"),
            LiveGuestRequest("g_req_3", "Sara Model", "@sara_official", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&fit=crop", "8.9K")
        )
    )
    val guestRequests: StateFlow<List<LiveGuestRequest>> = _guestRequests.asStateFlow()

    fun setBattleMode(mode: BattleMode, customPunishment: String? = null) {
        val newState = createInitialBattleState(mode)
        _battleState.value = if (customPunishment != null) {
            newState.copy(punishment = customPunishment)
        } else {
            newState
        }
    }

    fun startBattle(durationSeconds: Int = 180, punishment: String = "20 Pompa në dysheme 💪") {
        val current = _battleState.value
        _battleState.value = current.copy(
            isActive = true,
            timeRemainingSeconds = durationSeconds,
            durationSeconds = durationSeconds,
            isSpeedUpActive = false,
            isPunishmentPeriod = false,
            winnerTeam = null,
            punishment = punishment
        )
    }

    fun endBattle() {
        val current = _battleState.value
        val winner = when {
            current.redTeamScore > current.blueTeamScore -> "RED"
            current.blueTeamScore > current.redTeamScore -> "BLUE"
            else -> "DRAW"
        }
        _battleState.value = current.copy(
            isActive = false,
            isPunishmentPeriod = true,
            winnerTeam = winner
        )
    }

    fun tickBattleTimer() {
        val current = _battleState.value
        if (!current.isActive || current.timeRemainingSeconds <= 0) return

        val newTime = current.timeRemainingSeconds - 1
        val isSpeedUp = newTime in 30..60 // Speed-up x2 period
        if (newTime <= 0) {
            endBattle()
        } else {
            _battleState.value = current.copy(
                timeRemainingSeconds = newTime,
                isSpeedUpActive = isSpeedUp
            )
        }
    }

    fun addBattlePoints(team: String, points: Long, gifterName: String = "Ti", gifterAvatar: String = "") {
        val current = _battleState.value
        val multiplier = if (current.isSpeedUpActive) 2 else 1
        val finalPoints = points * multiplier

        val updatedRedScore = if (team == "RED") current.redTeamScore + finalPoints else current.redTeamScore
        val updatedBlueScore = if (team == "BLUE") current.blueTeamScore + finalPoints else current.blueTeamScore

        // Update participant gifters
        val updatedRed = if (team == "RED") {
            current.redParticipants.mapIndexed { idx, p ->
                if (idx == 0) {
                    val newGifters = updateTopGifters(p.topGifters, gifterName, gifterAvatar, finalPoints)
                    p.copy(score = p.score + finalPoints, topGifters = newGifters)
                } else p
            }
        } else current.redParticipants

        val updatedBlue = if (team == "BLUE") {
            current.blueParticipants.mapIndexed { idx, p ->
                if (idx == 0) {
                    val newGifters = updateTopGifters(p.topGifters, gifterName, gifterAvatar, finalPoints)
                    p.copy(score = p.score + finalPoints, topGifters = newGifters)
                } else p
            }
        } else current.blueParticipants

        _battleState.value = current.copy(
            redTeamScore = updatedRedScore,
            blueTeamScore = updatedBlueScore,
            redParticipants = updatedRed,
            blueParticipants = updatedBlue
        )
    }

    private fun updateTopGifters(currentGifters: List<TopGifter>, name: String, avatar: String, addedCoins: Long): List<TopGifter> {
        val existing = currentGifters.find { it.name == name }
        val updated = if (existing != null) {
            currentGifters.map {
                if (it.name == name) it.copy(coinsContributed = it.coinsContributed + addedCoins) else it
            }
        } else {
            val userAvatar = if (avatar.isNotBlank()) avatar else _currentUser.value.avatarUrl
            currentGifters + TopGifter(name, userAvatar, addedCoins, currentGifters.size + 1)
        }
        return updated.sortedByDescending { it.coinsContributed }.take(3).mapIndexed { index, tg ->
            tg.copy(rank = index + 1)
        }
    }

    fun tapCheer(team: String) {
        val points = if (_battleState.value.isSpeedUpActive) 2L else 1L
        addBattlePoints(team, points, _currentUser.value.username, _currentUser.value.avatarUrl)
    }

    fun toggleMuteParticipant(participantId: String) {
        val current = _battleState.value
        val updatedRed = current.redParticipants.map {
            if (it.id == participantId) it.copy(isMuted = !it.isMuted) else it
        }
        val updatedBlue = current.blueParticipants.map {
            if (it.id == participantId) it.copy(isMuted = !it.isMuted) else it
        }
        _battleState.value = current.copy(redParticipants = updatedRed, blueParticipants = updatedBlue)
    }

    fun acceptGuestRequest(id: String) {
        val guest = _guestRequests.value.find { it.id == id } ?: return
        _guestRequests.value = _guestRequests.value.filter { it.id != id }

        // If currently in 1vs1, can transition or add to 2vs1 or 2vs2
        val current = _battleState.value
        if (current.mode == BattleMode.ONE_VS_ONE) {
            val newParticipant = BattleParticipant(
                id = guest.id,
                name = guest.name,
                handle = guest.handle,
                avatar = guest.avatar,
                team = "RED",
                score = 1200,
                likes = 3400
            )
            _battleState.value = current.copy(
                mode = BattleMode.TWO_VS_ONE,
                redParticipants = current.redParticipants + newParticipant
            )
        }
    }

    fun rejectGuestRequest(id: String) {
        _guestRequests.value = _guestRequests.value.filter { it.id != id }
    }

    fun updateLiveGoalProgress(incrementBy: Int = 1) {
        val current = _liveGoal.value
        val newCount = (current.currentCount + incrementBy).coerceAtMost(current.targetCount)
        _liveGoal.value = current.copy(currentCount = newCount)
    }

    fun setPunishment(newPunishment: String) {
        _battleState.value = _battleState.value.copy(punishment = newPunishment)
    }

    val notifications = listOf(
        ActivityNotification("n1", "@elenadance", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&fit=crop", "liked your video", "5m ago"),
        ActivityNotification("n2", "@chef_marco", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&fit=crop", "started following you", "1h ago", isFollowAction = true),
        ActivityNotification("n3", "@skater_alex", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=200&fit=crop", "commented: \"Super video bro! 🔥\"", "3h ago"),
        ActivityNotification("n4", "@techuniverse", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&fit=crop", "mentioned you in a comment", "1d ago"),
        ActivityNotification("n5", "TikTok System", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200&fit=crop", "Welcome to TikTok! Start watching & creating!", "3d ago")
    )

    fun toggleLike(videoId: String): Boolean {
        var isNowLiked = false
        _videos.value = _videos.value.map { item ->
            if (item.id == videoId) {
                isNowLiked = !item.isLiked
                item.copy(
                    isLiked = isNowLiked,
                    likesCount = if (isNowLiked) item.likesCount + 1 else item.likesCount - 1
                )
            } else item
        }
        return isNowLiked
    }

    fun likeVideo(videoId: String) {
        _videos.value = _videos.value.map { item ->
            if (item.id == videoId && !item.isLiked) {
                item.copy(
                    isLiked = true,
                    likesCount = item.likesCount + 1
                )
            } else item
        }
    }

    fun toggleFavorite(videoId: String): Boolean {
        var isNowFavorited = false
        _videos.value = _videos.value.map { item ->
            if (item.id == videoId) {
                isNowFavorited = !item.isFavorited
                item.copy(
                    isFavorited = isNowFavorited,
                    favoritesCount = if (isNowFavorited) item.favoritesCount + 1 else item.favoritesCount - 1
                )
            } else item
        }
        return isNowFavorited
    }

    fun toggleRepost(videoId: String): Boolean {
        var isNowReposted = false
        _videos.value = _videos.value.map { item ->
            if (item.id == videoId) {
                isNowReposted = !item.isReposted
                item.copy(
                    isReposted = isNowReposted,
                    repostsCount = if (isNowReposted) item.repostsCount + 1 else item.repostsCount - 1
                )
            } else item
        }
        return isNowReposted
    }

    fun toggleFollow(authorHandle: String): Boolean {
        var isNowFollowing = false
        _videos.value = _videos.value.map { item ->
            if (item.authorHandle == authorHandle) {
                isNowFollowing = !item.isFollowing
                item.copy(isFollowing = isNowFollowing)
            } else item
        }
        return isNowFollowing
    }

    fun addComment(videoId: String, text: String): TikTokComment? {
        if (text.isBlank()) return null
        val user = _currentUser.value
        val newComment = TikTokComment(
            id = UUID.randomUUID().toString(),
            authorName = user.username,
            authorHandle = user.handle,
            authorAvatar = user.avatarUrl,
            text = text.trim(),
            timestamp = "Just now",
            likes = 0,
            isLiked = false
        )
        _videos.value = _videos.value.map { item ->
            if (item.id == videoId) {
                item.copy(
                    comments = listOf(newComment) + item.comments,
                    commentsCount = item.commentsCount + 1
                )
            } else item
        }
        return newComment
    }

    fun deleteComment(videoId: String, commentId: String) {
        _videos.value = _videos.value.map { item ->
            if (item.id == videoId) {
                item.copy(
                    comments = item.comments.filter { it.id != commentId },
                    commentsCount = (item.commentsCount - 1).coerceAtLeast(0)
                )
            } else item
        }
    }

    fun likeComment(videoId: String, commentId: String) {
        _videos.value = _videos.value.map { item ->
            if (item.id == videoId) {
                item.copy(
                    comments = item.comments.map { c ->
                        if (c.id == commentId) {
                            val liked = !c.isLiked
                            c.copy(
                                isLiked = liked,
                                likes = if (liked) c.likes + 1 else c.likes - 1
                            )
                        } else c
                    }
                )
            } else item
        }
    }

    fun buyCoins(coinsCount: Long, priceEur: Double) {
        val u = _currentUser.value
        _currentUser.value = u.copy(coins = u.coins + coinsCount)
    }

    fun buyCoinsReal(
        coinsCount: Long,
        priceEur: Double,
        method: String,
        accountDetail: String,
        cryptoAmount: String = "",
        cryptoNetwork: String = "",
        txHash: String = "",
        explorerUrl: String = ""
    ): CoinPurchaseRecord {
        val u = _currentUser.value
        _currentUser.value = u.copy(coins = u.coins + coinsCount)

        val now = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.US).format(java.util.Date())
        val authCode = "AUTH-" + (100000..999999).random()
        val record = CoinPurchaseRecord(
            id = "INV-" + java.util.UUID.randomUUID().toString().take(8).uppercase(),
            coins = coinsCount,
            priceEur = priceEur,
            method = method,
            accountDetail = accountDetail,
            date = now,
            status = "Kompletuar me Sukses ✅",
            authCode = authCode,
            cryptoAmount = cryptoAmount,
            cryptoNetwork = cryptoNetwork,
            txHash = txHash,
            explorerUrl = explorerUrl
        )
        _purchaseHistory.value = listOf(record) + _purchaseHistory.value
        return record
    }

    fun sendGift(gift: VirtualGift, targetUser: String = "Creator", count: Int = 1, team: String = "RED"): Boolean {
        val totalCoins = gift.coins * count
        val u = _currentUser.value
        if (u.coins < totalCoins) return false
        _currentUser.value = u.copy(
            coins = u.coins - totalCoins,
            diamondEarnings = u.diamondEarnings + (totalCoins * 0.005)
        )
        // Award points in battle
        addBattlePoints(team, totalCoins.toLong(), u.username, u.avatarUrl)
        updateLiveGoalProgress(count)
        return true
    }

    fun withdrawEarnings(
        amount: Double,
        method: String = "Transfer Bankar",
        rawDestination: String = "",
        recipientName: String = "",
        cryptoAmount: String = "",
        cryptoNetwork: String = "",
        txHash: String = "",
        explorerUrl: String = ""
    ): WithdrawalRecord? {
        val u = _currentUser.value
        if (amount > u.diamondEarnings || amount <= 0) return null

        val user = _currentUser.value
        _currentUser.value = u.copy(diamondEarnings = u.diamondEarnings - amount)

        val isCrypto = method.contains("Trust Wallet", ignoreCase = true) || method.contains("Kripto", ignoreCase = true)

        val maskedDestination = when {
            isCrypto && rawDestination.length > 10 -> {
                val start = rawDestination.take(6)
                val end = rawDestination.takeLast(4)
                "$start...$end (Trust Wallet)"
            }
            rawDestination.contains("@") -> {
                val parts = rawDestination.split("@")
                val namePart = parts[0]
                val visible = if (namePart.length > 2) namePart.take(2) else namePart.take(1)
                "$visible***@${parts.getOrElse(1) { "mail.com" }}"
            }
            rawDestination.length > 8 -> {
                val start = rawDestination.take(4)
                val end = rawDestination.takeLast(4)
                "$start **** **** $end"
            }
            rawDestination.isNotBlank() -> rawDestination
            else -> "Llogaria e lidhur me TikTok"
        }

        val refId = if (isCrypto) "TK-CRYPTO-" + (100000..999999).random() else "TK-WD-" + (100000..999999).random()
        val generatedTx = if (isCrypto && txHash.isBlank()) {
            val hexChars = "0123456789abcdef"
            "0x" + (1..64).map { hexChars.random() }.joinToString("")
        } else txHash

        val generatedExplorer = if (isCrypto && explorerUrl.isBlank()) {
            if (cryptoNetwork.contains("TRON", ignoreCase = true)) {
                "https://tronscan.org/#/transaction/${generatedTx.removePrefix("0x")}"
            } else {
                "https://bscscan.com/tx/$generatedTx"
            }
        } else explorerUrl

        val newRecord = WithdrawalRecord(
            id = refId,
            amountEur = amount,
            feeEur = 0.00,
            netAmountEur = amount,
            method = method,
            destinationAccount = maskedDestination,
            recipientName = recipientName.ifBlank { user.username },
            status = if (isCrypto) "Përfunduar në Blockchain ⚡" else "Në Proces (Brenda 24h) ⏳",
            date = "Sot, " + java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
            estimatedArrival = when {
                isCrypto -> "1 - 3 minuta (Depozituar në Trust Wallet)"
                method == "PayPal" -> "Brenda 30 minutash"
                method.contains("Kartë") -> "Brenda 2-4 orëve"
                method.contains("Western Union") -> "Kodi MTCN aktiv pas 15 minutash"
                else -> "Brenda 24-48 orëve"
            },
            cryptoAmount = cryptoAmount,
            cryptoNetwork = cryptoNetwork,
            txHash = generatedTx,
            explorerUrl = generatedExplorer
        )

        _withdrawalHistory.value = listOf(newRecord) + _withdrawalHistory.value
        return newRecord
    }

    fun sendMessage(conversationId: String, text: String, giftEmoji: String? = null) {
        if (text.isBlank() && giftEmoji == null) return
        val user = _currentUser.value
        val newMsg = DirectMessage(
            id = UUID.randomUUID().toString(),
            senderHandle = user.handle,
            text = text,
            timestamp = "Just now",
            isMe = true,
            giftEmoji = giftEmoji
        )
        _conversations.value = _conversations.value.map { conv ->
            if (conv.id == conversationId) {
                conv.copy(messages = conv.messages + newMsg)
            } else conv
        }
    }

    fun addLiveComment(streamId: String, text: String, giftEmoji: String? = null) {
        val user = _currentUser.value
        val newComment = LiveComment(
            id = UUID.randomUUID().toString(),
            user = user.username,
            avatar = user.avatarUrl,
            text = text,
            giftEmoji = giftEmoji
        )
        _liveStreams.value = _liveStreams.value.map { s ->
            if (s.id == streamId) {
                s.copy(comments = s.comments + newComment)
            } else s
        }
    }

    fun startLiveStream(title: String, category: String): LiveStreamItem {
        val user = _currentUser.value
        val newStream = LiveStreamItem(
            id = "live_${System.currentTimeMillis()}",
            hostName = user.username + " 🔴",
            hostHandle = user.handle,
            hostAvatar = user.avatarUrl,
            title = if (title.isNotBlank()) title else "LIVE me ndjekësit! 🔥",
            viewersCount = "1",
            category = category,
            videoUrl = "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_1MB.mp4",
            comments = listOf(
                LiveComment("lc_sys", "TikTok LIVE", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200&fit=crop", "Transmetimi juaj filloi me sukses! Ndani me miqtë!")
            )
        )
        _liveStreams.value = listOf(newStream) + _liveStreams.value
        return newStream
    }

    fun updateUserProfile(name: String, bio: String, website: String) {
        val u = _currentUser.value
        _currentUser.value = u.copy(
            username = name.ifBlank { u.username },
            bio = bio,
            website = website
        )
    }

    fun togglePrivateAccount(): Boolean {
        val u = _currentUser.value
        val newState = !u.isPrivate
        _currentUser.value = u.copy(isPrivate = newState)
        return newState
    }

    fun login(identifier: String, name: String) {
        _currentUser.value = _currentUser.value.copy(
            isLoggedIn = true,
            username = name.ifBlank { "User_${System.currentTimeMillis() % 10000}" },
            email = if (identifier.contains("@")) identifier else "user@tiktok.com",
            phone = if (!identifier.contains("@")) identifier else "+383 49 999 888"
        )
    }

    fun logout() {
        _currentUser.value = _currentUser.value.copy(isLoggedIn = false)
    }

    fun addCreatedVideo(caption: String, videoUrl: String = "") {
        val user = _currentUser.value
        val defaultUrl = if (videoUrl.isNotBlank()) videoUrl else "https://test-videos.co.uk/vids/sintel/mp4/h264/1080/Sintel_1080_10s_2MB.mp4"
        val newVideo = VideoItem(
            id = "vid_${System.currentTimeMillis()}",
            videoUrl = defaultUrl,
            authorName = user.username,
            authorHandle = user.handle,
            authorAvatar = user.avatarUrl,
            isVerified = true,
            isFollowing = true,
            caption = caption,
            tags = listOf("#new", "#tiktok", "#viral", "#foryou"),
            soundTitle = "Original Sound - " + user.username,
            soundAuthor = user.username,
            soundAlbumArt = user.avatarUrl,
            likesCount = 1,
            commentsCount = 0,
            favoritesCount = 0,
            sharesCount = 0,
            isLiked = true,
            isFavorited = false,
            primaryColor = 0xFF120E2C,
            secondaryColor = 0xFF35124A
        )
        _videos.value = listOf(newVideo) + _videos.value
    }

    fun formatCount(count: Long): String {
        return when {
            count >= 1_000_000 -> {
                val formatted = String.format("%.1f", count / 1_000_000.0)
                "${formatted.replace(".0", "")}M"
            }
            count >= 1_000 -> {
                val formatted = String.format("%.1f", count / 1_000.0)
                "${formatted.replace(".0", "")}K"
            }
            else -> count.toString()
        }
    }
}
