# Nihongo (Android)

Bangla + English Japanese learning app. Kotlin, Jetpack Compose, Room, DataStore.

## APK banano (GitHub Actions)
1. GitHub-e notun repo banan, ei folder-er sob file push korun (`main` branch).
2. Repo -> Actions -> "Build NIHONGO Debug & Release APKs" -> Run workflow.
3. Shesh hole Artifacts section theke `Nihongo-Debug-APK` download korun.

Error ashle Actions log-er error ta copy kore Claude-ke pathan.

## Notes
- Gemini-r 125-ta file theke latest version nie folder structure rebuild kora hoyeche.
- `assets/seed/seed_manifest.json` e shudhu kichu extra vocabulary ache. Main content Kotlin catalog (KanaCatalog, LearningCatalog, JlptCourseCatalog) e ache.
- Native audio: `assets/audio/README_NATIVE_PACKS.txt` dekhun.
