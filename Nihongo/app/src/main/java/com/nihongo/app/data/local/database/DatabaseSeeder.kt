package com.nihongo.app.data.local.database

import android.content.Context
import com.nihongo.app.data.local.database.entity.ExerciseEntity
import com.nihongo.app.data.local.database.entity.GrammarEntity
import com.nihongo.app.data.local.database.entity.KanaEntity
import com.nihongo.app.data.local.database.entity.KanjiEntity
import com.nihongo.app.data.local.database.entity.VocabularyEntity
import com.nihongo.app.domain.model.KanaCatalog
import com.nihongo.app.domain.model.LearningCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class SeedManifestPayload(
    val schemaVersion: Int = 1,
    val extraVocabulary: List<SeedVocabularyDto> = emptyList(),
    val listeningExercises: List<SeedListeningExerciseDto> = emptyList()
)

@Serializable
private data class SeedVocabularyDto(
    val id: String,
    val wordJp: String,
    val hiraganaReading: String,
    val kanjiSpelling: String,
    val romaji: String,
    val meaningBn: String,
    val meaningEn: String,
    val partOfSpeechEn: String,
    val partOfSpeechBn: String,
    val category: String,
    val jlptLevel: String,
    val exampleSentenceJp: String,
    val exampleSentenceReading: String,
    val exampleSentenceRomaji: String,
    val exampleSentenceBn: String,
    val exampleSentenceEn: String,
    val hasNativeAudio: Boolean = false
)

@Serializable
private data class SeedListeningExerciseDto(
    val id: String,
    val jlptLevel: String,
    val exerciseType: String,
    val japaneseAudioText: String,
    val questionBn: String,
    val questionEn: String,
    val optionsCsv: String,
    val correctOptionIndex: Int,
    val explanationBn: String,
    val explanationEn: String
)

/**
 * Seeds the offline Room database from bundled JSON files in `assets/seed/seed_manifest.json`
 * and the verified in-app catalogs using `OnConflictStrategy.IGNORE` so existing user data
 * is never overwritten.
 */
