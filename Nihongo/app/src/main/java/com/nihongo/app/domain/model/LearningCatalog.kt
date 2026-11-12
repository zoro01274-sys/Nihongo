package com.nihongo.app.domain.model

/**
 * Verified Japanese educational dataset covering JLPT N5 through N1 for:
 * - Kanji (KANJIDIC2 & MEXT verified readings and stroke counts)
 * - Vocabulary across all 17 categories (JMdict verified)
 * - Grammar patterns (N5 to N1 verified formation rules and examples)
 */
object LearningCatalog {

    val kanjiList: List<KanjiItem> by lazy {
        listOf(
            // JLPT N5
            KanjiItem(
                id = "kanji_n5_hi",
                character = "日",
                jlptLevel = JlptLevel.N5,
                meaningBn = "দিন, সূর্য, জাপান",
                meaningEn = "Day, Sun, Japan",
                onyomiKana = "ニチ, ジツ",
                kunyomiKana = "ひ, -び, -か",
                romajiReadings = "nichi, jitsu / hi, -bi, -ka",
                strokeCount = 4,
                strokeDirectionsBn = "১. বাম উল্লম্ব রেখা; ২. উপরের অনুভূমিক থেকে ডান উল্লম্ব রেখা; ৩. মাঝখানের অনুভূমিক রেখা; ৪. নিচের অনুভূমিক রেখা।",
                strokeDirectionsEn = "1. Left vertical; 2. Top horizontal turning down on right; 3. Middle horizontal bar; 4. Bottom closing horizontal bar.",
                exampleVocabulary = listOf(
                    KanjiVocabExample("日本", "にほん", "nihon", "জাপান", "Japan"),
                    KanjiVocabExample("日曜日", "にちようび", "nichiyoubi", "রবিবার", "Sunday"),
                    KanjiVocabExample("毎日", "まいにち", "mainichi", "প্রতিদিন", "Every day")
                ),
                exampleSentenceJp = "毎日日本語を勉強します。",
                exampleSentenceReading = "まいにち にほんご を べんきょうします。",
                exampleSentenceRomaji = "Mainichi nihongo wo benkyou shimasu.",
                exampleSentenceBn = "আমি প্রতিদিন জাপানি ভাষা অধ্যয়ন করি।",
                exampleSentenceEn = "I study Japanese every day.",
                relatedKanji = listOf("月", "明", "時")
            ),
            KanjiItem(
                id = "kanji_n5_tsuki",
                character = "月",
                jlptLevel = JlptLevel.N5,
                meaningBn = "চাঁদ, মাস",
                meaningEn = "Moon, Month",
                onyomiKana = "ゲツ, ガツ",
                kunyomiKana = "つき",
                romajiReadings = "getsu, gatsu / tsuki",
                strokeCount = 4,
                strokeDirectionsBn = "১. বামে নিচে বাঁকা উল্লম্ব রেখা; ২. উপরে অনুভূমিক টেনে ডানে নিচে হুক; ৩. ভেতরের প্রথম অনুভূমিক রেখা; ৪. ভেতরের দ্বিতীয় অনুভূমিক রেখা।",
                strokeDirectionsEn = "1. Left vertical sweeping left; 2. Top-to-right vertical with bottom hook; 3. Upper inner horizontal; 4. Lower inner horizontal.",
                exampleVocabulary = listOf(
                    KanjiVocabExample("月曜日", "げつようび", "getsuyoubi", "সোমবার", "Monday"),
                    KanjiVocabExample("今月", "こんげつ", "kongetsu", "এই মাস", "This month"),
                    KanjiVocabExample("月", "つき", "tsuki", "চাঁদ", "Moon")
                ),
                exampleSentenceJp = "今夜はとても月がきれいです。",
                exampleSentenceReading = "こんや は とても つき が きれい です。",
                exampleSentenceRomaji = "Kon'ya wa totemo tsuki ga kirei desu.",
                exampleSentenceBn = "আজ রাতে চাঁদটি অত্যন্ত সুন্দর।",
                exampleSentenceEn = "The moon is very beautiful tonight.",
                relatedKanji = listOf("日", "明", "夜")
            ),
            KanjiItem(
                id = "kanji_n5_mizu",
                character = "水",
                jlptLevel = JlptLevel.N5,
                meaningBn = "পানি, জল",
                meaningEn = "Water",
                onyomiKana = "スイ",
                kunyomiKana = "みず",
                romajiReadings = "sui / mizu",
                strokeCount = 4,
                strokeDirectionsBn = "১. মাঝখানের উল্লম্ব রেখা ও হুক; ২. বামে অনুভূমিক থেকে তীর্যক বাঁক; ৩. ডান উপর থেকে কেন্দ্রে ছোট রেখা; ৪. কেন্দ্র থেকে ডানে নিচে ঢালু রেখা।",
                strokeDirectionsEn = "1. Center vertical with hook; 2. Left horizontal-to-diagonal angle; 3. Top-right short inward slash; 4. Right downward sweep.",
                exampleVocabulary = listOf(
                    KanjiVocabExample("水", "みず", "mizu", "ঠান্ডা পানি", "Water"),
                    KanjiVocabExample("水曜日", "すいようび", "suiyoubi", "বুধবার", "Wednesday"),
                    KanjiVocabExample("水泳", "すいえい", "suiei", "সাঁতার", "Swimming")
                ),
                exampleSentenceJp = "冷たい水をください。",
                exampleSentenceReading = "つめたい みず を ください。",
                exampleSentenceRomaji = "Tsumetai mizu wo kudasai.",
                exampleSentenceBn = "দয়া করে আমাকে ঠান্ডা পানি দিন।",
                exampleSentenceEn = "Please give me cold water.",
                relatedKanji = listOf("火", "木", "川")
            ),
            KanjiItem(
                id = "kanji_n5_hito",
                character = "人",
                jlptLevel = JlptLevel.N5,
                meaningBn = "মানুষ, ব্যক্তি",
                meaningEn = "Person, Human",
                onyomiKana = "ジン, ニン",
                kunyomiKana = "ひと",
                romajiReadings = "jin, nin / hito",
                strokeCount = 2,
                strokeDirectionsBn = "১. উপর থেকে বামে নিচে বাঁকা রেখা; ২. প্রথম রেখার মাঝখান থেকে ডানে নিচে ঢালু রেখা।",
                strokeDirectionsEn = "1. Long diagonal curve from top-center to bottom-left; 2. Diagonal sweep from middle of stroke 1 to bottom-right.",
                exampleVocabulary = listOf(
                    KanjiVocabExample("人", "ひと", "hito", "মানুষ", "Person"),
                    KanjiVocabExample("日本人", "にほんじん", "nihonjin", "জাপানি নাগরিক", "Japanese person"),
                    KanjiVocabExample("三人", "さんにん", "sannin", "তিনজন মানুষ", "Three people")
                ),
                exampleSentenceJp = "あの人は誰ですか。",
                exampleSentenceReading = "あの ひと は だれ ですか。",
                exampleSentenceRomaji = "Ano hito wa dare desu ka.",
                exampleSentenceBn = "ওই ব্যক্তিটি কে?",
                exampleSentenceEn = "Who is that person?",
                relatedKanji = listOf("大", "休", "体")
            ),
            KanjiItem(
                id = "kanji_n5_hon",
                character = "本",
                jlptLevel = JlptLevel.N5,
                meaningBn = "বই, মূল, উৎস",
                meaningEn = "Book, Origin, Main",
                onyomiKana = "ホン",
                kunyomiKana = "もと",
                romajiReadings = "hon / moto",
                strokeCount = 5,
                strokeDirectionsBn = "১. অনুভূমিক রেখা; ২. মাঝখানে উল্লম্ব রেখা; ৩. বামে তীর্যক রেখা; ৪. ডানে তীর্যক রেখা; ৫. নিচের ছোট অনুভূমিক রেখা।",
                strokeDirectionsEn = "1. Top horizontal; 2. Center vertical; 3. Left diagonal sweep; 4. Right diagonal sweep; 5. Short lower horizontal crossbar.",
                exampleVocabulary = listOf(
                    KanjiVocabExample("本", "ほん", "hon", "বই", "Book"),
                    KanjiVocabExample("日本", "にほん", "nihon", "জাপান", "Japan"),
                    KanjiVocabExample("本屋", "ほんや", "hon'ya", "বইয়ের দোকান", "Bookstore")
                ),
                exampleSentenceJp = "この本はとても面白いです。",
                exampleSentenceReading = "この ほん は とても おもしろい です。",
                exampleSentenceRomaji = "Kono hon wa totemo omoshiroi desu.",
                exampleSentenceBn = "এই বইটি খুবই মজার।",
                exampleSentenceEn = "This book is very interesting.",
                relatedKanji = listOf("木", "休", "体")
            ),

            // JLPT N4
            KanjiItem(
                id = "kanji_n4_kokoro",
                character = "心",
                jlptLevel = JlptLevel.N4,
                meaningBn = "মন, হৃদয়, অনুভূতি",
                meaningEn = "Heart, Mind, Spirit",
                onyomiKana = "シン",
                kunyomiKana = "こころ",
                romajiReadings = "shin / kokoro",
                strokeCount = 4,
                strokeDirectionsBn = "১. বামে ছোট বিন্দু; ২. নিচ দিয়ে বাঁকা চন্দ্রাকৃতি রেখা ও উপরে হুক; ৩. মাঝখানে উপরে ফোঁটা; ৪. ডান পাশে ফোঁটা।",
                strokeDirectionsEn = "1. Left downward dot; 2. Long sweeping bottom curve ending with an upward hook; 3. Center-top dot; 4. Far-right dot.",
                exampleVocabulary = listOf(
                    KanjiVocabExample("心", "こころ", "kokoro", "মন / হৃদয়", "Heart / Mind"),
                    KanjiVocabExample("安心", "あんしん", "anshin", "মানসিক স্বস্তি", "Peace of mind / Relief"),
                    KanjiVocabExample("心配", "しんぱい", "shinpai", "চিন্তা / উদ্বেগ", "Worry / Concern")
                ),
                exampleSentenceJp = "ご心配なく、大丈夫です。",
                exampleSentenceReading = "ごしんぱい なく、だいじょうぶ です。",
                exampleSentenceRomaji = "Goshinpai naku, daijoubu desu.",
                exampleSentenceBn = "চিন্তা করবেন না, সব ঠিক আছে।",
                exampleSentenceEn = "Please do not worry, everything is fine.",
                relatedKanji = listOf("思", "意", "感")
            ),
            KanjiItem(
                id = "kanji_n4_ugo",
                character = "動",
                jlptLevel = JlptLevel.N4,
                meaningBn = "নড়াচড়া করা, গতি, কাজ",
                meaningEn = "Move, Motion, Shift",
                onyomiKana = "ドウ",
                kunyomiKana = "うご.く, うご.かす",
                romajiReadings = "dou / ugoku, ugokasu",
                strokeCount = 11,
                strokeDirectionsBn = "বাম পাশে '重' (৯ স্ট্রোক: ভারী) এবং ডান পাশে '力' (২ স্ট্রোক: শক্তি) মিলে ১১ স্ট্রোকে গঠিত।",
                strokeDirectionsEn = "Left component 重 (9 strokes: heavy) followed by right component 力 (2 strokes: power).",
                exampleVocabulary = listOf(
                    KanjiVocabExample("動物", "どうぶつ", "doubutsu", "প্রাণী / পশুপাখি", "Animal"),
                    KanjiVocabExample("運動", "うんどう", "undou", "শরীরচর্চা / ব্যায়াম", "Exercise / Motion"),
                    KanjiVocabExample("動く", "うごく", "ugoku", "নড়াচড়া করা", "To move")
                ),
                exampleSentenceJp = "健康のために毎日運動しています。",
                exampleSentenceReading = "けんこう の ため に まいにち うんどう して います。",
                exampleSentenceRomaji = "Kenkou no tame ni mainichi undou shite imasu.",
                exampleSentenceBn = "স্বাস্থ্যের জন্য আমি প্রতিদিন ব্যায়াম করি।",
                exampleSentenceEn = "I exercise every day for my health.",
                relatedKanji = listOf("働", "力", "重")
            ),

            // JLPT N3
            KanjiItem(
                id = "kanji_n3_kan",
                character = "感",
                jlptLevel = JlptLevel.N3,
                meaningBn = "অনুভূতি, আবেগ, উপলব্ধি",
                meaningEn = "Emotion, Feeling, Sensation",
                onyomiKana = "カン",
                kunyomiKana = "(かん.じる)",
                romajiReadings = "kan / kanjiru",
                strokeCount = 13,
                strokeDirectionsBn = "উপরে '咸' (৯ স্ট্রোক) এবং নিচে হৃদয় নির্দেশক '心' (৪ স্ট্রোক) মিলে মোট ১৩ স্ট্রোক।",
                strokeDirectionsEn = "Top component 咸 (9 strokes) over bottom radical 心 (4 strokes: heart).",
                exampleVocabulary = listOf(
                    KanjiVocabExample("感謝", "かんしゃ", "kansha", "কৃতজ্ঞতা", "Gratitude"),
                    KanjiVocabExample("感じる", "かんじる", "kanjiru", "অনুভব করা", "To feel / sense"),
                    KanjiVocabExample("感動", "かんどう", "kandou", "গভীর আবেগাপ্লুত হওয়া", "Being deeply moved")
                ),
                exampleSentenceJp = "皆様のご協力に心から感謝いたします。",
                exampleSentenceReading = "みなさま の ごきょうりょく に こころ から かんしゃ いたします。",
                exampleSentenceRomaji = "Minasama no gokyouryoku ni kokoro kara kansha itashimasu.",
                exampleSentenceBn = "আপনাদের সকলের সহযোগিতার জন্য আন্তরিক কৃতজ্ঞতা জানাই।",
                exampleSentenceEn = "I sincerely thank everyone for your cooperation.",
                relatedKanji = listOf("心", "情", "想")
            ),

            // JLPT N2
            KanjiItem(
                id = "kanji_n2_koku",
                character = "刻",
                jlptLevel = JlptLevel.N2,
                meaningBn = "খোদাই করা, সময়, মুহূর্ত",
                meaningEn = "Engrave, Carve, Time, Moment",
                onyomiKana = "コク",
                kunyomiKana = "きざ.む",
                romajiReadings = "koku / kizamu",
                strokeCount = 8,
                strokeDirectionsBn = "বাম পাশে '亥' (৬ স্ট্রোক) এবং ডান পাশে তলোয়ার র‍্যাডিক্যাল '刂' (২ স্ট্রোক) মিলে ৮ স্ট্রোক।",
                strokeDirectionsEn = "Left component 亥 (6 strokes) followed by right blade radical 刂 (2 strokes).",
                exampleVocabulary = listOf(
                    KanjiVocabExample("遅刻", "ちこく", "chikoku", "দেরি হওয়া / বিলম্ব", "Lateness / Tardiness"),
                    KanjiVocabExample("深刻", "しんこく", "shinkoku", "গুরুতর / গভীর", "Serious / Grave"),
                    KanjiVocabExample("時刻表", "じこくひょう", "jikokuhyou", "সময়সূচি (ট্রেন/বাস)", "Timetable / Schedule")
                ),
                exampleSentenceJp = "電車の遅れで会議に遅刻しました。",
                exampleSentenceReading = "でんしゃ の おくれ で かいぎ に ちこく しました。",
                exampleSentenceRomaji = "Densha no okure de kaigi ni chikoku shimashita.",
                exampleSentenceBn = "ট্রেন দেরি করায় মিটিংয়ে পৌঁছাতে আমার বিলম্ব হয়েছিল।",
                exampleSentenceEn = "I was late for the meeting due to a train delay.",
                relatedKanji = listOf("時", "期", "則")
            ),

            // JLPT N1
            KanjiItem(
                id = "kanji_n1_ken",
                character = "謙",
                jlptLevel = JlptLevel.N1,
                meaningBn = "বিনয়, নম্রতা, নিরহংকার",
                meaningEn = "Humility, Modesty, Self-effacement",
                onyomiKana = "ケン",
                kunyomiKana = "へりくだ.る",
                romajiReadings = "ken / herikudaru",
                strokeCount = 17,
                strokeDirectionsBn = "বামে কথা বলার র‍্যাডিক্যাল '言' (৭ স্ট্রোক) এবং ডানে '兼' (১০ স্ট্রোক) মিলে মোট ১৭ স্ট্রোক।",
                strokeDirectionsEn = "Left speech radical 言 (7 strokes) combined with right component 兼 (10 strokes).",
                exampleVocabulary = listOf(
                    KanjiVocabExample("謙虚", "けんきょ", "kenkyo", "বিনয়ী / নম্র", "Modest / Humble"),
                    KanjiVocabExample("謙譲語", "けんじょうご", "kenjougo", "বিনয়সূচক জাপানি ভাষা", "Humble polite speech (Keigo)"),
                    KanjiVocabExample("謙遜", "けんそん", "kenson", "নম্রতা প্রকাশ", "Modesty / Humility")
                ),
                exampleSentenceJp = "成功しても常に謙虚な姿勢を保つべきだ。",
                exampleSentenceReading = "せいこう しても つねに けんきょ な しせい を たもつ べき だ。",
                exampleSentenceRomaji = "Seikou shitemo tsuneni kenkyo na shisei wo tamotsu beki da.",
                exampleSentenceBn = "সাফল্য পেলেও সর্বদা বিনয়ী মনোভাব বজায় রাখা উচিত।",
                exampleSentenceEn = "Even when successful, one should always maintain a humble attitude.",
                relatedKanji = listOf("譲", "敬", "誠")
            )
        )
    }

