package com.nihongo.app.domain.model

/**
 * Independently verified JLPT N5 to N1 course content including Reading Comprehension
 * passages, exam preparation guides, and app-created practice Mock Examinations.
 */
object JlptCourseCatalog {

    fun getCourseBundle(level: JlptLevel): JlptLevelCourseBundle {
        return when (level) {
            JlptLevel.N5 -> buildN5Course()
            JlptLevel.N4 -> buildN4Course()
            JlptLevel.N3 -> buildN3Course()
            JlptLevel.N2 -> buildN2Course()
            JlptLevel.N1 -> buildN1Course()
        }
    }

    private fun buildN5Course(): JlptLevelCourseBundle = JlptLevelCourseBundle(
        level = JlptLevel.N5,
        examDurationGuideBn = "প্রস্তুতি লক্ষ্য: প্রায় ১০০টি কাঞ্জি, ৮০০টি শব্দ এবং মৌলিক বাক্য গঠন",
        examDurationGuideEn = "Study Target: ~100 Kanji, ~800 Words, and foundational sentence patterns",
        preparationTipsBn = listOf(
            "হিরাগানা ও কাতাকানা দ্রুত পড়ার অভ্যাস গড়ে তুলুন যাতে পরীক্ষায় রোমাজির ওপর নির্ভর করতে না হয়।",
            "মৌলিক কণা (は, が, を, に, で, へ, と, から, まで) এর সঠিক ব্যবহার বারবার অনুশীলন করুন।",
            "দৈনন্দিন সময়, তারিখ, সংখ্যা ও গণনাবাচক শব্দ (ひとつ, ふたつ, ～人, ～本) মুখস্থ রাখুন।"
        ),
        preparationTipsEn = listOf(
            "Master fluent reading of all Hiragana and Katakana without relying on Romaji.",
            "Focus on core grammatical particles (wa, ga, wo, ni, de, e, to, kara, made).",
            "Practice telling time, dates, prices, and common Japanese counters daily."
        ),
        readingPassages = listOf(
            JlptReadingPassage(
                id = "read_n5_1",
                jlptLevel = JlptLevel.N5,
                titleJp = "私の日曜日",
                titleBn = "আমার রবিবার",
                titleEn = "My Sunday",
                passageJp = "昨日は日曜日でした。私は朝八時に起きて、朝ご飯を食べました。午後から友達と一緒に図書館へ行って、日本語の本を読みました。",
                passageHiragana = "きのう は にちようび でした。わたし は あさ はちじ に おきて、あさごはん を たべました。ごご から ともだち と いっしょ に としょかん へ いって、にほんご の ほん を よみました。",
                passageRomaji = "Kinou wa nichiyoubi deshita. Watashi wa asa hachiji ni okite, asagohan wo tabemashita. Gogo kara tomodachi to issho ni toshokan e itte, nihongo no hon wo yomimashita.",
                translationBn = "গতকাল রবিবার ছিল। আমি সকাল আটটায় ঘুম থেকে উঠে নাস্তা খেয়েছি। বিকেল থেকে বন্ধুর সাথে লাইব্রেরিতে গিয়ে জাপানি ভাষার বই পড়েছি।",
                translationEn = "Yesterday was Sunday. I woke up at 8 AM and ate breakfast. In the afternoon, I went to the library with my friend and read a Japanese book.",
                questionJp = "私は日曜日の午後にどこへ行きましたか。",
                questionBn = "রবিবার বিকেলে আমি কোথায় গিয়েছিলাম?",
                questionEn = "Where did the speaker go on Sunday afternoon?",
                options = listOf(
                    "図書館 (としょかん) • গ্রন্থাগার / Library",
                    "病院 (びょういん) • হাসপাতাল / Hospital",
                    "銀行 (ぎんこう) • ব্যাংক / Bank",
                    "郵便局 (ゆうびんきょく) • ডাকঘর / Post office"
                ),
                correctOptionIndex = 0,
                explanationBn = "অনুচ্ছেদে স্পষ্ট বলা হয়েছে: 「午後から友達と一緒に図書館へ行って」 (বিকেল থেকে বন্ধুর সাথে লাইব্রেরিতে গিয়ে)।",
                explanationEn = "The passage states: 'Gogo kara tomodachi to issho ni toshokan e itte' (went to the library with a friend in the afternoon)."
            ),
            JlptReadingPassage(
                id = "read_n5_2",
                jlptLevel = JlptLevel.N5,
                titleJp = "新しいかばん",
                titleBn = "নতুন ব্যাগ",
                titleEn = "A New Bag",
                passageJp = "駅の近くの店で黒い革のかばんを買いました。あまり高くなかったですが、とても軽くて使いやすいです。毎日学校へ持って行きます。",
                passageHiragana = "えき の ちかく の みせ で くろい かわ の かばん を かいました。あまり たかく なかった ですが、とても かるくて つかいやすい です。まいにち がっこう へ もって いきます。",
                passageRomaji = "Eki no chikaku no mise de kuroi kawa no kaban wo kaimashita. Amari takakunakatta desu ga, totemo karukute tsukaiyasui desu. Mainichi gakkou e motte ikimasu.",
                translationBn = "স্টেশনের কাছের একটি দোকান থেকে কালো রঙের চামড়ার একটি ব্যাগ কিনেছি। দাম খুব বেশি ছিল না, তবে এটি বেশ হালকা ও ব্যবহার করা সহজ। প্রতিদিন স্কুলে নিয়ে যাই।",
                translationEn = "I bought a black leather bag at a shop near the station. It was not very expensive, and it is very light and easy to use. I take it to school every day.",
                questionJp = "このかばんはどうですか。",
                questionBn = "এই ব্যাগটি কেমন?",
                questionEn = "What is true about this bag?",
                options = listOf(
                    "軽くて使いやすいです • হালকা ও ব্যবহারে সহজ / Light and easy to use",
                    "とても高くて重いです • খুব দামি ও ভারী / Very expensive and heavy",
                    "白くて小さいです • সাদা ও ছোট / White and small",
                    "まだ使っていません • এখনো ব্যবহার করা হয়নি / Not used yet"
                ),
                correctOptionIndex = 0,
                explanationBn = "অনুচ্ছেদে বলা হয়েছে: 「とても軽くて使いやすいです」 (এটি খুব হালকা এবং ব্যবহার করা সহজ)।",
                explanationEn = "The text explicitly describes the bag as 'totemo karukute tsukaiyasui desu' (very light and easy to use)."
            )
        ),
        mockExamQuestions = listOf(
            JlptMockExamQuestion(
                id = "mock_n5_1",
                jlptLevel = JlptLevel.N5,
                section = MockExamSection.VOCAB_KANJI,
                promptJp = "「毎日」の読み方は何ですか。",
                promptInstructionBn = "「毎日」 কাঞ্জি শব্দটির সঠিক হিরাগানা উচ্চারণ নির্বাচন করুন:",
                promptInstructionEn = "Select the correct Hiragana reading for 毎日:",
                options = listOf("まいにち (mainichi)", "まいげつ (maigetsu)", "まいしゅう (maishuu)", "まいとし (maitoshi)"),
                correctOptionIndex = 0,
                explanationBn = "「毎日」 উচ্চারিত হয় 'まいにち' (mainichi), যার অর্থ প্রতিদিন।",
                explanationEn = "毎日 is read 'mainichi' and means every day."
            ),
            JlptMockExamQuestion(
                id = "mock_n5_2",
                jlptLevel = JlptLevel.N5,
                section = MockExamSection.VOCAB_KANJI,
                promptJp = "「みず」の漢字はどれですか。",
                promptInstructionBn = "'মিযু' (পানি) বোঝাতে কোন কাঞ্জিটি ব্যবহৃত হয়?",
                promptInstructionEn = "Which Kanji represents 'mizu' (water)?",
                options = listOf("水", "火", "木", "金"),
                correctOptionIndex = 0,
                explanationBn = "「水」 কাঞ্জির কুন'ইওমি উচ্চারণ 'みず' (mizu - পানি)।",
                explanationEn = "水 is the Kanji for water (mizu)."
            ),
            JlptMockExamQuestion(
                id = "mock_n5_3",
                jlptLevel = JlptLevel.N5,
                section = MockExamSection.GRAMMAR_READING,
                promptJp = "椅子の下（ ）白い猫がいます。",
                promptInstructionBn = "শূন্যস্থানে অবস্থান নির্দেশক সঠিক কণাটি বসান:",
                promptInstructionEn = "Choose the correct particle indicating location of existence:",
                options = listOf("に", "を", "へ", "ج"),
                correctOptionIndex = 0,
                explanationBn = "প্রাণীর অবস্থান (います) নির্দেশ করতে স্থানের পরে 「に」 কণা বসে।",
                explanationEn = "The particle に marks the location where a living thing exists (います)."
            ),
            JlptMockExamQuestion(
                id = "mock_n5_4",
                jlptLevel = JlptLevel.N5,
                section = MockExamSection.LISTENING,
                promptJp = "🔊 শ্রবণ প্রশ্ন: অডিওটি শুনে সঠিক অর্থটি নির্বাচন করুন",
                promptInstructionBn = "স্পিকার বাটনে ট্যাপ করে বাক্যটি শুনুন এবং সঠিক অর্থ বেছে নিন:",
                promptInstructionEn = "Tap the speaker to listen and select the accurate statement:",
                audioToSpeak = "来週の金曜日に試験があります。",
                options = listOf(
                    "আগামী সপ্তাহের শুক্রবারে পরীক্ষা আছে • Exam is next Friday",
                    "গতকালের পরীক্ষা খুব সহজ ছিল • Yesterday's exam was easy",
                    "আজ বিকেলে ক্লাস নেই • No class this afternoon",
                    "আগামীকাল লাইব্রেরি বন্ধ থাকবে • Library is closed tomorrow"
                ),
                correctOptionIndex = 0,
                explanationBn = "উচ্চারিত বাক্য: 「来週の金曜日に試験があります」 (আগামী সপ্তাহের শুক্রবারে পরীক্ষা আছে)।",
                explanationEn = "Spoken sentence: Raishuu no kin'youbi ni shiken ga arimasu (There is an exam next Friday)."
            )
        )
    )

