# Keep Kotlinx Serialization generated serializers for JSON seed files
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep our app domain and entity models for serialization & Room
-keep class com.nihongo.app.data.local.database.entity.** { *; }
-keep class com.nihongo.app.domain.model.** { *; }
-keepclassmembers class com.nihongo.app.** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}

# Room Database rules
-keep class * extends androidx.room.RoomDatabase {
    void <init>();
}
