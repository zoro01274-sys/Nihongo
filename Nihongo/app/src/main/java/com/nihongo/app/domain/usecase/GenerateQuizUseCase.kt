package com.nihongo.app.domain.usecase

import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.KanaCatalog
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.domain.model.QuizMode
import com.nihongo.app.domain.model.QuizQuestion
import com.nihongo.app.domain.model.WordMatchPair

/**
 * Generates verified quiz questions and listening exercises across all 9 QuizModes
 * and 6 Listening Comprehension exercise types (Sections 12 & 14).
 */
class GenerateQuizUseCase {

    fun generateQuiz(
        mode: QuizMode,
        mistakeQuestionIds: Set<String> = emptySet()
    ): List<QuizQuestion> {
        return when (mode) {
            QuizMode.MULTIPLE_CHOICE -> buildMultipleChoiceQuestions()
            QuizMode.JP_TO_BENGALI -> buildJapaneseToBengaliQuestions()
            QuizMode.JP_TO_ENGLISH -> buildJapaneseToEnglishQuestions()
            QuizMode.MATCH_WORDS -> buildMatchWordsQuestions()
            QuizMode.KANA_RECOGNITION -> buildKanaRecognitionQuestions()
            QuizMode.KANJI_RECOGNITION -> buildKanjiRecognitionQuestions()
            QuizMode.FILL_IN_BLANKS -> buildFillInBlanksQuestions()
            QuizMode.LISTENING_QUIZ -> buildListeningComprehensionQuestions()
            QuizMode.SENTENCE_ORDERING -> buildSentenceOrderingQuestions()
            QuizMode.MISTAKE_REVIEW -> {
                val allQuestions = buildMultipleChoiceQuestions() +
                    buildJapaneseToBengaliQuestions() +
                    buildKanjiRecognitionQuestions() +
                    buildFillInBlanksQuestions() +
                    buildListeningComprehensionQuestions()
                val matched = allQuestions.filter { mistakeQuestionIds.contains(it.id) }
                matched.ifEmpty { buildMultipleChoiceQuestions().take(5) }
            }
        }
    }

    private fun buildJapaneseToBengaliQuestions(): List<QuizQuestion> {
        val vocab = LearningCatalog.vocabularyList
        return vocab.take(8).mapIndexed { index, target ->
            val distractors = vocab.filter { it.id != target.id }.shuffled().take(3)
            val allOptions = (distractors + target).shuffled()
            val correctIdx = allOptions.indexOfFirst { it.id == target.id }

            QuizQuestion(
                id = "q_jp_bn_${target.id}_$index",
                mode = QuizMode.JP_TO_BENGALI,
                jlptLevel = target.jlptLevel,
                promptJapanese = target.wordJp,
                promptSubtext = "${target.hiraganaReading} (${target.romaji})",
                questionInstructionBn = "নিচের জাপানি শব্দটির সঠিক বাংলা অর্থ নির্বাচন করুন:",
                questionInstructionEn = "Select the correct Bengali meaning for this Japanese word:",
                audioTextToSpeak = target.wordJp,
                options = allOptions.map { it.meaningBn },
                correctOptionIndex = correctIdx,
                explanationBn = "「${target.wordJp} (${target.hiraganaReading})」 শব্দের বাংলা অর্থ হলো '${target.meaningBn}'।",
                explanationEn = "${target.wordJp} (${target.romaji}) means '${target.meaningEn}' (${target.meaningBn})."
            )
        }
    }

    private fun buildJapaneseToEnglishQuestions(): List<QuizQuestion> {
        val vocab = LearningCatalog.vocabularyList
        return vocab.take(8).mapIndexed { index, target ->
            val distractors = vocab.filter { it.id != target.id }.shuffled().take(3)
            val allOptions = (distractors + target).shuffled()
            val correctIdx = allOptions.indexOfFirst { it.id == target.id }

            QuizQuestion(
                id = "q_jp_en_${target.id}_$index",
                mode = QuizMode.JP_TO_ENGLISH,
                jlptLevel = target.jlptLevel,
                promptJapanese = target.wordJp,
                promptSubtext = "${target.hiraganaReading} (${target.romaji})",
                questionInstructionBn = "নিচের জাপানি শব্দটির সঠিক ইংরেজি অর্থ নির্বাচন করুন:",
                questionInstructionEn = "Select the correct English meaning for this Japanese word:",
                audioTextToSpeak = target.wordJp,
                options = allOptions.map { it.meaningEn },
                correctOptionIndex = correctIdx,
                explanationBn = "「${target.wordJp}」-এর ইংরেজি অর্থ '${target.meaningEn}' (${target.meaningBn})।",
                explanationEn = "${target.wordJp} (${target.romaji}) translates to '${target.meaningEn}'."
            )
        }
    }

