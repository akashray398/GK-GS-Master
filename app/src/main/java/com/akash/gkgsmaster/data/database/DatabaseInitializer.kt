package com.akash.gkgsmaster.data.database

import com.akash.gkgsmaster.data.model.*

object DatabaseInitializer {

    fun getInitialQuestions(): List<Question> {
        return listOf(
            Question(
                id = "q1",
                text = "Which of the following Fundamental Rights is available only to the citizens of India?",
                options = listOf(
                    "Right to Equality before law",
                    "Freedom of speech and expression",
                    "Right to life and personal liberty",
                    "Religious freedom"
                ),
                correctOptionIndex = 1,
                category = "Polity",
                difficulty = "Medium",
                explanation = "Article 19 (Freedom of speech and expression) is available only to citizens.",
                whyCorrect = "Article 19 guarantees six freedoms to citizens only.",
                whyOthersIncorrect = "Articles 14, 20, 21, 21A, 22, 23, 24, 25, 26, 27 and 28 are available to both citizens and foreigners.",
                relatedFacts = listOf("Magna Carta of India", "Part III of Constitution"),
                upscTip = "Memorize Articles 15, 16, 19, 29, 30 as they are exclusively for citizens.",
                referenceTopic = "Fundamental Rights"
            ),
            Question(
                id = "q2",
                text = "Who among the following was the Chairman of the Drafting Committee of the Indian Constitution?",
                options = listOf(
                    "Dr. Rajendra Prasad",
                    "Jawaharlal Nehru",
                    "Dr. B.R. Ambedkar",
                    "Sardar Vallabhbhai Patel"
                ),
                correctOptionIndex = 2,
                category = "Polity",
                difficulty = "Easy",
                explanation = "Dr. B.R. Ambedkar was the chairman of the Drafting Committee.",
                whyCorrect = "The Drafting Committee was set up on August 29, 1947, with Dr. B.R. Ambedkar as Chairman.",
                whyOthersIncorrect = "Dr. Rajendra Prasad was President of Constituent Assembly. Nehru was Chairman of Union Constitution Committee.",
                relatedFacts = listOf("Father of the Constitution", "Modern Manu"),
                upscTip = "Committees of the Constituent Assembly are frequently asked in Prelims.",
                referenceTopic = "Making of the Constitution"
            ),
            Question(
                id = "q3",
                text = "The 'Instrument of Instructions' contained in the Government of India Act 1935 has been incorporated in the Constitution of India in the year 1950 as:",
                options = listOf(
                    "Fundamental Rights",
                    "Directive Principles of State Policy",
                    "Extent of executive power of State",
                    "Conduct of business of the Government of India"
                ),
                correctOptionIndex = 1,
                category = "Polity",
                difficulty = "Hard",
                explanation = "DPSP were originally 'Instrument of Instructions'.",
                whyCorrect = "DPSP resemble the 'Instrument of Instructions' enumerated in the GOI Act of 1935.",
                whyOthersIncorrect = "Fundamental Rights are justiciable; DPSP are not.",
                upscTip = "UPSC often asks comparisons between 1935 Act and the Constitution."
            ),
            Question(
                id = "q4",
                text = "Which of the following is not a member of G7?",
                options = listOf("France", "Germany", "Russia", "Japan"),
                correctOptionIndex = 2,
                category = "International Relations",
                difficulty = "Medium",
                explanation = "Russia was suspended from G8 in 2014.",
                whyCorrect = "The G7 consists of Canada, France, Germany, Italy, Japan, the UK, and the US."
            ),
            Question(
                id = "q5",
                text = "The concept of 'Carbon Credit' originated from which one of the following?",
                options = listOf("Earth Summit", "Kyoto Protocol", "Montreal Protocol", "G-8 Summit"),
                correctOptionIndex = 1,
                category = "Environment",
                difficulty = "Medium",
                explanation = "Carbon Credit originated from Kyoto Protocol.",
                whyCorrect = "Kyoto Protocol (1997) introduced the concept of carbon trading/credits."
            ),
            Question(
                id = "q6",
                text = "Who was the first Governor-General of Bengal?",
                options = listOf("Robert Clive", "Warren Hastings", "Lord William Bentinck", "Lord Cornwallis"),
                correctOptionIndex = 1,
                category = "History",
                difficulty = "Easy",
                explanation = "Warren Hastings was the first Governor-General of Bengal under Regulating Act 1773."
            ),
            Question(
                id = "q7",
                text = "The headquarters of the International Court of Justice is at:",
                options = listOf("Geneva", "The Hague", "Vienna", "New York"),
                correctOptionIndex = 1,
                category = "International Organizations",
                difficulty = "Easy",
                explanation = "ICJ is located at Peace Palace in The Hague, Netherlands."
            ),
            Question(
                id = "q8",
                text = "Which part of the Constitution of India is known as the 'Magna Carta of India'?",
                options = listOf("Part II", "Part III", "Part IV", "Part IVA"),
                correctOptionIndex = 1,
                category = "Polity",
                difficulty = "Easy",
                explanation = "Part III (Fundamental Rights) is called the Magna Carta of India."
            ),
            Question(
                id = "q9",
                text = "The 'Cripps Mission' visited India in which year?",
                options = listOf("1940", "1942", "1945", "1946"),
                correctOptionIndex = 1,
                category = "History",
                difficulty = "Medium",
                explanation = "Cripps Mission came to India in March 1942."
            ),
            Question(
                id = "q10",
                text = "Which of the following indices is published by NITI Aayog?",
                options = listOf("SDG India Index", "Global Innovation Index", "Human Development Index", "Ease of Doing Business"),
                correctOptionIndex = 0,
                category = "Economics",
                difficulty = "Medium",
                explanation = "SDG India Index is published by NITI Aayog."
            )
        )
    }

