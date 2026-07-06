package com.akash.gkgsmaster.data.model

object BookletDataGenerator {
    fun getInitialBooklets(): List<Booklet> {
        return listOf(
            Booklet(
                id = "polity_laxmikanth",
                title = "Indian Polity",
                author = "M. Laxmikanth",
                subject = "Polity",
                description = "The most comprehensive book for UPSC and other competitive exams covering Indian political system.",
                coverImageUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?q=80&w=2070&auto=format&fit=crop",
                category = "Polity",
                type = BookletType.RECOMMENDED_BOOK,
                totalChapters = 76
            ),
            Booklet(
                id = "const_dd_basu",
                title = "Introduction to the Constitution of India",
                author = "D.D. Basu",
                subject = "Constitution",
                description = "An analytical and comparative study of the Constitution of India.",
                coverImageUrl = "https://images.unsplash.com/photo-1505664194779-8beaceb93744?q=80&w=2070&auto=format&fit=crop",
                category = "Polity",
                type = BookletType.RECOMMENDED_BOOK,
                totalChapters = 32
            ),
            Booklet(
                id = "our_constitution",
                title = "Our Constitution",
                author = "Subhash Kashyap",
                subject = "Polity",
                description = "A classic introduction to the Indian Constitution, its history, and features.",
                coverImageUrl = "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?q=80&w=2070&auto=format&fit=crop",
                category = "Polity",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "our_parliament",
                title = "Our Parliament",
                author = "Subhash Kashyap",
                subject = "Polity",
                description = "Comprehensive guide to the functions and history of the Indian Parliament.",
                coverImageUrl = "https://images.unsplash.com/photo-1590212151175-e58edd96185b?q=80&w=2070&auto=format&fit=crop",
                category = "Polity",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "working_demo_const",
                title = "Working a Democratic Constitution",
                author = "Granville Austin",
                subject = "Polity",
                description = "A definitive history of the Indian Constitution's first three decades.",
                coverImageUrl = "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?q=80&w=2070&auto=format&fit=crop",
                category = "Polity",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "const_govt_india",
                title = "Constitutional Government in India",
                author = "M.V. Pylee",
                subject = "Polity",
                description = "A standard text on the working of the Indian government.",
                coverImageUrl = "https://images.unsplash.com/photo-1450101499163-c8848c66ca85?q=80&w=2070&auto=format&fit=crop",
                category = "Polity",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "oxford_politics_india",
                title = "Oxford Companion to Politics in India",
                author = "Niraja Gopal Jayal",
                subject = "Politics",
                description = "Essays exploring the various dimensions of Indian politics.",
                coverImageUrl = "https://images.unsplash.com/photo-1529107386315-e1a2ed48a620?q=80&w=2070&auto=format&fit=crop",
                category = "Politics",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "governance_india",
                title = "Governance in India",
                author = "M. Laxmikanth",
                subject = "Governance",
                description = "Covers the aspects of governance, public policy, and administrative systems.",
                coverImageUrl = "https://images.unsplash.com/photo-1517048676732-d65bc937f952?q=80&w=2070&auto=format&fit=crop",
                category = "Polity",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "india_ancient_past",
                title = "India's Ancient Past",
                author = "R.S. Sharma",
                subject = "History",
                description = "A comprehensive narrative of the history of ancient India.",
                coverImageUrl = "https://images.unsplash.com/photo-1599308149176-787d605bc035?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "medieval_india",
                title = "History of Medieval India",
                author = "Satish Chandra",
                subject = "History",
                description = "Details the transition from ancient to modern India during the medieval period.",
                coverImageUrl = "https://images.unsplash.com/photo-1548013146-72479768bbaa?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "modern_india_brief",
                title = "A Brief History of Modern India",
                author = "Spectrum Publications",
                subject = "History",
                description = "Concise summary of modern Indian history, essential for competitive exams.",
                coverImageUrl = "https://images.unsplash.com/photo-1532375810709-75b1da00537c?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "struggle_independence",
                title = "India's Struggle for Independence",
                author = "Bipan Chandra",
                subject = "History",
                description = "A detailed analysis of the Indian freedom movement.",
                coverImageUrl = "https://images.unsplash.com/photo-1572953109213-3be62398eb95?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "india_since_indep",
                title = "India Since Independence",
                author = "Bipan Chandra",
                subject = "History",
                description = "Explores the challenges and progress of post-independence India.",
                coverImageUrl = "https://images.unsplash.com/photo-1582650625119-3a31f8fa2699?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "plassey_to_partition",
                title = "From Plassey to Partition",
                author = "Sekhar Bandyopadhyay",
                subject = "History",
                description = "A comprehensive history of modern India and its social changes.",
                coverImageUrl = "https://images.unsplash.com/photo-1594122230689-45899d9e6f69?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "wonder_that_was_india",
                title = "The Wonder That Was India",
                author = "A.L. Basham",
                subject = "History",
                description = "A classic survey of the culture of sub-continental India before the arrival of the Muslims.",
                coverImageUrl = "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "discovery_of_india",
                title = "The Discovery of India",
                author = "Jawaharlal Nehru",
                subject = "History",
                description = "Written by India's first Prime Minister, exploring India's history and philosophy.",
                coverImageUrl = "https://images.unsplash.com/photo-1562676505-15b63013d39f?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "ancient_india_metadata",
                title = "Ancient India",
                author = "Upinder Singh",
                subject = "History",
                description = "A history of ancient and early medieval India.",
                coverImageUrl = "https://images.unsplash.com/photo-1544006659-f0b21f04cb1d?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "modern_india_bipan",
                title = "History of Modern India",
                author = "Bipan Chandra",
                subject = "History",
                description = "Focuses on the period from the 18th century to independence.",
                coverImageUrl = "https://images.unsplash.com/photo-1582230864696-7649d2170361?q=80&w=2070&auto=format&fit=crop",
                category = "History",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "ncert_history_vixii",
                title = "NCERT History VI-XII",
                author = "NCERT",
                subject = "History",
                description = "Foundational history textbooks for all school levels.",
                coverImageUrl = "https://images.unsplash.com/photo-1497633762265-9a177c809dd3?q=80&w=2070&auto=format&fit=crop",
                category = "Education",
                type = BookletType.ORIGINAL_BOOKLET
            ),
            Booklet(
                id = "ncert_polsci_ixxii",
                title = "NCERT Political Science IX-XII",
                author = "NCERT",
                subject = "Polity",
                description = "Core political science concepts from school curriculum.",
                coverImageUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?q=80&w=2070&auto=format&fit=crop",
                category = "Education",
                type = BookletType.ORIGINAL_BOOKLET
            ),
            Booklet(
                id = "prs_legislative",
                title = "PRS Legislative Research",
                author = "PRS",
                subject = "Governance",
                description = "Analytical reports on various legislative bills and policies.",
                coverImageUrl = "https://images.unsplash.com/photo-1507842217343-583bb7270b66?q=80&w=2070&auto=format&fit=crop",
                category = "Governance",
                type = BookletType.ORIGINAL_BOOKLET
            ),
            Booklet(
                id = "niti_aayog_reports",
                title = "NITI Aayog Reports",
                author = "NITI Aayog",
                subject = "Economy",
                description = "Strategic policy reports from the government's premier think tank.",
                coverImageUrl = "https://images.unsplash.com/photo-1460925895917-afdab827c52f?q=80&w=2070&auto=format&fit=crop",
                category = "Economy",
                type = BookletType.ORIGINAL_BOOKLET
            ),
            Booklet(
                id = "election_comm_reports",
                title = "Election Commission Reports",
                author = "ECI",
                subject = "Polity",
                description = "Reports on electoral reforms and process in India.",
                coverImageUrl = "https://images.unsplash.com/photo-1540910419892-4a39d80b6644?q=80&w=2070&auto=format&fit=crop",
                category = "Polity",
                type = BookletType.ORIGINAL_BOOKLET
            ),
            Booklet(
                id = "law_comm_reports",
                title = "Law Commission Reports",
                author = "Law Commission",
                subject = "Law",
                description = "Recommendations on legal reforms in India.",
                coverImageUrl = "https://images.unsplash.com/photo-1505664194779-8beaceb93744?q=80&w=2070&auto=format&fit=crop",
                category = "Governance",
                type = BookletType.ORIGINAL_BOOKLET
            ),
            Booklet(
                id = "pib_news",
                title = "Press Information Bureau",
                author = "PIB",
                subject = "Current Affairs",
                description = "Official government press releases and information updates.",
                coverImageUrl = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?q=80&w=2070&auto=format&fit=crop",
                category = "Current Affairs",
                type = BookletType.ORIGINAL_BOOKLET
            ),
            Booklet(
                id = "epw_journal",
                title = "Economic & Political Weekly",
                author = "EPW",
                subject = "General",
                description = "Prestigious social science journal covering economy and politics.",
                coverImageUrl = "https://images.unsplash.com/photo-1495020689067-958852a7765e?q=80&w=2070&auto=format&fit=crop",
                category = "Politics",
                type = BookletType.RECOMMENDED_BOOK
            ),
            Booklet(
                id = "the_hindu_editorial",
                title = "The Hindu Editorial",
                author = "The Hindu",
                subject = "Current Affairs",
                description = "Expert analysis of daily news events.",
                coverImageUrl = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?q=80&w=2070&auto=format&fit=crop",
                category = "Current Affairs",
                type = BookletType.ORIGINAL_BOOKLET
            ),
            Booklet(
                id = "indian_express_editorial",
                title = "Indian Express Editorial",
                author = "Indian Express",
                subject = "Current Affairs",
                description = "In-depth editorial analysis and opinion pieces.",
                coverImageUrl = "https://images.unsplash.com/photo-1495020689067-958852a7765e?q=80&w=2070&auto=format&fit=crop",
                category = "Current Affairs",
                type = BookletType.ORIGINAL_BOOKLET
            )
        )
    }
}