    private fun buildKanaRecognitionQuestions(): List<QuizQuestion> {
        val kanaPool = (KanaCatalog.hiraganaList.take(15) + KanaCatalog.katakanaList.take(15))
        return kanaPool.take(10).mapIndexed { idx, target ->
            val distractors = kanaPool.filter { it.id != target.id }.shuffled().take(3)
            val allOptions = (distractors + target).shuffled()
            val correctIdx = allOptions.indexOfFirst { it.id == target.id }

            QuizQuestion(
                id = "q_kana_${target.id}_$idx",
                mode = QuizMode.KANA_RECOGNITION,
                jlptLevel = JlptLevel.N5,
                promptJapanese = target.character,
                promptSubtext = target.script.titleEn,
                questionInstructionBn = "এই কানা বর্ণটির সঠিক রোমাজি ও বাংলা উচ্চারণ কোনটি?",
                questionInstructionEn = "Which is the correct Romaji and pronunciation for this Kana?",
                audioTextToSpeak = target.character,
                options = allOptions.map { "${it.romaji} • ${it.pronunciationBn.substringBefore(" (")}" },
                correctOptionIndex = correctIdx,
                explanationBn = "「${target.character}」 বর্ণের উচ্চারণ হলো '${target.romaji}' (${target.pronunciationBn})। উদাহরণ: ${target.exampleWordJp} (${target.exampleMeaningBn})।",
                explanationEn = "${target.character} is pronounced '${target.romaji}'. Example: ${target.exampleWordJp} (${target.exampleMeaningEn})."
            )
        }
    }

    private fun buildKanjiRecognitionQuestions(): List<QuizQuestion> {
        val kanjiPool = LearningCatalog.kanjiList
        return kanjiPool.mapIndexed { idx, target ->
            val distractors = kanjiPool.filter { it.id != target.id }.shuffled().take(3)
            val allOptions = (distractors + target).shuffled()
            val correctIdx = allOptions.indexOfFirst { it.id == target.id }

            QuizQuestion(
                id = "q_kanji_${target.id}_$idx",
                mode = QuizMode.KANJI_RECOGNITION,
                jlptLevel = target.jlptLevel,
                promptJapanese = target.character,
                promptSubtext = "Strokes: ${target.strokeCount}",
                questionInstructionBn = "এই কাঞ্জিটির সঠিক অর্থ ও উচ্চারণ (Reading) নির্বাচন করুন:",
                questionInstructionEn = "Select the correct meaning and reading for this Kanji:",
                audioTextToSpeak = target.character,
                options = allOptions.map {
                    "${it.meaningBn} (${it.meaningEn}) • ${it.onyomiKana} / ${it.kunyomiKana}"
                },
                correctOptionIndex = correctIdx,
                explanationBn = "কাঞ্জি 「${target.character}」-এর অর্থ '${target.meaningBn}' এবং এর উচ্চারণ ${target.romajiReadings}।",
                explanationEn = "Kanji ${target.character} means '${target.meaningEn}' with readings: ${target.romajiReadings}."
            )
        }
    }

    private fun buildFillInBlanksQuestions(): List<QuizQuestion> {
        val grammarQuestions = LearningCatalog.grammarList.map { lesson ->
            val pq = lesson.practiceQuestion
            QuizQuestion(
                id = "q_fill_${lesson.id}",
                mode = QuizMode.FILL_IN_BLANKS,
                jlptLevel = lesson.jlptLevel,
                promptJapanese = pq.promptJp,
                promptSubtext = lesson.pattern,
                questionInstructionBn = pq.promptBn,
                questionInstructionEn = pq.promptEn,
                audioTextToSpeak = lesson.examples.firstOrNull()?.japanese.orEmpty(),
                options = pq.options,
                correctOptionIndex = pq.correctOptionIndex,
                explanationBn = pq.explanationBn,
                explanationEn = pq.explanationEn
            )
        }

        val extraBlankQuestions = listOf(
            QuizQuestion(
                id = "q_fill_extra_1",
                mode = QuizMode.FILL_IN_BLANKS,
                jlptLevel = JlptLevel.N5,
                promptJapanese = "毎日冷たい水（ ）飲みます。",
                promptSubtext = "Object Marker Particle",
                questionInstructionBn = "শূন্যস্থানে সঠিক কর্মকারক কণাটি বসান: 'আমি প্রতিদিন ঠান্ডা পানি পান করি।'",
                questionInstructionEn = "Complete the missing particle: 'I drink cold water every day.'",
                audioTextToSpeak = "毎日冷たい水を飲みます。",
                options = listOf("を", "は", "で", "に"),
                correctOptionIndex = 0,
                explanationBn = "ক্রিয়ার সরাসরি কর্ম (Direct Object: 水 পানি) নির্দেশ করতে 「を」 কণা বসে।",
                explanationEn = "The particle を marks 水 (water) as the direct object of the verb 飲みます (to drink)."
            ),
            QuizQuestion(
                id = "q_fill_extra_2",
                mode = QuizMode.FILL_IN_BLANKS,
                jlptLevel = JlptLevel.N5,
                promptJapanese = "新幹線（ ）東京へ行きます。",
                promptSubtext = "Means of Transport Particle",
                questionInstructionBn = "শূন্যস্থানে যানবাহন নির্দেশক কণাটি বসান: 'শিনকানসেনে করে টোকিও যাব।'",
                questionInstructionEn = "Select the particle indicating means of transportation:",
                audioTextToSpeak = "新幹線で東京へ行きます。",
                options = listOf("で", "を", "に", "と"),
                correctOptionIndex = 0,
                explanationBn = "যানবাহন বা মাধ্যম বোঝাতে বিশেষ্যের পরে 「で」 কণা বসে।",
                explanationEn = "The particle で indicates the means of transportation (by Shinkansen)."
            )
        )
        return grammarQuestions + extraBlankQuestions
    }

    /**
     * Covers all 6 Listening Comprehension Exercise Types required in Section 12:
     * 1. Listen and choose the correct word.
     * 2. Listen and select the correct meaning.
     * 3. Identify the Kana character.
     * 4. Complete the missing word.
     * 5. Understand a Japanese sentence.
     * 6. Listen and repeat (reinforced via audio replay + slow playback).
     */
    private fun buildListeningComprehensionQuestions(): List<QuizQuestion> = listOf(
        // Type 1: Listen and choose the correct word
        QuizQuestion(
            id = "q_lis_type1",
            mode = QuizMode.LISTENING_QUIZ,
            jlptLevel = JlptLevel.N5,
            promptJapanese = "🔊 অডিওটি মনোযোগ দিয়ে শুনুন",
            promptSubtext = "Exercise Type 1: Listen & Choose the Word",
            questionInstructionBn = "১. অডিওটি শুনে সঠিক জাপানি শব্দটি নির্বাচন করুন:",
            questionInstructionEn = "1. Listen to the audio and choose the exact Japanese word spoken:",
            audioTextToSpeak = "図書館",
            hideJapanesePromptUntilAnswered = true,
            options = listOf("図書館 (としょかん)", "新幹線 (しんかんせん)", "朝ご飯 (あさごはん)", "家族 (かぞく)"),
            correctOptionIndex = 0,
            explanationBn = "উচ্চারিত শব্দটি ছিল 「図書館 (としょかん - Toshokan)」 যার অর্থ লাইব্রেরি বা গ্রন্থাগার।",
            explanationEn = "The spoken word was 図書館 (toshokan), meaning Library."
        ),
        // Type 2: Listen and select the correct meaning
        QuizQuestion(
            id = "q_lis_type2",
            mode = QuizMode.LISTENING_QUIZ,
            jlptLevel = JlptLevel.N5,
            promptJapanese = "🔊 অডিও শুনে অর্থ বলুন",
            promptSubtext = "Exercise Type 2: Listen & Select Meaning",
            questionInstructionBn = "২. জাপানি শব্দটি শুনে সঠিক বাংলা ও ইংরেজি অর্থ নির্বাচন করুন:",
            questionInstructionEn = "2. Listen to the Japanese word and select its correct meaning:",
            audioTextToSpeak = "おはようございます",
            hideJapanesePromptUntilAnswered = true,
            options = listOf(
                "শুভ সকাল • Good morning",
                "ধন্যবাদ • Thank you",
                "শুভ রাত্রি • Good night",
                "বিদায় • Goodbye"
            ),
            correctOptionIndex = 0,
            explanationBn = "「おはようございます (Ohayou gozaimasu)」 অর্থ শুভ সকাল।",
            explanationEn = "おはようございます (Ohayou gozaimasu) means Good morning (polite)."
        ),
        // Type 3: Identify the Kana character by sound
        QuizQuestion(
            id = "q_lis_type3",
            mode = QuizMode.LISTENING_QUIZ,
            jlptLevel = JlptLevel.N5,
            promptJapanese = "🔊 ধ্বনি শুনে কানা বর্ণটি খুঁজুন",
            promptSubtext = "Exercise Type 3: Identify the Kana Character",
            questionInstructionBn = "৩. অডিও ধ্বনিটি শুনে সঠিক হিরাগানা বর্ণটি চিহ্নিত করুন:",
            questionInstructionEn = "3. Listen to the syllable and identify the matching Hiragana character:",
            audioTextToSpeak = "し",
            hideJapanesePromptUntilAnswered = true,
            options = listOf("し (shi)", "ち (chi)", "つ (tsu)", "さ (sa)"),
            correctOptionIndex = 0,
            explanationBn = "উচ্চারিত ধ্বনিটি ছিল 「し (shi - শি)」।",
            explanationEn = "The spoken kana syllable was し (shi)."
        ),
        // Type 4: Complete the missing word from audio
        QuizQuestion(
            id = "q_lis_type4",
            mode = QuizMode.LISTENING_QUIZ,
            jlptLevel = JlptLevel.N4,
            promptJapanese = "京都の古い（  ）に泊まりました。",
            promptSubtext = "Exercise Type 4: Complete the Missing Word",
            questionInstructionBn = "৪. সম্পূর্ণ বাক্যটি শুনে শূন্যস্থানের শব্দটি পূরণ করুন:",
            questionInstructionEn = "4. Listen to the full sentence and fill in the missing word:",
            audioTextToSpeak = "京都の古い旅館に泊まりました。",
            hideJapanesePromptUntilAnswered = false,
            options = listOf("旅館 (りょかん)", "病院 (びょういん)", "銀行 (ぎんこう)", "公園 (こうえん)"),
            correctOptionIndex = 0,
            explanationBn = "বাক্যটি ছিল: 「京都の古い旅館に泊まりました」 (আমরা কিয়োটোর একটি প্রাচীন রিওকানে থেকেছিলাম)।",
            explanationEn = "The missing word is 旅館 (ryokan - traditional Japanese inn)."
        ),
        // Type 5: Understand a Japanese sentence
        QuizQuestion(
            id = "q_lis_type5",
            mode = QuizMode.LISTENING_QUIZ,
            jlptLevel = JlptLevel.N4,
            promptJapanese = "🔊 বাক্যটি শুনে অর্থ অনুধাবন করুন",
            promptSubtext = "Exercise Type 5: Sentence Comprehension",
            questionInstructionBn = "৫. জাপানি বাক্যটি শুনে এর সঠিক অর্থ নির্বাচন করুন:",
            questionInstructionEn = "5. Listen to the Japanese sentence and choose what it means:",
            audioTextToSpeak = "天気予報によると、明日は晴れるそうです。",
            hideJapanesePromptUntilAnswered = true,
            options = listOf(
                "আবহাওয়ার পূর্বাভাস অনুযায়ী আগামীকাল আকাশ পরিষ্কার থাকবে • Forecast says it will be sunny tomorrow",
                "গতকাল সারাদিন প্রচণ্ড বৃষ্টি হয়েছিল • It rained heavily all day yesterday",
                "আগামী সপ্তাহে আমাদের পরীক্ষা শুরু হবে • Our exam starts next week",
                "ট্রেন দেরি করায় মিটিংয়ে বিলম্ব হয়েছে • Late for the meeting due to train delay"
            ),
            correctOptionIndex = 0,
            explanationBn = "「天気予報によると、明日は晴れるそうです」 অর্থ: আবহাওয়ার পূর্বাভাস অনুযায়ী আগামীকাল আকাশ রৌদ্রোজ্জ্বল থাকবে।",
            explanationEn = "天気予報 (weather forecast) + 明日は晴れるそうです (it is said tomorrow will be sunny)."
        )
    )

    private fun buildSentenceOrderingQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "q_order_1",
            mode = QuizMode.SENTENCE_ORDERING,
            jlptLevel = JlptLevel.N5,
            promptJapanese = "আমি প্রতিদিন জাপানি ভাষা অধ্যয়ন করি।",
            promptSubtext = "I study Japanese every day.",
            questionInstructionBn = "শব্দগুলো সঠিক ক্রমে ট্যাপ করে অর্থপূর্ণ জাপানি বাক্য গঠন করুন:",
            questionInstructionEn = "Tap the Japanese words in the correct grammatical order:",
            audioTextToSpeak = "毎日日本語を勉強します。",
            sentenceTokens = listOf("を", "毎日", "勉強します", "日本語"),
            correctSentenceOrder = listOf("毎日", "日本語", "を", "勉強します"),
            explanationBn = "সঠিক ক্রম: 毎日 (প্রতিদিন) + 日本語 (জাপানি ভাষা) + を (কর্ম কণা) + 勉強します (অধ্যয়ন করি)।",
            explanationEn = "Correct order: 毎日 (every day) + 日本語 (Japanese) + を (object particle) + 勉強します (study)."
        ),
        QuizQuestion(
            id = "q_order_2",
            mode = QuizMode.SENTENCE_ORDERING,
            jlptLevel = JlptLevel.N5,
            promptJapanese = "এটি একটি জাপানি ভাষার বই।",
            promptSubtext = "This is a Japanese language book.",
            questionInstructionBn = "শব্দগুলো সঠিক ক্রমে সাজিয়ে বাক্যটি সম্পূর্ণ করুন:",
            questionInstructionEn = "Arrange the tokens to form: 'This is a Japanese language book.'",
            audioTextToSpeak = "これは日本語の本です。",
            sentenceTokens = listOf("本です", "これは", "の", "日本語"),
            correctSentenceOrder = listOf("これは", "日本語", "の", "本です"),
            explanationBn = "সঠিক ক্রম: これは (এটি) + 日本語 (জাপানি ভাষা) + の (এর) + 本です (বই)।",
            explanationEn = "Correct order: これは + 日本語 + の + 本です."
        ),
        QuizQuestion(
            id = "q_order_3",
            mode = QuizMode.SENTENCE_ORDERING,
            jlptLevel = JlptLevel.N4,
            promptJapanese = "আমি কি এখানে ছবি তুলতে পারি?",
            promptSubtext = "May I take photographs here?",
            questionInstructionBn = "অনুমতি প্রার্থনার জাপানি বাক্যটি সঠিক ক্রমে সাজান:",
            questionInstructionEn = "Order the words to ask politely: 'May I take photos here?'",
            audioTextToSpeak = "ここで写真を撮ってもいいですか。",
            sentenceTokens = listOf("撮っても", "ここで", "いいですか", "写真を"),
            correctSentenceOrder = listOf("ここで", "写真を", "撮っても", "いいですか"),
            explanationBn = "সঠিক ক্রম: ここで (এখানে) + 写真を (ছবি) + 撮っても (তুললেও) + いいですか (চলবে কি?)।",
            explanationEn = "Correct order: ここで + 写真を + 撮っても + いいですか."
        )
    )

    private fun buildMatchWordsQuestions(): List<QuizQuestion> {
        val vocabChunks = LearningCatalog.vocabularyList.chunked(4)
        return vocabChunks.take(3).mapIndexed { idx, chunk ->
            QuizQuestion(
                id = "q_match_$idx",
                mode = QuizMode.MATCH_WORDS,
                jlptLevel = chunk.firstOrNull()?.jlptLevel ?: JlptLevel.N5,
                promptJapanese = "শব্দ মিলকরণ সেট #${idx + 1}",
                promptSubtext = "Word Matching Set #${idx + 1}",
                questionInstructionBn = "বাম পাশের জাপানি শব্দের সাথে ডান পাশের সঠিক অর্থটি মিল করুন:",
                questionInstructionEn = "Tap a Japanese word on the left, then tap its matching meaning on the right:",
                matchingPairs = chunk.map { v ->
                    WordMatchPair(
                        pairId = v.id,
                        japaneseText = "${v.wordJp} (${v.hiraganaReading})",
                        readingRomaji = v.romaji,
                        meaningBn = v.meaningBn,
                        meaningEn = v.meaningEn
                    )
                },
                explanationBn = "সকল জোড়া সফলভাবে মিলিত হয়েছে!",
                explanationEn = "All vocabulary pairs matched accurately!"
            )
        }
    }

    private fun buildMultipleChoiceQuestions(): List<QuizQuestion> {
        return (buildJapaneseToBengaliQuestions().take(3) +
            buildKanjiRecognitionQuestions().take(3) +
            buildFillInBlanksQuestions().take(3)).map {
            it.copy(mode = QuizMode.MULTIPLE_CHOICE)
        }
    }
}