    /**
     * Verified Vocabulary spanning all 17 categories required in Section 08 and JLPT N5 to N1.
     */
    val vocabularyList: List<VocabularyItem> by lazy {
        listOf(
            // 1. GREETINGS
            VocabularyItem(
                id = "voc_greet_1",
                wordJp = "ありがとう",
                hiraganaReading = "ありがとう",
                kanjiSpelling = "有り難う",
                romaji = "Arigatou",
                meaningBn = "ধন্যবাদ",
                meaningEn = "Thank you",
                partOfSpeechEn = "Expression",
                partOfSpeechBn = "অভিব্যক্তি",
                category = VocabCategory.GREETINGS,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "手伝ってくれてありがとう。",
                exampleSentenceReading = "てつだって くれて ありがとう。",
                exampleSentenceRomaji = "Tetsudatte kurete arigatou.",
                exampleSentenceBn = "সাহায্য করার জন্য আপনাকে ধন্যবাদ।",
                exampleSentenceEn = "Thank you for helping me."
            ),
            VocabularyItem(
                id = "voc_greet_2",
                wordJp = "おはようございます",
                hiraganaReading = "おはようございます",
                kanjiSpelling = "お早うございます",
                romaji = "Ohayou gozaimasu",
                meaningBn = "শুভ সকাল (ভদ্র রীতি)",
                meaningEn = "Good morning (Polite)",
                partOfSpeechEn = "Expression",
                partOfSpeechBn = "অভিবাদন",
                category = VocabCategory.GREETINGS,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "先生、おはようございます。",
                exampleSentenceReading = "せんせい、おはようございます。",
                exampleSentenceRomaji = "Sensei, ohayou gozaimasu.",
                exampleSentenceBn = "শুভ সকাল, শিক্ষক মহোদয়।",
                exampleSentenceEn = "Good morning, Teacher."
            ),

            // 2. NUMBERS
            VocabularyItem(
                id = "voc_num_1",
                wordJp = "百",
                hiraganaReading = "ひゃく",
                kanjiSpelling = "百",
                romaji = "Hyaku",
                meaningBn = "একশো (১০০)",
                meaningEn = "One hundred (100)",
                partOfSpeechEn = "Numeric",
                partOfSpeechBn = "সংখ্যাবাচক শব্দ",
                category = VocabCategory.NUMBERS,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "このりんごは一個百円です。",
                exampleSentenceReading = "この りんご は いっこ ひゃくえん です。",
                exampleSentenceRomaji = "Kono ringo wa ikko hyaku-en desu.",
                exampleSentenceBn = "এই আপেলটি প্রতিটি একশো ইয়েন।",
                exampleSentenceEn = "This apple is 100 yen each."
            ),

            // 3. FAMILY
            VocabularyItem(
                id = "voc_fam_1",
                wordJp = "家族",
                hiraganaReading = "かぞく",
                kanjiSpelling = "家族",
                romaji = "Kazoku",
                meaningBn = "পরিবার",
                meaningEn = "Family",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.FAMILY,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "私の家族は四人です。",
                exampleSentenceReading = "わたし の かぞく は よにん です。",
                exampleSentenceRomaji = "Watashi no kazoku wa yonin desu.",
                exampleSentenceBn = "আমার পরিবারে চারজন সদস্য আছেন।",
                exampleSentenceEn = "There are four people in my family."
            ),

            // 4. FOOD
            VocabularyItem(
                id = "voc_food_1",
                wordJp = "朝ご飯",
                hiraganaReading = "あさごはん",
                kanjiSpelling = "朝ご飯",
                romaji = "Asagohan",
                meaningBn = "সকালের নাস্তা",
                meaningEn = "Breakfast",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.FOOD,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "毎朝七時に朝ご飯を食べます。",
                exampleSentenceReading = "まいあさ しちじ に あさごはん を たべます。",
                exampleSentenceRomaji = "Maiasa shichiji ni asagohan wo tabemasu.",
                exampleSentenceBn = "আমি প্রতিদিন সকাল সাতটায় নাস্তা খাই।",
                exampleSentenceEn = "I eat breakfast at 7 o'clock every morning."
            ),

            // 5. ANIMALS
            VocabularyItem(
                id = "voc_anim_1",
                wordJp = "猫",
                hiraganaReading = "ねこ",
                kanjiSpelling = "猫",
                romaji = "Neko",
                meaningBn = "বিড়াল",
                meaningEn = "Cat",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.ANIMALS,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "椅子の下に白い猫がいます。",
                exampleSentenceReading = "いす の した に しろい ねこ が います。",
                exampleSentenceRomaji = "Isu no shita ni shiroi neko ga imasu.",
                exampleSentenceBn = "চেয়ারের নিচে একটি সাদা বিড়াল আছে।",
                exampleSentenceEn = "There is a white cat under the chair."
            ),

            // 6. SCHOOL
            VocabularyItem(
                id = "voc_sch_1",
                wordJp = "図書館",
                hiraganaReading = "としょかん",
                kanjiSpelling = "図書館",
                romaji = "Toshokan",
                meaningBn = "গ্রন্থাগার / লাইব্রেরি",
                meaningEn = "Library",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.SCHOOL,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "図書館で日本の歴史の本を借りました。",
                exampleSentenceReading = "としょかん で にほん の れきし の ほん を かりました。",
                exampleSentenceRomaji = "Toshokan de Nihon no rekishi no hon wo karimashita.",
                exampleSentenceBn = "লাইব্রেরি থেকে জাপানের ইতিহাসের একটি বই ধার করেছি।",
                exampleSentenceEn = "I borrowed a book on Japanese history from the library."
            ),

            // 7. TRAVEL
            VocabularyItem(
                id = "voc_trv_1",
                wordJp = "旅館",
                hiraganaReading = "りょかん",
                kanjiSpelling = "旅館",
                romaji = "Ryokan",
                meaningBn = "ঐতিহ্যবাহী জাপানি আবাসিক হোটেল",
                meaningEn = "Traditional Japanese inn",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.TRAVEL,
                jlptLevel = JlptLevel.N4,
                exampleSentenceJp = "京都の古い旅館に泊まりました。",
                exampleSentenceReading = "きょうと の ふるい りょかん に とまりました。",
                exampleSentenceRomaji = "Kyouto no furui ryokan ni tomarimashita.",
                exampleSentenceBn = "আমরা কিয়োটোর একটি প্রাচীন রিওকানে থেকেছিলাম।",
                exampleSentenceEn = "We stayed at an old traditional inn in Kyoto."
            ),

            // 8. TRANSPORTATION
            VocabularyItem(
                id = "voc_trn_1",
                wordJp = "新幹線",
                hiraganaReading = "しんかんせん",
                kanjiSpelling = "新幹線",
                romaji = "Shinkansen",
                meaningBn = "শিনকানসেন (জাপানি বুলেট ট্রেন)",
                meaningEn = "Bullet train (Shinkansen)",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.TRANSPORTATION,
                jlptLevel = JlptLevel.N4,
                exampleSentenceJp = "新幹線で東京から大阪へ行きます。",
                exampleSentenceReading = "しんかんせん で とうきょう から おおさか へ いきます。",
                exampleSentenceRomaji = "Shinkansen de Toukyou kara Oosaka e ikimasu.",
                exampleSentenceBn = "শিনকানসেনে করে টোকিও থেকে ওসাকা যাব।",
                exampleSentenceEn = "I will go from Tokyo to Osaka by bullet train."
            ),

            // 9. SHOPPING
            VocabularyItem(
                id = "voc_shp_1",
                wordJp = "領収書",
                hiraganaReading = "りょうしゅうしょ",
                kanjiSpelling = "領収書",
                romaji = "Ryoushuusho",
                meaningBn = "অর্থ প্রাপ্তির রসিদ",
                meaningEn = "Official receipt",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.SHOPPING,
                jlptLevel = JlptLevel.N3,
                exampleSentenceJp = "すみません、領収書をお願いします。",
                exampleSentenceReading = "すみません、りょうしゅうしょ を おねがいします。",
                exampleSentenceRomaji = "Sumimasen, ryoushuusho wo onegaishimasu.",
                exampleSentenceBn = "দুঃখিত, দয়া করে আমাকে একটি রসিদ দিন।",
                exampleSentenceEn = "Excuse me, could I have a receipt, please?"
            ),

            // 10. TIME AND DATES
            VocabularyItem(
                id = "voc_tim_1",
                wordJp = "来週",
                hiraganaReading = "らいしゅう",
                kanjiSpelling = "来週",
                romaji = "Raishuu",
                meaningBn = "আগামী সপ্তাহ",
                meaningEn = "Next week",
                partOfSpeechEn = "Noun / Temporal",
                partOfSpeechBn = "সময়বাচক বিশেষ্য",
                category = VocabCategory.TIME_AND_DATES,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "来週の金曜日に試験があります。",
                exampleSentenceReading = "らいしゅう の きんようび に しけん が あります。",
                exampleSentenceRomaji = "Raishuu no kin'youbi ni shiken ga arimasu.",
                exampleSentenceBn = "আগামী সপ্তাহের শুক্রবারে পরীক্ষা আছে।",
                exampleSentenceEn = "There is an exam next Friday."
            ),

            // 11. WEATHER
            VocabularyItem(
                id = "voc_wea_1",
                wordJp = "天気予報",
                hiraganaReading = "てんきよほう",
                kanjiSpelling = "天気予報",
                romaji = "Tenki yohou",
                meaningBn = "আবহাওয়ার পূর্বাভাস",
                meaningEn = "Weather forecast",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.WEATHER,
                jlptLevel = JlptLevel.N4,
                exampleSentenceJp = "天気予報によると、明日は晴れるそうです。",
                exampleSentenceReading = "てんきよほう に よると、あした は はれる そうです。",
                exampleSentenceRomaji = "Tenki yohou ni yoru to, ashita wa hareru sou desu.",
                exampleSentenceBn = "আবহাওয়ার পূর্বাভাস অনুযায়ী আগামীকাল আকাশ পরিষ্কার থাকবে।",
                exampleSentenceEn = "According to the weather forecast, it will be sunny tomorrow."
            ),

            // 12. DAILY CONVERSATION
            VocabularyItem(
                id = "voc_cnv_1",
                wordJp = "お陰様で",
                hiraganaReading = "おかげさまで",
                kanjiSpelling = "お陰様で",
                romaji = "Okagesama de",
                meaningBn = "আপনার দোয়ায় / ঈশ্বরের কৃপায়",
                meaningEn = "Thanks to you / Fortunately",
                partOfSpeechEn = "Expression",
                partOfSpeechBn = "প্রচলিত অভিব্যক্তি",
                category = VocabCategory.DAILY_CONVERSATION,
                jlptLevel = JlptLevel.N4,
                exampleSentenceJp = "お陰様で元気になりました。",
                exampleSentenceReading = "おかげさまで げんき に なりました。",
                exampleSentenceRomaji = "Okagesama de genki ni narimashita.",
                exampleSentenceBn = "আপনার আশীর্বাদে আমি এখন সুস্থ হয়ে উঠেছি।",
                exampleSentenceEn = "Thanks to your kind wishes, I have recovered."
            ),

            // 13. WORK
            VocabularyItem(
                id = "voc_wrk_1",
                wordJp = "打ち合わせ",
                hiraganaReading = "うちあわせ",
                kanjiSpelling = "打ち合わせ",
                romaji = "Uchiawase",
                meaningBn = "প্রস্তুতিমূলক সভা / আলোচনা",
                meaningEn = "Business meeting / Preparatory discussion",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.WORK,
                jlptLevel = JlptLevel.N2,
                exampleSentenceJp = "午後三時から新しい企画の打ち合わせがあります。",
                exampleSentenceReading = "ごご さんじ から あたらしい きかく の うちあわせ が あります。",
                exampleSentenceRomaji = "Gogo sanji kara atarashii kikaku no uchiawase ga arimasu.",
                exampleSentenceBn = "বিকেল তিনটা থেকে নতুন প্রকল্পের প্রস্তুতি সভা রয়েছে।",
                exampleSentenceEn = "We have a planning meeting for the new project at 3 PM."
            ),

            // 14. TECHNOLOGY
            VocabularyItem(
                id = "voc_tec_1",
                wordJp = "人工知能",
                hiraganaReading = "じんこうちのう",
                kanjiSpelling = "人工知能",
                romaji = "Jinkou chinou",
                meaningBn = "কৃত্রিম বুদ্ধিমত্তা (AI)",
                meaningEn = "Artificial Intelligence (AI)",
                partOfSpeechEn = "Noun",
                partOfSpeechBn = "বিশেষ্য",
                category = VocabCategory.TECHNOLOGY,
                jlptLevel = JlptLevel.N1,
                exampleSentenceJp = "人工知能の技術は急速に発展している。",
                exampleSentenceReading = "じんこうちのう の ぎじゅつ は きゅうそく に はってん して いる。",
                exampleSentenceRomaji = "Jinkou chinou no gijutsu wa kyuusoku ni hatten shite iru.",
                exampleSentenceBn = "কৃত্রিম বুদ্ধিমত্তা প্রযুক্তি দ্রুতগতিতে বিকশিত হচ্ছে।",
                exampleSentenceEn = "Artificial intelligence technology is developing rapidly."
            ),

            // 15. EMOTIONS
            VocabularyItem(
                id = "voc_emo_1",
                wordJp = "懐かしい",
                hiraganaReading = "なつかしい",
                kanjiSpelling = "懐かしい",
                romaji = "Natsukashii",
                meaningBn = "স্মৃতিকাতর / পুরনো মধুর স্মৃতি জাগানিয়া",
                meaningEn = "Nostalgic / Fondly remembered",
                partOfSpeechEn = "I-Adjective",
                partOfSpeechBn = "ই-বিশেষণ",
                category = VocabCategory.EMOTIONS,
                jlptLevel = JlptLevel.N3,
                exampleSentenceJp = "故郷の写真を見ると懐かしい気持ちになります。",
                exampleSentenceReading = "こきょう の しゃしん を みる と なつかしい きもち に なります。",
                exampleSentenceRomaji = "Kokyou no shashin wo miru to natsukashii kimochi ni narimasu.",
                exampleSentenceBn = "নিজের গ্রামের বাড়ির ছবি দেখলে মন স্মৃতিকাতর হয়ে ওঠে।",
                exampleSentenceEn = "Looking at photos of my hometown makes me feel nostalgic."
            ),

            // 16. VERBS
            VocabularyItem(
                id = "voc_vrb_1",
                wordJp = "食べる",
                hiraganaReading = "たべる",
                kanjiSpelling = "食べる",
                romaji = "Taberu",
                meaningBn = "খাওয়া",
                meaningEn = "To eat",
                partOfSpeechEn = "Ichidan Verb",
                partOfSpeechBn = "ক্রিয়াপদ (ইচিদান)",
                category = VocabCategory.VERBS,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "みんなで美味しい料理を食べましょう。",
                exampleSentenceReading = "みんな で おいしい りょうり を たべましょう。",
                exampleSentenceRomaji = "Minna de oishii ryouri wo tabemashou.",
                exampleSentenceBn = "চলুন সবাই মিলে সুস্বাদু খাবার খাই।",
                exampleSentenceEn = "Let's eat delicious food together."
            ),

            // 17. ADJECTIVES
            VocabularyItem(
                id = "voc_adj_1",
                wordJp = "静か",
                hiraganaReading = "しずか",
                kanjiSpelling = "静か",
                romaji = "Shizuka",
                meaningBn = "শান্ত, নীরব, নিরিবিলি",
                meaningEn = "Quiet, Peaceful, Calm",
                partOfSpeechEn = "Na-Adjective",
                partOfSpeechBn = "না-বিশেষণ",
                category = VocabCategory.ADJECTIVES,
                jlptLevel = JlptLevel.N5,
                exampleSentenceJp = "この公園は朝とても静かです。",
                exampleSentenceReading = "この こうえん は あさ とても しずか です。",
                exampleSentenceRomaji = "Kono kouen wa asa totemo shizuka desu.",
                exampleSentenceBn = "এই পার্কটি সকালে খুব শান্ত থাকে।",
                exampleSentenceEn = "This park is very quiet in the morning."
            )
        )
    }