    private fun buildN4Course(): JlptLevelCourseBundle = JlptLevelCourseBundle(
        level = JlptLevel.N4,
        examDurationGuideBn = "প্রস্তুতি লক্ষ্য: প্রায় ৩০০টি কাঞ্জি, ১৫০০টি শব্দ এবং ক্রিয়ার সকল মৌলিক রূপান্তর",
        examDurationGuideEn = "Study Target: ~300 Kanji, ~1,500 Words, and core verb conjugations",
        preparationTipsBn = listOf(
            "ক্রিয়ার সম্ভাব্য রূপ (Potential), ইচ্ছাসূচক রূপ (Volitional), শর্তবাচক রূপ (～ば, ～たら) এবং প্যাসিভ রূপ ভালোভাবে আয়ত্ত করুন।",
            "দেওয়া-নেওয়া বা উপকার প্রকাশক ক্রিয়া (あげる, くれる, もらう) এর দিকনির্দেশনা খেয়াল রাখুন।",
            "ছোট ইমেইল, নোটিশ বোর্ড ও দৈনন্দিন চিঠির অনুচ্ছেদ পড়ার অভ্যাস করুন।"
        ),
        preparationTipsEn = listOf(
            "Master verb conjugations: Potential, Volitional, Conditional (~ba, ~tara), and Passive forms.",
            "Practice giving and receiving verbs (ageru, kureru, morau) and polite requests.",
            "Read short notices, emails, and everyday narrative passages regularly."
        ),
        readingPassages = listOf(
            JlptReadingPassage(
                id = "read_n4_1",
                jlptLevel = JlptLevel.N4,
                titleJp = "京都への旅行",
                titleBn = "কিয়োটো ভ্রমণ",
                titleEn = "A Trip to Kyoto",
                passageJp = "先月、家族と一緒に新幹線で京都へ行きました。駅からバスに乗って、静かな山の中の古い旅館に泊まりました。旅館の料理はとても美味しくて、家族みんなが喜びました。",
                passageHiragana = "せんげつ、かぞく と いっしょ に しんかんせん で きょうと へ いきました。えき から バス に のって、しずかな やま の なか の ふるい りょかん に とまりました。りょかん の りょうり は とても おいしくて、かぞく みんな が よろこびました。",
                passageRomaji = "Sengetsu, kazoku to issho ni shinkansen de Kyouto e ikimashita. Eki kara basu ni notte, shizuka na yama no naka no furui ryokan ni tomarimashita. Ryokan no ryouri wa totemo oishikute, kazoku minna ga yorokobimashita.",
                translationBn = "গত মাসে পরিবারের সাথে শিনকানসেনে করে কিয়োটো গিয়েছিলাম। স্টেশন থেকে বাসে চড়ে শান্ত পাহাড়ের ভেতর একটি পুরনো রিওকানে (জাপানি হোটেল) থেকেছিলাম। রিওকানের খাবার অত্যন্ত সুস্বাদু ছিল এবং পরিবারের সবাই খুব খুশি হয়েছিল।",
                translationEn = "Last month, I went to Kyoto by bullet train with my family. From the station, we took a bus and stayed at an old traditional inn in the quiet mountains. The inn's food was delicious, and everyone in the family was delighted.",
                questionJp = "駅から旅館まで何で行きましたか。",
                questionBn = "স্টেশন থেকে রিওকান পর্যন্ত কিসে করে গিয়েছিল?",
                questionEn = "How did the family travel from the station to the ryokan?",
                options = listOf(
                    "バス • বাসে করে / By bus",
                    "新幹線 • শিনকানসেনে / By bullet train",
                    "自転車 • সাইকেলে / By bicycle",
                    "歩いて • হেঁটে / On foot"
                ),
                correctOptionIndex = 0,
                explanationBn = "অনুচ্ছেদে বলা হয়েছে: 「駅からバスに乗って、...古い旅館に泊まりました」 (স্টেশন থেকে বাসে চড়ে রিওকানে গিয়েছিলাম)।",
                explanationEn = "While they took the Shinkansen to Kyoto, from the station to the inn they took a bus ('Eki kara basu ni notte')."
            )
        ),
        mockExamQuestions = listOf(
            JlptMockExamQuestion(
                id = "mock_n4_1",
                jlptLevel = JlptLevel.N4,
                section = MockExamSection.VOCAB_KANJI,
                promptJp = "「安心」の読み方はどれですか。",
                promptInstructionBn = "「安心」 (মানসিক স্বস্তি) কাঞ্জিটির সঠিক উচ্চারণ নির্বাচন করুন:",
                promptInstructionEn = "Select the correct reading for 安心 (peace of mind):",
                options = listOf("あんしん (anshin)", "あんじん (anjin)", "やすこころ (yasukokoro)", "しんぱい (shinpai)"),
                correctOptionIndex = 0,
                explanationBn = "「安心」 উচ্চারিত হয় 'あんしん' (anshin), যার অর্থ স্বস্তি বা নিশ্চিন্ত হওয়া।",
                explanationEn = "安心 is read 'anshin' and means relief or peace of mind."
            ),
            JlptMockExamQuestion(
                id = "mock_n4_2",
                jlptLevel = JlptLevel.N4,
                section = MockExamSection.GRAMMAR_READING,
                promptJp = "ここで写真を撮っ（ ）いいですか。",
                promptInstructionBn = "অনুমতি চাওয়ার বাক্যটি সম্পূর্ণ করুন: 'আমি কি এখানে ছবি তুলতে পারি?'",
                promptInstructionEn = "Complete the permission request: 'May I take photos here?'",
                options = listOf("ても", "たら", "てから", "ながら"),
                correctOptionIndex = 0,
                explanationBn = "অনুমতি প্রার্থনা করতে ক্রিয়ার て-রূপের সাথে 「てもいいですか」 ব্যবহৃত হয়।",
                explanationEn = "Verb te-form + もいいですか is the standard pattern for asking permission."
            ),
            JlptMockExamQuestion(
                id = "mock_n4_3",
                jlptLevel = JlptLevel.N4,
                section = MockExamSection.LISTENING,
                promptJp = "🔊 শ্রবণ প্রশ্ন: অডিওটি শুনে সঠিক অর্থটি নির্বাচন করুন",
                promptInstructionBn = "অডিওটি মনোযোগ দিয়ে শুনুন এবং বক্তা কেন প্রতিদিন ব্যায়াম করেন তা বলুন:",
                promptInstructionEn = "Listen to the audio and select why the speaker exercises every day:",
                audioToSpeak = "健康のために毎日運動しています。",
                options = listOf(
                    "স্বাস্থ্যের জন্য • For health (健康のため)",
                    "খেলার প্রতিযোগিতার জন্য • For a sports tournament",
                    "ওজন বাড়ানোর জন্য • To gain weight",
                    "ডাক্তারের চাকরির জন্য • For a medical job"
                ),
                correctOptionIndex = 0,
                explanationBn = "বাক্যটি ছিল: 「健康のために毎日運動しています」 (স্বাস্থ্যের জন্য প্রতিদিন ব্যায়াম করছি)।",
                explanationEn = "Kenkou no tame ni (for the sake of health) mainichi undou shite imasu."
            )
        )
    )