class DatabaseSeeder(
    private val context: Context,
    private val database: NihongoDatabase
) {
    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        val contentDao = database.contentDao()

        // 1. Seed all Hiragana & Katakana items
        val kanaEntities = (KanaCatalog.hiraganaList + KanaCatalog.katakanaList).map { item ->
            KanaEntity(
                id = item.id,
                script = item.script.name,
                category = item.category.name,
                rowGroup = item.rowGroup,
                character = item.character,
                romaji = item.romaji,
                pronunciationBn = item.pronunciationBn,
                explanationEn = item.explanationEn,
                strokeCount = item.strokeCount,
                strokeDirectionsBn = item.strokeDirectionsBn,
                strokeDirectionsEn = item.strokeDirectionsEn,
                exampleWordJp = item.exampleWordJp,
                exampleWordReading = item.exampleWordReading,
                exampleWordRomaji = item.exampleWordRomaji,
                exampleMeaningBn = item.exampleMeaningBn,
                exampleMeaningEn = item.exampleMeaningEn
            )
        }
        contentDao.insertKanaBatch(kanaEntities)

        // 2. Seed all Kanji items (N5 to N1)
        val kanjiEntities = LearningCatalog.kanjiList.map { k ->
            KanjiEntity(
                id = k.id,
                character = k.character,
                jlptLevel = k.jlptLevel.label,
                meaningBn = k.meaningBn,
                meaningEn = k.meaningEn,
                onyomiKana = k.onyomiKana,
                kunyomiKana = k.kunyomiKana,
                romajiReadings = k.romajiReadings,
                strokeCount = k.strokeCount,
                strokeDirectionsBn = k.strokeDirectionsBn,
                strokeDirectionsEn = k.strokeDirectionsEn,
                exampleSentenceJp = k.exampleSentenceJp,
                exampleSentenceReading = k.exampleSentenceReading,
                exampleSentenceRomaji = k.exampleSentenceRomaji,
                exampleSentenceBn = k.exampleSentenceBn,
                exampleSentenceEn = k.exampleSentenceEn,
                relatedKanjiCsv = k.relatedKanji.joinToString(",")
            )
        }
        contentDao.insertKanjiBatch(kanjiEntities)

        // 3. Seed Vocabulary from Catalog + Bundled JSON Asset
        val catalogVocabEntities = LearningCatalog.vocabularyList.map { v ->
            VocabularyEntity(
                id = v.id,
                wordJp = v.wordJp,
                hiraganaReading = v.hiraganaReading,
                kanjiSpelling = v.kanjiSpelling,
                romaji = v.romaji,
                meaningBn = v.meaningBn,
                meaningEn = v.meaningEn,
                partOfSpeechEn = v.partOfSpeechEn,
                partOfSpeechBn = v.partOfSpeechBn,
                category = v.category.name,
                jlptLevel = v.jlptLevel.label,
                exampleSentenceJp = v.exampleSentenceJp,
                exampleSentenceReading = v.exampleSentenceReading,
                exampleSentenceRomaji = v.exampleSentenceRomaji,
                exampleSentenceBn = v.exampleSentenceBn,
                exampleSentenceEn = v.exampleSentenceEn,
                hasNativeAudio = v.hasNativeAudio
            )
        }

        val parsedManifest = runCatching {
            val rawJson = context.assets.open("seed/seed_manifest.json")
                .bufferedReader()
                .use { it.readText() }
            jsonParser.decodeFromString<SeedManifestPayload>(rawJson)
        }.getOrNull()

        val jsonVocabEntities = parsedManifest?.extraVocabulary.orEmpty().map { dto ->
            VocabularyEntity(
                id = dto.id,
                wordJp = dto.wordJp,
                hiraganaReading = dto.hiraganaReading,
                kanjiSpelling = dto.kanjiSpelling,
                romaji = dto.romaji,
                meaningBn = dto.meaningBn,
                meaningEn = dto.meaningEn,
                partOfSpeechEn = dto.partOfSpeechEn,
                partOfSpeechBn = dto.partOfSpeechBn,
                category = dto.category,
                jlptLevel = dto.jlptLevel,
                exampleSentenceJp = dto.exampleSentenceJp,
                exampleSentenceReading = dto.exampleSentenceReading,
                exampleSentenceRomaji = dto.exampleSentenceRomaji,
                exampleSentenceBn = dto.exampleSentenceBn,
                exampleSentenceEn = dto.exampleSentenceEn,
                hasNativeAudio = dto.hasNativeAudio
            )
        }
        contentDao.insertVocabularyBatch(catalogVocabEntities + jsonVocabEntities)

        // 4. Seed Grammar Lessons (N5 to N1)
        val grammarEntities = LearningCatalog.grammarList.map { g ->
            GrammarEntity(
                id = g.id,
                pattern = g.pattern,
                jlptLevel = g.jlptLevel.label,
                titleBn = g.titleBn,
                titleEn = g.titleEn,
                explanationBn = g.explanationBn,
                explanationEn = g.explanationEn,
                formationRulesBn = g.formationRulesBn,
                formationRulesEn = g.formationRulesEn,
                commonMistakesBn = g.commonMistakesBn,
                commonMistakesEn = g.commonMistakesEn
            )
        }
        contentDao.insertGrammarBatch(grammarEntities)

        // 5. Seed Listening Exercises from JSON Asset
        val exerciseEntities = parsedManifest?.listeningExercises.orEmpty().map { ex ->
            ExerciseEntity(
                id = ex.id,
                jlptLevel = ex.jlptLevel,
                exerciseType = ex.exerciseType,
                japaneseAudioText = ex.japaneseAudioText,
                questionBn = ex.questionBn,
                questionEn = ex.questionEn,
                optionsCsv = ex.optionsCsv,
                correctOptionIndex = ex.correctOptionIndex,
                explanationBn = ex.explanationBn,
                explanationEn = ex.explanationEn
            )
        }
        if (exerciseEntities.isNotEmpty()) {
            contentDao.insertExercisesBatch(exerciseEntities)
        }
    }
}