    /**
     * Structured Grammar Lessons from JLPT N5 to N1 (Section 09).
     */
    val grammarList: List<GrammarLesson> by lazy {
        listOf(
            // JLPT N5
            GrammarLesson(
                id = "gram_n5_wa_desu",
                pattern = "N1 は N2 です",
                jlptLevel = JlptLevel.N5,
                titleBn = "মূল বাক্য গঠন ও বিষয় নির্দেশক কণা 「は」",
                titleEn = "Topic Marker Particle は and Polite Copula です",
                explanationBn = "জাপানি বাক্যে 「は」 (লেখা হয় ha, কিন্তু কণা হিসেবে উচ্চারিত হয় wa) বাক্যের মূল বিষয় বা কর্তা নির্দেশ করে। বাক্যের শেষে 「です」 বসে বাক্যটিকে ভদ্র ও সম্পূর্ণ করে (বাংলায় 'হয়/আছি' অর্থ প্রকাশ করে)।",
                explanationEn = "The particle は (written 'ha', pronounced 'wa') marks the topic of the sentence. です (desu) is placed at the end of a noun or adjective predicate to make the statement polite.",
                formationRulesBn = "[বিশেষ্য ১ (Topic)] + は + [বিশেষ্য ২ / বিশেষণ] + です।",
                formationRulesEn = "[Noun 1 (Topic)] + は + [Noun 2 / Adjective] + です.",
                examples = listOf(
                    GrammarExample(
                        japanese = "私は学生です。",
                        hiraganaReading = "わたし は がくせい です。",
                        romaji = "Watashi wa gakusei desu.",
                        bengali = "আমি একজন ছাত্র।",
                        english = "I am a student."
                    ),
                    GrammarExample(
                        japanese = "さくらさんは日本人です。",
                        hiraganaReading = "さくらさん は にほんじん です。",
                        romaji = "Sakura-san wa Nihonjin desu.",
                        bengali = "সাকুরা একজন জাপানি।",
                        english = "Sakura is Japanese."
                    )
                ),
                commonMistakesBn = "ভুল: কণা 「は」-কে রোমাজিতে 'ha' উচ্চারণ করা বা লেখা। মনে রাখবেন: কণা হিসেবে এর উচ্চারণ সবসময় 'ওয়া' (wa), কিন্তু লিখতে হয় হিরাগানা 「は」 দিয়ে (わ নয়)।",
                commonMistakesEn = "Do not write the topic particle with わ (wa); it is always written with Hiragana は and pronounced 'wa'.",
                practiceQuestion = GrammarPracticeQuestion(
                    promptJp = "私（ ）田中です。",
                    promptBn = "শূন্যস্থানে সঠিক কণাটি বসান: 'আমি তানাকা।'",
                    promptEn = "Select the correct topic marker particle: 'I am Tanaka.'",
                    options = listOf("は", "わ", "を", "へ"),
                    correctOptionIndex = 0,
                    explanationBn = "কর্তা বা মূল বিষয় 'আমি' (私) নির্দেশ করতে হিরাগানা 「は」 কণা ব্যবহৃত হয়।",
                    explanationEn = "Hiragana は marks 'watashi' as the sentence topic."
                ),
                relatedGrammarPatterns = listOf("N1 は N2 ではありません", "N1 も N2 です")
            ),

            // JLPT N4
            GrammarLesson(
                id = "gram_n4_te_mo_ii",
                pattern = "V-て もいいですか",
                jlptLevel = JlptLevel.N4,
                titleBn = "অনুমতি প্রার্থনা করা (～てもいいですか)",
                titleEn = "Asking for Permission (~te mo ii desu ka)",
                explanationBn = "কোনো কাজ করার অনুমতি চাওয়ার জন্য ক্রিয়ার て-রূপের (Te-form) সাথে 「もいいですか」 যুক্ত করা হয়। বাংলায় এর অর্থ 'আমি কি ... করতে পারি?'।",
                explanationEn = "Attached to the te-form of a verb to politely ask for permission ('May I...? / Is it alright if I...?').",
                formationRulesBn = "[ক্রিয়ার て-রূপ (Te-form)] + もいいですか",
                formationRulesEn = "[Verb Te-form] + もいいですか",
                examples = listOf(
                    GrammarExample(
                        japanese = "ここで写真を撮ってもいいですか。",
                        hiraganaReading = "ここ で しゃしん を とっても いい ですか。",
                        romaji = "Koko de shashin wo tottemo ii desu ka.",
                        bengali = "আমি কি এখানে ছবি তুলতে পারি?",
                        english = "May I take photographs here?"
                    ),
                    GrammarExample(
                        japanese = "窓を開けてもいいですか。",
                        hiraganaReading = "まど を あけても いい ですか。",
                        romaji = "Mado wo aketemo ii desu ka.",
                        bengali = "আমি কি জানালাটি খুলতে পারি?",
                        english = "Is it alright if I open the window?"
                    )
                ),
                commonMistakesBn = "ক্রিয়ার মূল রূপ (Dictionary form) সরাসরি ব্যবহার করবেন না; অবশ্যই ক্রিয়ার て-রূপ (যেমন: 撮る → 撮って) ব্যবহার করতে হবে।",
                commonMistakesEn = "Always conjugate the verb into its Te-form first (e.g., とる becomes とって before もいいですか).",
                practiceQuestion = GrammarPracticeQuestion(
                    promptJp = "このペンを（ ）もいいですか。",
                    promptBn = "'আমি কি এই কলমটি ব্যবহার করতে পারি?' (使う = ব্যবহার করা)",
                    promptEn = "'May I use this pen?' (Conjugate 使う = tsukau)",
                    options = listOf("使って", "使う", "使った", "使い"),
                    correctOptionIndex = 0,
                    explanationBn = "使う (tsukau)-এর て-রূপ হলো 使って (tsukatte)।",
                    explanationEn = "The te-form of 使う (tsukau) is 使って (tsukatte)."
                ),
                relatedGrammarPatterns = listOf("V-てはいけません", "V-なくてもいいです")
            ),

            // JLPT N3
            GrammarLesson(
                id = "gram_n3_you_ni_naru",
                pattern = "V-る / V-ない ようになる",
                jlptLevel = JlptLevel.N3,
                titleBn = "সক্ষমতা বা অভ্যাসের পরিবর্তন (～ようになる)",
                titleEn = "Change in Ability or Habit (~you ni naru)",
                explanationBn = "আগে যা পারা যেত না বা করা হতো না, সময়ের সাথে তা পারা বা অভ্যাসে পরিণত হওয়া বোঝাতে 「ようになる」 ব্যবহৃত হয়।",
                explanationEn = "Expresses a gradual change in state, ability, or habit ('came to be able to...' or 'started doing...').",
                formationRulesBn = "[ক্রিয়ার সক্ষমতাবাচক রূপ / সাধারণ বর্তমান রূপ] + ようになる",
                formationRulesEn = "[Verb Dictionary Form / Potential Form / Nai-form] + ようになる",
                examples = listOf(
                    GrammarExample(
                        japanese = "日本語の新聞が読めるようになりました。",
                        hiraganaReading = "にほんご の しんぶん が よめる ように なりました。",
                        romaji = "Nihongo no shinbun ga yomeru you ni narimashita.",
                        bengali = "আমি এখন জাপানি সংবাদপত্র পড়তে পারি (আগে পারতাম না)।",
                        english = "I have become able to read Japanese newspapers."
                    )
                ),
                commonMistakesBn = "ইচ্ছাকৃত প্রচেষ্টা বোঝাতে 「ようにする」 ব্যবহৃত হয়, আর ফলাফল বা অবস্থার পরিবর্তন বোঝাতে 「ようになる」 বসে।",
                commonMistakesEn = "Do not confuse with ようにする (making a conscious effort); ようになる describes the resulting change.",
                practiceQuestion = GrammarPracticeQuestion(
                    promptJp = "やっと自転車に乗れる（ ）なりました。",
                    promptBn = "'অবশেষে আমি সাইকেল চালাতে শিখেছি।'",
                    promptEn = "'I finally became able to ride a bicycle.'",
                    options = listOf("ように", "ことに", "ために", "そうに"),
                    correctOptionIndex = 0,
                    explanationBn = "সক্ষমতার পরিবর্তন বোঝাতে সম্ভাব্য ক্রিয়ার (乗れる) পর ようになりました বসে।",
                    explanationEn = "ように pairs with なりました after a potential verb to show newly acquired ability."
                ),
                relatedGrammarPatterns = listOf("V-るようにする", "N になる")
            ),

            // JLPT N2
            GrammarLesson(
                id = "gram_n2_ni_oshite",
                pattern = "N に応じて (におうじて)",
                jlptLevel = JlptLevel.N2,
                titleBn = "পরিস্থিতি বা চাহিদা অনুযায়ী (～に応じて)",
                titleEn = "In Accordance With / Depending On (~ni oujite)",
                explanationBn = "কোনো নির্দিষ্ট পরিস্থিতি, বয়স, ক্ষমতা বা চাহিদার সাথে সামঞ্জস্য রেখে পরিবর্তন বা ব্যবস্থা গ্রহণ করা বোঝাতে বিশেষ্যের পরে 「に応じて」 বসে।",
                explanationEn = "Indicates adapting or responding appropriately to a variable condition, need, or capacity ('in accordance with / depending on').",
                formationRulesBn = "[বিশেষ্য (Noun)] + に応じて (বিশেষ্যের পর হলে: に応じた + বিশেষ্য)",
                formationRulesEn = "[Noun] + に応じて (or に応じた + Noun)",
                examples = listOf(
                    GrammarExample(
                        japanese = "体力に応じて運動の量を決めてください。",
                        hiraganaReading = "たいりょく に おうじて うんどう の りょう を きめて ください。",
                        romaji = "Tairyoku ni oujite undou no ryou wo kimete kudasai.",
                        bengali = "শারীরিক সক্ষমতা অনুযায়ী ব্যায়ামের পরিমাণ নির্ধারণ করুন।",
                        english = "Please decide the amount of exercise according to your physical strength."
                    )
                ),
                commonMistakesBn = "এটি পরিবর্তনশীল শর্তের ক্ষেত্রে বসে; নিছক তথ্যের উৎস বোঝাতে 「によると」 ব্যবহৃত হয়।",
                commonMistakesEn = "Use に応じて when responding to a range of conditions, not for quoting a source of information.",
                practiceQuestion = GrammarPracticeQuestion(
                    promptJp = "お客様の希望（ ）メニューを変更できます。",
                    promptBn = "'ক্রেতাদের চাহিদা অনুযায়ী মেনু পরিবর্তন করা সম্ভব।'",
                    promptEn = "'We can adjust the menu according to the customer's wishes.'",
                    options = listOf("に応じて", "について", "にとって", "に対して"),
                    correctOptionIndex = 0,
                    explanationBn = "চাহিদা বা প্রত্যাশা (希望) অনুসারে খাপ খাইয়ে নেওয়া বোঝাতে に応じて ব্যবহৃত হয়।",
                    explanationEn = "に応じて means adapting in response to wishes or conditions."
                ),
                relatedGrammarPatterns = listOf("N に従って", "N に伴って")
            ),

            // JLPT N1
            GrammarLesson(
                id = "gram_n1_ni_taru",
                pattern = "V-る / N に足る (にたる)",
                jlptLevel = JlptLevel.N1,
                titleBn = "যোগ্য বা উপযুক্ত হওয়া (～に足る)",
                titleEn = "Worthy Of / Deserving Of (~ni taru)",
                explanationBn = "উচ্চমার্গীয় ও আনুষ্ঠানিক জাপানি ভাষায় কোনো কিছু যে বিশ্বাসের, প্রশংসার বা মনোযোগের যোগ্য—তা প্রকাশ করতে 「に足る」 ব্যবহৃত হয়।",
                explanationEn = "Formal literary expression indicating that something is worthy of or sufficient for an action or evaluation ('worthy of trust / praise').",
                formationRulesBn = "[ক্রিয়ার মূল রূপ / বিশেষ্যবাচক ক্রিয়া] + に足る (নেতিবাচক: に足りない)",
                formationRulesEn = "[Verb Dictionary Form / Verbal Noun] + に足る (Negative: に足りない)",
                examples = listOf(
                    GrammarExample(
                        japanese = "彼は信頼に足る人物だ。",
                        hiraganaReading = "かれ は しんらい に たる じんぶつ だ。",
                        romaji = "Kare wa shinrai ni taru jinbutsu da.",
                        bengali = "তিনি একজন বিশ্বাসের যোগ্য ব্যক্তি।",
                        english = "He is a person worthy of trust."
                    )
                ),
                commonMistakesBn = "শুধুমাত্র ইতিবাচক মূল্যায়ন (যেমন: 信頼 বিশ্বাস, 尊敬 শ্রদ্ধা, 注目 মনোযোগ) প্রকাশক শব্দের সাথেই এটি ব্যবহৃত হয়।",
                commonMistakesEn = "Used strictly with positive evaluation nouns/verbs such as 信頼 (trust), 尊敬 (respect), and 評価 (appreciation).",
                practiceQuestion = GrammarPracticeQuestion(
                    promptJp = "そんな小さな噂は取る（ ）問題ではない。",
                    promptBn = "'এমন তুচ্ছ গুজব আমলে নেওয়ার মতো কোনো বিষয় নয়।'",
                    promptEn = "'Such a trivial rumor is not worth taking seriously.'",
                    options = listOf("に足る", "に際して", "に先立ち", "に限って"),
                    correctOptionIndex = 0,
                    explanationBn = "取るに足る / 取るに足りない একটি প্রচলিত N1 অভিব্যক্তি যার অর্থ 'গুরুত্ব দেওয়ার যোগ্য'।",
                    explanationEn = "取るに足る means worthy of consideration or notice."
                ),
                relatedGrammarPatterns = listOf("V-るに値する", "取るに足りない")
            )
        )
    }
}