    fun getInitialBooklets(): List<Booklet> {
        val booklets = mutableListOf<Booklet>()

        // 1. Recommended Reference Books (Metadata only)
        val recommendedBooks = listOf(
            "Indian Polity – M Laxmikanth" to "M Laxmikanth",
            "Introduction to the Constitution of India – D D Basu" to "D D Basu",
            "Our Constitution" to "Subhash Kashyap",
            "Our Parliament" to "Subhash Kashyap",
            "A Brief History of Modern India" to "Spectrum",
            "India's Ancient Past" to "R.S. Sharma",
            "History of Medieval India" to "Satish Chandra",
            "India Since Independence" to "Bipan Chandra"
        )

        recommendedBooks.forEachIndexed { index, pair ->
            booklets.add(
                Booklet(
                    id = "rec_${index + 1}",
                    title = pair.first,
                    author = pair.second,
                    category = "Recommended Books",
                    type = BookletType.RECOMMENDED_BOOK,
                    description = "Standard reference book for UPSC CSE preparation.",
                    upscRelevance = "Essential for Prelims and Mains.",
                    difficulty = "Medium",
                    officialSourceUrl = "https://www.google.com/search?q=${pair.first}"
                )
            )
        }

        // 2. Original Study Materials
        val originalTopics = listOf(
            "Indian Constitution", "Indian Polity", "Indian Economy", "Ancient History",
            "Medieval History", "Modern History", "Geography", "Environment", "Ecology",
            "Physics", "Chemistry", "Biology", "Current Affairs", "Science & Technology",
            "International Relations", "Ethics", "Disaster Management", "Agriculture"
        )

        originalTopics.forEachIndexed { index, title ->
            booklets.add(
                Booklet(
                    id = "orig_${index + 1}",
                    title = title,
                    author = "GKGS Master Editorial Team",
                    category = "Study Material",
                    type = BookletType.ORIGINAL_BOOKLET,
                    description = "Complete original study notes on $title for UPSC aspirants.",
                    upscRelevance = "Directly covers key syllabus points.",
                    difficulty = "Medium",
                    totalChapters = 2,
                    chapters = listOf(
                        Chapter(
                            id = "orig_${index}_ch1",
                            title = "Chapter 1: Key Concepts",
                            order = 1,
                            pages = listOf(
                                BookletPage("orig_${index}_p1", 1, "Original content for $title part 1..."),
                                BookletPage("orig_${index}_p2", 2, "Original content for $title part 2...")
                            )
                        )
                    )
                )
            )
        }

        return booklets
    }

    fun getInitialLearningTopics(): List<LearningTopicEntity> {
        return listOf(
            LearningTopicEntity(
                id = "lt1",
                title = "Fundamental Rights",
                description = "Basic human rights guaranteed by the Indian Constitution.",
                category = "Polity",
                content = "Part III of the Indian Constitution (Articles 12-35) deals with Fundamental Rights...",
                highlights = "Originally 7 rights, now 6 after removal of Right to Property.",
                importantFacts = listOf("Magna Carta of India", "Justiciable"),
                bulletPoints = listOf("Right to Equality", "Right to Freedom")
            )
        )
    }
}
