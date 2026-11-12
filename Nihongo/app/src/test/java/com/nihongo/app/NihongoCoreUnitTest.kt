package com.nihongo.app

import com.nihongo.app.domain.model.AppLanguage
import com.nihongo.app.domain.model.JlptCourseCatalog
import com.nihongo.app.domain.model.JlptLevel
import com.nihongo.app.domain.model.KanaCatalog
import com.nihongo.app.domain.model.KanaCategory
import com.nihongo.app.domain.model.KanaScript
import com.nihongo.app.domain.model.LearningCatalog
import com.nihongo.app.domain.model.QuizMode
import com.nihongo.app.domain.model.SrsCardState
import com.nihongo.app.domain.model.SrsReviewGrade
import com.nihongo.app.domain.model.VocabCategory
import com.nihongo.app.domain.usecase.CalculateSrsScheduleUseCase
import com.nihongo.app.domain.usecase.GenerateQuizUseCase
import com.nihongo.app.ui.theme.AncientWoodBrown
import com.nihongo.app.ui.theme.InkGreen
import com.nihongo.app.ui.theme.MistGray
import com.nihongo.app.ui.theme.PhoenixFlowerRed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Automated Unit Test Suite verifying NIHONGO specifications:
 * 1. Permanent Brand Color Palette (#335749, #C9432D, #D6D0C4, #4B4038)
 * 2. Complete 46 Basic Hiragana & 46 Basic Katakana + Dakuten, Handakuten, Yoon, Special
 * 3. All 17 Vocabulary Categories & N5–N1 Kanji/Grammar coverage
 * 4. Local SM-2 Spaced Repetition schedule math
 * 5. 9-Mode Quiz & 6-Mode Listening generator integrity
 */
class NihongoCoreUnitTest {

    @Test
    fun officialColorPalette_matchesExactSpecificationHexCodes() {
        assertEquals(0xFF335749L, InkGreen.value.toLong() ushr 32)
        assertEquals(0xFFC9432DL, PhoenixFlowerRed.value.toLong() ushr 32)
        assertEquals(0xFFD6D0C4L, MistGray.value.toLong() ushr 32)
        assertEquals(0xFF4B4038L, AncientWoodBrown.value.toLong() ushr 32)
    }

    @Test
    fun hiraganaAndKatakanaCatalogs_containAll46BasicCharactersAndExtensions() {
        val basicHiragana = KanaCatalog.getKanaByScriptAndCategory(
            KanaScript.HIRAGANA,
            KanaCategory.BASIC
        )
        val basicKatakana = KanaCatalog.getKanaByScriptAndCategory(
            KanaScript.KATAKANA,
            KanaCategory.BASIC
        )

        assertEquals(46, basicHiragana.size)
        assertEquals(46, basicKatakana.size)

        // Verify every basic character has a verified positive stroke count
        assertTrue(basicHiragana.all { it.strokeCount in 1..6 })
        assertTrue(basicKatakana.all { it.strokeCount in 1..5 })

        // Verify Dakuten (20), Handakuten (5), and Yoon (33) per script
        assertEquals(
            20,
            KanaCatalog.getKanaByScriptAndCategory(KanaScript.HIRAGANA, KanaCategory.DAKUTEN).size
        )
        assertEquals(
            5,
            KanaCatalog.getKanaByScriptAndCategory(KanaScript.HIRAGANA, KanaCategory.HANDAKUTEN).size
        )
        assertEquals(
            33,
            KanaCatalog.getKanaByScriptAndCategory(KanaScript.HIRAGANA, KanaCategory.YOON).size
        )
        assertEquals(
            33,
            KanaCatalog.getKanaByScriptAndCategory(KanaScript.KATAKANA, KanaCategory.YOON).size
        )
    }

    @Test
    fun vocabularyAndKanjiCatalogs_coverAll17CategoriesAndAll5JlptLevels() {
        assertEquals(17, VocabCategory.entries.size)

        val representedCategories = LearningCatalog.vocabularyList.map { it.category }.toSet()
        assertEquals(17, representedCategories.size)

        val representedKanjiLevels = LearningCatalog.kanjiList.map { it.jlptLevel }.toSet()
        assertEquals(JlptLevel.entries.toSet(), representedKanjiLevels)

        val representedGrammarLevels = LearningCatalog.grammarList.map { it.jlptLevel }.toSet()
        assertEquals(JlptLevel.entries.toSet(), representedGrammarLevels)

        // Verify JLPT Course bundles exist for N5 through N1
        JlptLevel.entries.forEach { level ->
            val bundle = JlptCourseCatalog.getCourseBundle(level)
            assertFalse(bundle.readingPassages.isEmpty())
            assertFalse(bundle.mockExamQuestions.isEmpty())
        }
    }

    @Test
    fun calculateSrsSchedule_computesAccurateSm2Intervals() {
        val useCase = CalculateSrsScheduleUseCase()
        val initial = SrsCardState(itemId = "voc_greet_1", contentType = "VOCAB")
        val now = 1_700_000_000_000L

        // First successful recall ("Know it" -> GOOD) schedules 1 day
        val afterFirstGood = useCase(initial, SrsReviewGrade.GOOD, now)
        assertEquals(1, afterFirstGood.repetitionCount)
        assertEquals(1, afterFirstGood.intervalDays)

        // Second successful recall schedules 4 days
        val afterSecondGood = useCase(afterFirstGood, SrsReviewGrade.GOOD, now)
        assertEquals(2, afterSecondGood.repetitionCount)
        assertEquals(4, afterSecondGood.intervalDays)

        // Failed recall ("Need revision" -> AGAIN) resets repetition count to 0 and interval to 1 day
        val afterAgain = useCase(afterSecondGood, SrsReviewGrade.AGAIN, now)
        assertEquals(0, afterAgain.repetitionCount)
        assertEquals(1, afterAgain.intervalDays)
    }

    @Test
    fun generateQuizUseCase_producesValidQuestionsForAllModes() {
        val generator = GenerateQuizUseCase()
        QuizMode.entries.forEach { mode ->
            val questions = generator.generateQuiz(mode)
            assertFalse("Quiz mode $mode should not be empty", questions.isEmpty())
        }
    }

    @Test
    fun bilingualLanguageSelection_returnsExpectedScript() {
        val bn = "ধন্যবাদ"
        val en = "Thank you"
        assertEquals(bn, AppLanguage.BENGALI.selectText(bn, en))
        assertEquals(en, AppLanguage.ENGLISH.selectText(bn, en))
    }
}