    private fun buildN3Course(): JlptLevelCourseBundle = JlptLevelCourseBundle(
        level = JlptLevel.N3,
        examDurationGuideBn = "প্রস্তুতি লক্ষ্য: প্রায় ৬৫০টি কাঞ্জি, ৩৭৫০টি শব্দ এবং মধ্যম স্তরের বাস্তব পরিস্থিতি অনুধাবন",
        examDurationGuideEn = "Study Target: ~650 Kanji, ~3,750 Words, and intermediate bridge fluency",
        preparationTipsBn = listOf(
            "কথ্য জাপানি ও আনুষ্ঠানিক লিখিত জাপানির মধ্যকার রূপান্তরগুলো (যেমন: ～ようになる, ～ことにする, ～おかげで) তুলনা করে পড়ুন।",
            "দৈনন্দিন কর্মক্ষেত্রের ঘোষণা, ভ্রমণের নিয়মাবলি এবং মতামতধর্মী ছোট প্রবন্ধ পড়ার অভ্যাস গড়ে তুলুন।",
            "শ্রবণ অনুশীলনে স্বাভাবিক গতির (1.0x) কথোপকথন শুনে বক্তার মূল উদ্দেশ্য ধরার চেষ্টা করুন।"
        ),
        preparationTipsEn = listOf(
            "Compare similar intermediate grammar structures (~you ni naru vs ~koto ni naru, ~okage de vs ~sei de).",
            "Practice reading workplace announcements, travel notices, and short opinion essays.",
            "Train listening comprehension at natural 1.0x speed to catch speaker intent."
        ),
        readingPassages = listOf(
            JlptReadingPassage(
                id = "read_n3_1",
                jlptLevel = JlptLevel.N3,
                titleJp = "日本語の新聞を読む習慣",
                titleBn = "জাপানি সংবাদপত্র পড়ার অভ্যাস",
                titleEn = "The Habit of Reading Japanese Newspapers",
                passageJp = "日本へ来たばかりの頃は漢字が難しくて、新聞を全く読めませんでした。しかし、毎日少しずつ新しい言葉を辞書で調べるようにした結果、今では短い記事なら辞書なしで読めるようになりました。",
                passageHiragana = "にほん へ きた ばかり の ころ は かんじ が むずかしくて、しんぶん を まったく よめませんでした。しかし、まいにち すこしずつ あたらしい ことば を じしょ で しらべる ように した けっか、いま では みじかい きじ なら じしょ なし で よめる ように なりました。",
                passageRomaji = "Nihon e kita bakari no koro wa kanji ga muzukashikute, shinbun wo mattaku yomemasen deshita. Shikashi, mainichi sukoshizutsu atarashii kotoba wo jisho de shiraberu you ni shita kekka, ima de wa mijikai kiji nara jisho nashi de yomeru you ni narimashita.",
                translationBn = "জাপানে আসার শুরুর দিকে কাঞ্জি কঠিন মনে হওয়ায় সংবাদপত্র মোটেই পড়তে পারতাম না। কিন্তু প্রতিদিন অল্প অল্প করে নতুন শব্দ অভিধানে খোঁজার চেষ্টা করার ফলে এখন ছোট নিবন্ধগুলো অভিধান ছাড়াই পড়তে পারি।",
                translationEn = "When I first arrived in Japan, Kanji was difficult and I could not read newspapers at all. However, as a result of making an effort to look up new words in a dictionary little by little every day, I have now become able to read short articles without a dictionary.",
                questionJp = "筆者は今、どのぐらい新聞が読めますか。",
                questionBn = "লেখক এখন কতটুকু সংবাদপত্র পড়তে পারেন?",
                questionEn = "To what extent can the writer read newspapers now?",
                options = listOf(
                    "短い記事なら辞書なしで読める • ছোট নিবন্ধ অভিধান ছাড়াই পড়তে পারেন",
                    "どんな記事でも簡単に読める • যেকোনো জটিল নিবন্ধ সহজেই পড়তে পারেন",
                    "まだ新聞は全く読めない • এখনো সংবাদপত্র একদমই পড়তে পারেন না",
                    "ひらがなの本しか読めない • কেবল হিরাগানা বই পড়তে পারেন"
                ),
                correctOptionIndex = 0,
                explanationBn = "অনুচ্ছেদের শেষ অংশে বলা হয়েছে: 「今では短い記事なら辞書なしで読めるようになりました」 (এখন ছোট নিবন্ধ হলে অভিধান ছাড়াই পড়তে পারি)।",
                explanationEn = "The passage concludes: 'ima de wa mijikai kiji nara jisho nashi de yomeru you ni narimashita' (can now read short articles without a dictionary)."
            )
        ),
        mockExamQuestions = listOf(
            JlptMockExamQuestion(
                id = "mock_n3_1",
                jlptLevel = JlptLevel.N3,
                section = MockExamSection.VOCAB_KANJI,
                promptJp = "皆様のご協力に心から（感謝）いたします。",
                promptInstructionBn = "বন্ধনীর ভেতরের 「感謝」 (কৃতজ্ঞতা) কাঞ্জিটির সঠিক উচ্চারণ কোনটি?",
                promptInstructionEn = "Select the correct reading for 感謝 (gratitude):",
                options = listOf("かんしゃ (kansha)", "かんどう (kandou)", "かんしん (kanshin)", "かんかく (kankaku)"),
                correctOptionIndex = 0,
                explanationBn = "「感謝」 উচ্চারিত হয় 'かんしゃ' (kansha), যার অর্থ কৃতজ্ঞতা।",
                explanationEn = "感謝 is read 'kansha' and means gratitude or appreciation."
            ),
            JlptMockExamQuestion(
                id = "mock_n3_2",
                jlptLevel = JlptLevel.N3,
                section = MockExamSection.GRAMMAR_READING,
                promptJp = "毎日練習したので、日本語が話せる（ ）なりました。",
                promptInstructionBn = "সক্ষমতার পরিবর্তন বোঝাতে শূন্যস্থানে সঠিক ব্যাকরণ রূপটি বসান:",
                promptInstructionEn = "Choose the grammar pattern expressing a change in ability:",
                options = listOf("ように", "ことに", "ために", "ばかりに"),
                correctOptionIndex = 0,
                explanationBn = "আগে না পারা কোনো কাজ পারা বোঝাতে ক্রিয়ার সম্ভাব্য রূপের পর 「ようになりました」 বসে।",
                explanationEn = "Potential verb + ようになる indicates acquiring a new ability."
            ),
            JlptMockExamQuestion(
                id = "mock_n3_3",
                jlptLevel = JlptLevel.N3,
                section = MockExamSection.LISTENING,
                promptJp = "🔊 শ্রবণ প্রশ্ন: দোকানে ক্রেতা কী চাইছেন?",
                promptInstructionBn = "অডিওটি শুনুন এবং ক্রেতা দোকানির কাছে কী অনুরোধ করছেন তা নির্বাচন করুন:",
                promptInstructionEn = "Listen to the audio and identify what the customer is requesting:",
                audioToSpeak = "すみません、領収書をお願いします。",
                options = listOf(
                    "অর্থ প্রাপ্তির রসিদ (領収書) • An official receipt",
                    "খাবারের মেনু (メニュー) • The restaurant menu",
                    "ফেরত টিকিট (往復切符) • A round-trip ticket",
                    "নতুন ব্যাগ (新しいかばん) • A new bag"
                ),
                correctOptionIndex = 0,
                explanationBn = "「領収書をお願いします」 অর্থ 'দয়া করে আমাকে একটি রসিদ (Receipt) দিন'।",
                explanationEn = "Ryoushuusho wo onegaishimasu means 'Could I have an official receipt, please?'"
            )
        )
    )

    private fun buildN2Course(): JlptLevelCourseBundle = JlptLevelCourseBundle(
        level = JlptLevel.N2,
        examDurationGuideBn = "প্রস্তুতি লক্ষ্য: প্রায় ১০০০টি কাঞ্জি, ৬০০০টি শব্দ এবং পেশাদার ও সংবাদপত্রের জাপানি ভাষা",
        examDurationGuideEn = "Study Target: ~1,000 Kanji, ~6,000 Words, and business/editorial Japanese",
        preparationTipsBn = listOf(
            "যুক্ত কাঞ্জি শব্দ (Jukugo) এবং এদের অন'ইওমি উচ্চারণের পার্থক্য সূক্ষ্মভাবে অনুশীলন করুন।",
            "আনুষ্ঠানিক সংযোগকারী অব্যয় ও লিখিত ব্যাকরণ (যেমন: ～に応じて, ～に際して, ～にもかかわらず) আয়ত্ত করুন।",
            "ব্যবসায়িক ইমেইল, সম্পাদকীয় কলাম ও তুলনামূলক প্রবন্ধ দ্রুত পড়ার কৌশল অনুশীলন করুন।"
        ),
        preparationTipsEn = listOf(
            "Focus on compound Kanji (Jukugo) readings and subtle vocabulary synonyms.",
            "Master formal written grammar connectors (~ni oujite, ~ni saishite, ~ni mo kakawarazu).",
            "Practice skimming and scanning business emails and newspaper editorials."
        ),
        readingPassages = listOf(
            JlptReadingPassage(
                id = "read_n2_1",
                jlptLevel = JlptLevel.N2,
                titleJp = "柔軟な働き方と会議の効率化",
                titleBn = "নমনীয় কর্মপদ্ধতি ও সভার কার্যকারিতা",
                titleEn = "Flexible Work Styles and Meeting Efficiency",
                passageJp = "近年、業務の内容や社員の生活環境に応じて、働く時間や場所を柔軟に選べる企業が増えている。また、事前の打ち合わせ資料を共有することで、会議の時間を短縮し、生産性を高める取り組みも進んでいる。",
                passageHiragana = "きんねん、ぎょうむ の ないよう や しゃいん の せいかつ かんきょう に おうじて、はたらく じかん や ばしょ を じゅうなん に えらべる きぎょう が ふえて いる。また、じぜん の うちあわせ しりょう を きょうゆう する こと で、かいぎ の じかん を たんしゅく し、せいさんせい を たかめる とりくみ も すすんで いる。",
                passageRomaji = "Kinnen, gyoumu no naiyou ya shain no seikatsu kankyou ni oujite, hataraku jikan ya basho wo juunan ni eraberu kigyou ga fuete iru. Mata, jizen no uchiawase shiryou wo kyouyuu suru koto de, kaigi no jikan wo tanshuku shi, seisansei wo takameru torikumi mo susunde iru.",
                translationBn = "সাম্প্রতিক বছরগুলোতে কাজের ধরন এবং কর্মীদের জীবনযাত্রার পরিবেশ অনুযায়ী কাজের সময় ও স্থান নমনীয়ভাবে বেছে নেওয়ার সুযোগ দেওয়া প্রতিষ্ঠানের সংখ্যা বাড়ছে। এছাড়া সভার আগেই আলোচ্য নথিপত্র আদান-প্রদানের মাধ্যমে সভার সময় কমিয়ে উৎপাদনশীলতা বৃদ্ধির উদ্যোগও এগিয়ে চলেছে।",
                translationEn = "In recent years, more companies are allowing employees to flexibly choose their working hours and locations according to job duties and living circumstances. Additionally, initiatives to shorten meeting times and boost productivity by sharing preparatory materials in advance are progressing.",
                questionJp = "会議の時間を短縮するために、どのような取り組みが行われていますか。",
                questionBn = "সভার সময় কমানোর জন্য কী ধরনের উদ্যোগ নেওয়া হচ্ছে?",
                questionEn = "What initiative is being taken to shorten meeting times?",
                options = listOf(
                    "事前に打ち合わせ資料を共有すること • সভার আগেই আলোচ্য নথিপত্র শেয়ার করা",
                    "すべての会議を完全に廃止すること • সকল মিটিং পুরোপুরি বাতিল করা",
                    "休日だけ会議を開くこと • শুধুমাত্র ছুটির দিনে মিটিং করা",
                    "資料を一切作らないこと • কোনো নথিপত্র তৈরি না করা"
                ),
                correctOptionIndex = 0,
                explanationBn = "অনুচ্ছেদে উল্লেখ আছে: 「事前の打ち合わせ資料を共有することで、会議の時間を短縮し」 (আগেই আলোচ্য নথিপত্র শেয়ার করার মাধ্যমে সভার সময় কমানো হচ্ছে)।",
                explanationEn = "The text states that sharing preparatory materials in advance ('jizen no uchiawase shiryou wo kyouyuu suru koto de') shortens meeting times."
            )
        ),
        mockExamQuestions = listOf(
            JlptMockExamQuestion(
                id = "mock_n2_1",
                jlptLevel = JlptLevel.N2,
                section = MockExamSection.VOCAB_KANJI,
                promptJp = "電車の遅れで会議に（遅刻）しました。",
                promptInstructionBn = "বন্ধনীর 「遅刻」 (দেরি/বিলম্ব) শব্দের সঠিক হিরাগানা পাঠ কোনটি?",
                promptInstructionEn = "Select the correct Hiragana reading for 遅刻 (lateness):",
                options = listOf("ちこく (chikoku)", "ちかく (chikaku)", "ていこく (teikoku)", "じこく (jikoku)"),
                correctOptionIndex = 0,
                explanationBn = "「遅刻」 উচ্চারিত হয় 'ちこく' (chikoku), যার অর্থ নির্ধারিত সময়ের চেয়ে দেরি হওয়া।",
                explanationEn = "遅刻 is read 'chikoku' and means lateness or tardiness."
            ),
            JlptMockExamQuestion(
                id = "mock_n2_2",
                jlptLevel = JlptLevel.N2,
                section = MockExamSection.GRAMMAR_READING,
                promptJp = "体力（ ）運動の量を決めてください。",
                promptInstructionBn = "'শারীরিক সক্ষমতা অনুযায়ী' বোঝাতে সঠিক ব্যাকরণ রূপটি নির্বাচন করুন:",
                promptInstructionEn = "Choose the expression meaning 'in accordance with physical strength':",
                options = listOf("に応じて (ni oujite)", "について (ni tsuite)", "にかかわらず (ni kakawarazu)", "に反して (ni hanshite)"),
                correctOptionIndex = 0,
                explanationBn = "পরিস্থিতি বা সক্ষমতার সাথে সামঞ্জস্য রেখে কিছু করা বোঝাতে 「に応じて」 ব্যবহৃত হয়।",
                explanationEn = "に応じて means 'in accordance with' or 'adapted to'."
            ),
            JlptMockExamQuestion(
                id = "mock_n2_3",
                jlptLevel = JlptLevel.N2,
                section = MockExamSection.LISTENING,
                promptJp = "🔊 শ্রবণ প্রশ্ন: বিকেল তিনটায় কী নির্ধারিত রয়েছে?",
                promptInstructionBn = "অডিও বাক্যটি শুনে বিকেল তিনটার কর্মসূচি নির্বাচন করুন:",
                promptInstructionEn = "Listen to the audio and select what is scheduled for 3 PM:",
                audioToSpeak = "午後三時から新しい企画の打ち合わせがあります。",
                options = listOf(
                    "নতুন প্রকল্পের প্রস্তুতি সভা (打ち合わせ) • Preparatory meeting for a new project",
                    "গ্রাহকের সাথে নৈশভোজ • Dinner with a client",
                    "বার্ষিক স্বাস্থ্য পরীক্ষা • Annual health checkup",
                    "টোকিও স্টেশন ভ্রমণ • Trip to Tokyo Station"
                ),
                correctOptionIndex = 0,
                explanationBn = "「午後三時から新しい企画の打ち合わせがあります」 অর্থ বিকেল তিনটা থেকে নতুন প্রকল্পের প্রস্তুতিমূলক সভা আছে।",
                explanationEn = "Atarashii kikaku no uchiawase means a planning meeting for the new project."
            )
        )
    )

    private fun buildN1Course(): JlptLevelCourseBundle = JlptLevelCourseBundle(
        level = JlptLevel.N1,
        examDurationGuideBn = "প্রস্তুতি লক্ষ্য: প্রায় ২০০০টি কাঞ্জি, ১০,০০০টি শব্দ এবং একাডেমিক ও সাহিত্যিক জাপানি দক্ষতা",
        examDurationGuideEn = "Study Target: ~2,000 Kanji, ~10,000 Words, and abstract academic/literary mastery",
        preparationTipsBn = listOf(
            "উচ্চস্তরের ধ্রুপদী ও আনুষ্ঠানিক ব্যাকরণ কাঠামো (যেমন: ～に足る, ～を余儀なくされる, ～に即して) এর সূক্ষ্ম ব্যবহার আয়ত্ত করুন।",
            "বিজ্ঞান, প্রযুক্তি, দর্শন ও অর্থনীতি বিষয়ক দীর্ঘ প্রবন্ধ পড়ে লেখকের অন্তর্নিহিত যুক্তি বিশ্লেষণ করার অভ্যাস করুন।",
            "বিনয়সূচক ও সম্মানসূচক জাপানি (謙譲語 ও 尊敬語) এর উন্নত প্রয়োগগুলো চর্চা করুন।"
        ),
        preparationTipsEn = listOf(
            "Master literary and high-register grammar structures (~ni taru, ~wo yoginaku sareru, ~ni sokushite).",
            "Analyze abstract essays on technology, philosophy, economics, and society.",
            "Refine advanced Keigo (Sonkeigo and Kenjougo) for executive and academic contexts."
        ),
        readingPassages = listOf(
            JlptReadingPassage(
                id = "read_n1_1",
                jlptLevel = JlptLevel.N1,
                titleJp = "人工知能の発展と人間の謙虚な姿勢",
                titleBn = "কৃত্রিম বুদ্ধিমত্তার বিকাশ ও মানুষের বিনয়ী দৃষ্টিভঙ্গি",
                titleEn = "The Advancement of AI and Human Humility",
                passageJp = "人工知能の技術は急速に発展しており、膨大なデータを瞬時に処理する能力においては人間を凌駕しつつある。しかし、どれほど技術が進歩しようとも、それを使う人間自身が常に謙虚な姿勢を保ち、倫理的な判断力を養うことが不可欠である。",
                passageHiragana = "じんこうちのう の ぎじゅつ は きゅうそく に はってん して おり、ぼうだいな データ を しゅんじ に しょり する のうりょく に おいて は にんげん を りょうが しつつ ある。しかし、どれほど ぎじゅつ が しんぽ しよう とも、それ を つかう にんげん じしん が つねに けんきょ な しせい を たもち、りんりてき な はんだんりょく を やしなう こと が ふかけつ で ある。",
                passageRomaji = "Jinkou chinou no gijutsu wa kyuusoku ni hatten shite ori, boudai na deeta wo shunji ni shori suru nouryoku ni oite wa ningen wo ryouga shitsutsu aru. Shikashi, dorehodo gijutsu ga shinpo shiyou tomo, sore wo tsukau ningen jishin ga tsuneni kenkyo na shisei wo tamochi, rinriteki na handanryoku wo yashinau koto ga fukaketsu de aru.",
                translationBn = "কৃত্রিম বুদ্ধিমত্তা প্রযুক্তি দ্রুতগতিতে বিকশিত হচ্ছে এবং বিপুল পরিমাণ উপাত্ত মুহূর্তের মধ্যে প্রক্রিয়াকরণের সক্ষমতায় তা মানুষকে ছাড়িয়ে যাচ্ছে। তবে প্রযুক্তি যতই উন্নত হোক না কেন, এর ব্যবহারকারী মানুষের নিজেদের সর্বদা বিনয়ী মনোভাব বজায় রাখা এবং নৈতিক বিচারবোধ গড়ে তোলা অপরিহার্য।",
                translationEn = "Artificial intelligence technology is developing rapidly and is surpassing humans in the ability to instantaneously process vast amounts of data. However, no matter how far technology advances, it remains indispensable for the humans who use it to always maintain a humble attitude and cultivate ethical judgment.",
                questionJp = "筆者が最も強調していることは何ですか。",
                questionBn = "লেখক কোন বিষয়টির ওপর সবচেয়ে বেশি গুরুত্ব আরোপ করেছেন?",
                questionEn = "What does the author emphasize as indispensable?",
                options = listOf(
                    "人間が謙虚な姿勢を保ち、倫理的な判断力を養うこと • মানুষের বিনয়ী মনোভাব ও নৈতিক বিচারবোধ গড়ে তোলা",
                    "人工知能の研究を直ちに中止すること • কৃত্রিম বুদ্ধিমত্তার গবেষণা অবিলম্বে বন্ধ করা",
                    "すべての判断を機械に任せること • সকল সিদ্ধান্ত যন্ত্রের ওপর ছেড়ে দেওয়া",
                    "データの処理をすべて手作業に戻すこと • সকল ডেটা পুনরায় হাতে-কলমে হিসাব করা"
                ),
                correctOptionIndex = 0,
                explanationBn = "অনুচ্ছেদের শেষ বাক্যে লেখক বলেছেন: 「人間自身が常に謙虚な姿勢を保ち、倫理的な判断力を養うことが不可欠である」।",
                explanationEn = "The author concludes that maintaining a humble attitude ('kenkyo na shisei') and cultivating ethical judgment ('rinriteki na handanryoku') is indispensable ('fukaketsu')."
            )
        ),
        mockExamQuestions = listOf(
            JlptMockExamQuestion(
                id = "mock_n1_1",
                jlptLevel = JlptLevel.N1,
                section = MockExamSection.VOCAB_KANJI,
                promptJp = "成功しても常に（謙虚）な姿勢を保つべきだ。",
                promptInstructionBn = "বন্ধনীর ভেতরের 「謙虚」 (বিনয়ী/নম্র) শব্দের সঠিক উচ্চারণ নির্বাচন করুন:",
                promptInstructionEn = "Select the correct Hiragana reading for 謙虚 (humble / modest):",
                options = listOf("けんきょ (kenkyo)", "けんそん (kenson)", "けんじょう (kenjou)", "かんきょ (kankyo)"),
                correctOptionIndex = 0,
                explanationBn = "「謙虚」 উচ্চারিত হয় 'けんきょ' (kenkyo), যার অর্থ বিনয়ী বা নিরহংকার।",
                explanationEn = "謙虚 is read 'kenkyo' and means modest or humble."
            ),
            JlptMockExamQuestion(
                id = "mock_n1_2",
                jlptLevel = JlptLevel.N1,
                section = MockExamSection.GRAMMAR_READING,
                promptJp = "彼は誠実で、十分に信頼（ ）人物だ。",
                promptInstructionBn = "'বিশ্বাসের যোগ্য' অর্থ প্রকাশ করতে সঠিক N1 ব্যাকরণ রূপটি বেছে নিন:",
                promptInstructionEn = "Select the formal N1 expression meaning 'worthy of trust':",
                options = listOf("に足る (ni taru)", "に際して (ni saishite)", "に先立ち (ni sakidachi)", "を皮切りに (wo kawakiri ni)"),
                correctOptionIndex = 0,
                explanationBn = "「信頼に足る」 অর্থ বিশ্বাসের যোগ্য বা আস্থা রাখার উপযুক্ত।",
                explanationEn = "信頼に足る (shinrai ni taru) is a classic N1 idiom meaning worthy of trust."
            ),
            JlptMockExamQuestion(
                id = "mock_n1_3",
                jlptLevel = JlptLevel.N1,
                section = MockExamSection.LISTENING,
                promptJp = "🔊 শ্রবণ প্রশ্ন: অডিও বাক্যটির মূল বক্তব্য কী?",
                promptInstructionBn = "অডিওটি শুনে বাক্যটির সঠিক অর্থ নির্বাচন করুন:",
                promptInstructionEn = "Listen to the audio and select the accurate meaning:",
                audioToSpeak = "人工知能の技術は急速に発展している。",
                options = listOf(
                    "কৃত্রিম বুদ্ধিমত্তা প্রযুক্তি দ্রুতগতিতে বিকশিত হচ্ছে • AI technology is developing rapidly",
                    "কৃত্রিম বুদ্ধিমত্তা ব্যবহারের খরচ কমে গেছে • The cost of using AI has decreased",
                    "নতুন আইন আগামী বছর থেকে কার্যকর হবে • The new law takes effect next year",
                    "গবেষণাগারের নির্মাণকাজ সম্পন্ন হয়েছে • Laboratory construction is complete"
                ),
                correctOptionIndex = 0,
                explanationBn = "「人工知能の技術は急速に発展している」 অর্থ কৃত্রিম বুদ্ধিমত্তা প্রযুক্তি দ্রুতগতিতে বিকশিত হচ্ছে।",
                explanationEn = "Jinkou chinou no gijutsu wa kyuusoku ni hatten shite iru (AI technology is developing rapidly)."
            )
        )
    )
}
