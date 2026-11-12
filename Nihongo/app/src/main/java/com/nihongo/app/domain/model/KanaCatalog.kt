package com.nihongo.app.domain.model

/**
 * Complete, verified Hiragana and Katakana dataset covering:
 * - 46 Basic characters (Seion) with MEXT-verified stroke counts and stroke directions
 * - 20 Dakuten voiced characters per script
 * - 5 Handakuten p-sound characters per script
 * - 33 Yoon contracted combinations per script
 * - Small tsu (Sokuon), Long vowels (Chouon), and Extended Foreign Katakana combinations
 */
object KanaCatalog {

    val hiraganaList: List<KanaItem> by lazy {
        buildHiraganaBasic() +
            buildHiraganaDakuten() +
            buildHiraganaHandakuten() +
            buildHiraganaYoon() +
            buildHiraganaSpecial()
    }

    val katakanaList: List<KanaItem> by lazy {
        buildKatakanaBasic() +
            buildKatakanaDakuten() +
            buildKatakanaHandakuten() +
            buildKatakanaYoon() +
            buildKatakanaSpecialAndForeign()
    }

    fun getKanaByScriptAndCategory(script: KanaScript, category: KanaCategory): List<KanaItem> {
        val source = if (script == KanaScript.HIRAGANA) hiraganaList else katakanaList
        return source.filter { it.category == category }
    }

    private fun hBasic(
        row: String,
        char: String,
        romaji: String,
        pronBn: String,
        expEn: String,
        strokes: Int,
        dirBn: String,
        dirEn: String,
        wordJp: String,
        wordRead: String,
        wordRom: String,
        meanBn: String,
        meanEn: String
    ) = KanaItem(
        id = "h_basic_$romaji",
        script = KanaScript.HIRAGANA,
        category = KanaCategory.BASIC,
        rowGroup = row,
        character = char,
        romaji = romaji,
        pronunciationBn = pronBn,
        explanationEn = expEn,
        strokeCount = strokes,
        strokeDirectionsBn = dirBn,
        strokeDirectionsEn = dirEn,
        exampleWordJp = wordJp,
        exampleWordReading = wordRead,
        exampleWordRomaji = wordRom,
        exampleMeaningBn = meanBn,
        exampleMeaningEn = meanEn
    )

    private fun buildHiraganaBasic(): List<KanaItem> = listOf(
        // A-row (あ行)
        hBasic("A-Row (あ)", "あ", "a", "আ (বাংলা 'আম'-এর আ ধ্বনির মতো)", "Open central vowel like 'a' in 'father'.", 3,
            "১. বাম থেকে ডানে অনুভূমিক রেখা; ২. উপর থেকে নিচে সামান্য বাঁকা রেখা; ৩. ডান পাশ থেকে নিচে গোল লুপ।",
            "1. Horizontal stroke left to right; 2. Vertical curved stroke top to bottom; 3. Loop from top-right sweeping left and around.",
            "あさ", "あさ", "asa", "সকাল", "Morning"),
        hBasic("A-Row (あ)", "い", "i", "ই (হ্রস্ব ই ধ্বনি, যেমন 'ইট')", "High front vowel like 'ee' in 'meet', kept short.", 2,
            "১. বাম পাশে উপর থেকে নিচে বাঁকা রেখা ও নিচে ছোট হুক; ২. ডান পাশে ছোট বাঁকা রেখা।",
            "1. Longer left vertical curve ending with an upward hook; 2. Shorter right downward curve.",
            "いぬ", "いぬ", "inu", "কুকুর", "Dog"),
        hBasic("A-Row (あ)", "う", "u", "উ (ঠোঁট বেশি গোল না করে সংক্ষিপ্ত উ)", "Unrounded back vowel, similar to 'oo' in 'book' without rounding lips.", 2,
            "১. উপরে ছোট তীর্যক বিন্দু/রেখা; ২. নিচ দিয়ে বাম থেকে ডানে উঠে নিচে বাঁকানো রেখা।",
            "1. Short top slanting dash; 2. Curved arch from left to right sweeping down.",
            "うみ", "うみ", "umi", "সমুদ্র", "Sea / Ocean"),
        hBasic("A-Row (あ)", "え", "e", "এ (বাংলা 'এক'-এর এ ধ্বনি)", "Mid front vowel like 'e' in 'desk'.", 2,
            "১. উপরে ছোট তীর্যক রেখা; ২. অনুভূমিক রেখা টেনে কোণাকুণি নিচে নেমে আবার ডানে ঢেউ।",
            "1. Short top dash; 2. Continuous Z-like brush stroke sweeping right at the base.",
            "えき", "えき", "eki", "রেল স্টেশন", "Train station"),
        hBasic("A-Row (あ)", "お", "o", "ও (সংক্ষিপ্ত ও ধ্বনি, যেমন 'ওল')", "Pure mid back vowel like 'o' in 'more'.", 3,
            "১. অনুভূমিক রেখা; ২. উপর থেকে নিচে নেমে বামে লুপ করে ডানে গোল ঘূর্ণন; ৩. ডান পাশে উপরে ছোট ফোঁটা।",
            "1. Horizontal bar; 2. Vertical stem looping at bottom-left and curving wide right; 3. Small dash at top-right.",
            "おちゃ", "おちゃ", "ocha", "গ্রিন টি / চা", "Green tea"),

        // KA-row (か行)
        hBasic("KA-Row (か)", "か", "ka", "কা (ক + আ)", "Voiceless velar stop 'k' + 'a'.", 3,
            "১. বামে অনুভূমিক থেকে নিচে বাঁক ও হুক; ২. মাঝখানে উপর থেকে নিচে রেখা; ৩. ডানে ছোট বাঁকা রেখা।",
            "1. Horizontal turn curving down with hook; 2. Vertical line cutting through; 3. Short right side dash.",
            "かさ", "かさ", "kasa", "ছাতা", "Umbrella"),
        hBasic("KA-Row (か)", "き", "ki", "কি (ক + ই)", "Voiceless 'k' + 'i' sound.", 4,
            "১. প্রথম অনুভূমিক রেখা; ২. দ্বিতীয় সমান্তরাল রেখা; ৩. তীর্যক উল্লম্ব রেখা; ৪. নিচে বাঁকা চন্দ্রাকৃতি রেখা।",
            "1. Top horizontal bar; 2. Second horizontal bar; 3. Diagonal downward stem; 4. Bottom rounded curve.",
            "き", "き", "ki", "গাছ", "Tree"),
        hBasic("KA-Row (か)", "く", "ku", "কু (ক + উ)", "Voiceless 'k' + unrounded 'u'.", 1,
            "১. এক টানে উপর থেকে বামে কোণ তৈরি করে নিচে ডানে নামুন।",
            "1. Single stroke slanting down-left to a vertex, then down-right.",
            "くるま", "くるま", "kuruma", "গাড়ি", "Car"),
        hBasic("KA-Row (か)", "け", "ke", "কে (ক + এ)", "Voiceless 'k' + 'e'.", 3,
            "১. বামে উল্লম্ব বাঁকা রেখা ও হুক; ২. ডানে ছোট অনুভূমিক রেখা; ৩. উপর থেকে নিচে বামে বাঁকানো রেখা।",
            "1. Left vertical curve with hook; 2. Short horizontal crossbar on right; 3. Right vertical sweeping down-left.",
            "けさ", "けさ", "kesa", "আজ সকাল", "This morning"),
        hBasic("KA-Row (か)", "こ", "ko", "কো (ক + ও)", "Voiceless 'k' + 'o'.", 2,
            "১. উপরে অনুভূমিক রেখা ও ছোট হুক; ২. নিচে সমান্তরাল অনুভূমিক রেখা।",
            "1. Top horizontal line with slight downward hook; 2. Bottom curved horizontal line.",
            "こども", "こども", "kodomo", "শিশু / সন্তান", "Child"),

        // SA-row (さ行)
        hBasic("SA-Row (さ)", "さ", "sa", "সা (দস্ত্য স + আ)", "Crisp 's' + 'a'.", 3,
            "১. অনুভূমিক রেখা; ২. তীর্যক রেখা যা প্রথমটিকে ছেদ করে; ৩. নিচে বাঁকা চন্দ্রাকৃতি রেখা।",
            "1. Horizontal bar; 2. Slanted vertical cutting through; 3. Bottom rounded arc.",
            "さくら", "さくら", "sakura", "চেরি ফুল", "Cherry blossom"),
        hBasic("SA-Row (さ)", "し", "shi", "শি (তালব্য শ + ই, কখনো 'সি' নয়)", "Palatal 'sh' + 'i' like 'she' (never 'si').", 1,
            "১. এক টানে উপর থেকে সোজা নিচে নেমে ডানে উপরে বাঁক।",
            "1. Single J-shaped hook starting at top and sweeping up to the right.",
            "しんぶん", "しんぶん", "shinbun", "সংবাদপত্র", "Newspaper"),
        hBasic("SA-Row (さ)", "す", "su", "সু (স + উ)", "Crisp 's' + unrounded 'u'.", 2,
            "১. অনুভূমিক রেখা; ২. উপর থেকে নিচে নেমে মাঝখানে ছোট গোল লুপ করে নিচে নামুন।",
            "1. Top horizontal bar; 2. Vertical line forming a small circular loop in the middle before sweeping down.",
            "すし", "すし", "sushi", "সুশি", "Sushi"),
        hBasic("SA-Row (さ)", "せ", "se", "সে (স + এ)", "Crisp 's' + 'e'.", 3,
            "১. অনুভূমিক রেখা; ২. ডান পাশের ছোট উল্লম্ব রেখা ও হুক; ৩. বাম পাশের উল্লম্ব রেখা নিচে নেমে ডানে সমান হবে।",
            "1. Horizontal crossbar; 2. Right vertical with hook; 3. Left vertical bending flat to the right at bottom.",
            "せんせい", "せんせい", "sensei", "শিক্ষক", "Teacher"),
        hBasic("SA-Row (さ)", "そ", "so", "সো (স + ও)", "Crisp 's' + 'o'.", 1,
            "১. এক টানে উপরে Z-আকৃতি হয়ে নিচে C-এর মতো বাঁক।",
            "1. Single continuous stroke: top zig-zag flowing into a bottom C-curve.",
            "そら", "そら", "sora", "আকাশ", "Sky"),

        // TA-row (た行)
        hBasic("TA-Row (た)", "た", "ta", "তা (ত + আ, ট নয়)", "Dental 't' + 'a'.", 4,
            "১. বামে অনুভূমিক রেখা; ২. তীর্যক উল্লম্ব রেখা; ৩. ডানে ছোট উপরের রেখা; ৪. ডানে নিচের বাঁকা রেখা।",
            "1. Top-left horizontal; 2. Diagonal vertical intersecting it; 3. Top-right mini curve; 4. Bottom-right mini curve.",
            "たべもの", "たべもの", "tabemono", "খাবার", "Food"),
        hBasic("TA-Row (た)", "ち", "chi", "চি (চ + ই, কখনো 'তি' নয়)", "Affricate 'ch' + 'i' like 'cheese' (never 'ti').", 2,
            "১. উপরে অনুভূমিক রেখা; ২. উপর থেকে নিচে নেমে ডানে গোল পেট তৈরি করে বাঁক।",
            "1. Top horizontal bar; 2. Downward slant curving into a wide right loop (like number 5).",
            "ちず", "ちず", "chizu", "মানচিত্র", "Map"),
        hBasic("TA-Row (た)", "つ", "tsu", "ৎসু (ত + স + উ একসাথে)", "Affricate 'ts' + 'u' like 'tsunami'.", 1,
            "১. এক টানে বাম থেকে ডানে গিয়ে নিচে গোল হয়ে বামে বাঁক।",
            "1. Single wide horizontal curve sweeping round and down-left.",
            "つき", "つき", "tsuki", "চাঁদ", "Moon"),
        hBasic("TA-Row (た)", "て", "te", "তে (ত + এ)", "Dental 't' + 'e'.", 1,
            "১. এক টানে বাম থেকে ডানে গিয়ে মাঝখান দিয়ে নিচে C-এর মতো বাঁক।",
            "1. Single stroke: horizontal line left-to-right, then sweeping back into a central C-curve.",
            "てがみ", "てがみ", "tegami", "চিঠি", "Letter"),
        hBasic("TA-Row (た)", "と", "to", "তো (ত + ও)", "Dental 't' + 'o'.", 2,
            "১. উপর থেকে তীর্যক ছোট রেখা; ২. ডান থেকে এসে নিচে গোল বাঁক।",
            "1. Short diagonal downward line; 2. C-shaped loop meeting stroke 1 and curving flat right.",
            "とり", "とり", "tori", "পাখি", "Bird"),

        // NA-row (な行)
        hBasic("NA-Row (な)", "な", "na", "না (ন + আ)", "Alveolar nasal 'n' + 'a'.", 4,
            "১. বামে অনুভূমিক রেখা; ২. তীর্যক উল্লম্ব রেখা; ৩. ডানে উপরে ছোট ফোঁটা; ৪. নিচে লুপযুক্ত রেখা।",
            "1. Top-left horizontal; 2. Slanted vertical; 3. Top-right dash; 4. Vertical dropping into a bottom knot loop.",
            "なつ", "なつ", "natsu", "গ্রীষ্মকাল", "Summer"),
        hBasic("NA-Row (な)", "に", "ni", "নি (ন + ই)", "Alveolar nasal 'n' + 'i'.", 3,
            "১. বামে উল্লম্ব বাঁকা রেখা; ২. ডানে উপরের অনুভূমিক রেখা; ৩. ডানে নিচের অনুভূমিক রেখা।",
            "1. Left vertical curve with hook; 2. Top-right horizontal bar; 3. Bottom-right horizontal bar.",
            "にほん", "にほん", "nihon", "জাপান", "Japan"),
        hBasic("NA-Row (な)", "ぬ", "ぬ".let { "nu" }, "নু (ন + উ)", "Alveolar nasal 'n' + 'u'.", 2,
            "১. বামে তীর্যক রেখা; ২. উপর থেকে বামে ঘুরে ডানে বড় লুপ ও শেষে ছোট গিঁট।",
            "1. Left downward slant; 2. Long sweeping loop crossing stroke 1 and ending in a small tail knot.",
            "ぬの", "ぬの", "nuno", "কাপড়", "Cloth"),
        hBasic("NA-Row (な)", "ね", "ne", "নে (ন + এ)", "Alveolar nasal 'n' + 'e'.", 2,
            "১. বামে সোজা উল্লম্ব রেখা; ২. Z-এর মতো রেখা টেনে উপরে উঠে নিচে ছোট গিঁট।",
            "1. Left vertical stem; 2. Continuous Z-cross sweeping up and down into a small bottom-right loop.",
            "ねこ", "ねこ", "neko", "বিড়াল", "Cat"),
        hBasic("NA-Row (な)", "の", "no", "নো (ন + ও)", "Alveolar nasal 'n' + 'o'.", 1,
            "১. এক টানে মাঝখান থেকে নিচে নেমে বাম দিয়ে উপরে উঠে ডানে গোল হয়ে নামুন।",
            "1. Single stroke: slant down-left, loop up over the top, and sweep down-right.",
            "のみもの", "のみもの", "nomimono", "পানীয়", "Drink / Beverage"),

        // HA-row (は行)
        hBasic("HA-Row (は)", "は", "ha", "হা (হ + আ; কণা হিসেবে 'ওয়া' উচ্চারিত হয়)", "'h' + 'a' (pronounced 'wa' when used as topic particle).", 3,
            "১. বামে উল্লম্ব বাঁকা রেখা; ২. ডানে অনুভূমিক রেখা; ৩. উপর থেকে নিচে নেমে বামে ছোট লুপ।",
            "1. Left vertical curve; 2. Right horizontal bar; 3. Right vertical stem ending in a bottom loop.",
            "はな", "はな", "hana", "ফুল", "Flower"),
        hBasic("HA-Row (は)", "ひ", "hi", "হি (হ + ই)", "Palatalized 'h' + 'i'.", 1,
            "১. এক টানে বাম থেকে নিচে গভীর ইউ-আকৃতি (U) হয়ে ডানে নিচে ছোট টান।",
            "1. Single stroke: short horizontal, deep U-dip, sweeping up and finishing with a short downward flick.",
            "ひと", "ひと", "hito", "মানুষ / ব্যক্তি", "Person"),
        hBasic("HA-Row (は)", "ふ", "fu", "ফু (দুই ঠোঁটের মাঝ দিয়ে বাতাস ছেড়ে হালকা ফু/হু)", "Bilabial fricative between 'fu' and 'hu' (lips do not touch teeth).", 4,
            "১. উপরে ফোঁটা; ২. মাঝখানে বাঁকা নাসিকা রেখা; ৩. বামে ছোট রেখা; ৪. ডানে ছোট রেখা।",
            "1. Top dash; 2. Central curved hook; 3. Left side dot; 4. Right side dot.",
            "ふゆ", "ふゆ", "fuyu", "শীতকাল", "Winter"),
        hBasic("HA-Row (は)", "へ", "he", "হে (হ + এ; কণা হিসেবে 'এ' উচ্চারিত হয়)", "'h' + 'e' (pronounced 'e' when used as direction particle).", 1,
            "১. এক টানে বাম থেকে উপরে উঠে পর্বতের চূড়ার মতো ডানে নিচে নামুন।",
            "1. Single mountain-peak stroke: short up-right slant followed by a longer down-right slope.",
            "へや", "へや", "heya", "ঘর / কক্ষ", "Room"),
        hBasic("HA-Row (は)", "ほ", "ho", "হো (হ + ও)", "'h' + 'o'.", 4,
            "১. বামে উল্লম্ব রেখা; ২. ডানে উপরের অনুভূমিক রেখা; ৩. দ্বিতীয় অনুভূমিক রেখা; ৪. উল্লম্ব রেখা ও নিচে লুপ।",
            "1. Left vertical curve; 2. Top-right horizontal; 3. Second horizontal; 4. Vertical stem from top bar ending in bottom loop.",
            "ほん", "ほん", "hon", "বই", "Book"),

        // MA-row (ま行)
        hBasic("MA-Row (ま)", "ま", "ma", "মা (ম + আ)", "Bilabial nasal 'm' + 'a'.", 3,
            "১. উপরের অনুভূমিক রেখা; ২. নিচের সমান্তরাল রেখা; ৩. উপর থেকে নিচে নেমে বামে লুপ।",
            "1. Top horizontal bar; 2. Second horizontal bar; 3. Central vertical stem ending in a bottom knot loop.",
            "まど", "まど", "mado", "জানালা", "Window"),
        hBasic("MA-Row (ま)", "み", "mi", "মি (ম + ই)", "Bilabial nasal 'm' + 'i'.", 2,
            "১. উপরে ছোট অনুভূমিক রেখা টেনে নিচে বামে লুপ করে ডানে যান; ২. ডান থেকে নিচে বাঁকা ছেদ রেখা।",
            "1. Horizontal top, diagonal down into a left loop and sweeping right; 2. Short cross-slash on the right.",
            "みず", "みず", "mizu", "পানি / জল", "Water"),
        hBasic("MA-Row (ま)", "む", "mu", "মু (ম + উ)", "Bilabial nasal 'm' + 'u'.", 3,
            "১. অনুভূমিক রেখা; ২. নিচে নেমে ছোট লুপ করে ডানে গিয়ে উপরে হুক; ৩. ডানে উপরে ছোট ফোঁটা।",
            "1. Top-left horizontal; 2. Vertical stem looping in the middle, sweeping flat right and hooking up; 3. Top-right dash.",
            "むし", "むし", "mushi", "পোকা / কীট", "Insect"),
        hBasic("MA-Row (ま)", "め", "me", "মে (ম + এ)", "Bilabial nasal 'm' + 'e'.", 2,
            "১. বামে তীর্যক বাঁকা রেখা; ২. উপর থেকে নিচে বামে ঘুরে বড় খোলা গোল বাঁক।",
            "1. Left slanting downward curve; 2. Longer stroke crossing stroke 1 and looping wide around to the bottom.",
            "め", "め", "me", "চোখ", "Eye"),
        hBasic("MA-Row (ま)", "も", "mo", "মো (ম + ও)", "Bilabial nasal 'm' + 'o'.", 3,
            "১. মাঝখানে উপর থেকে নিচে বাঁক ও ডানে হুক; ২. উপরের অনুভূমিক রেখা; ৩. নিচের অনুভূমিক রেখা।",
            "1. Central J-curved hook; 2. Top horizontal crossbar; 3. Second horizontal crossbar.",
            "もり", "もり", "mori", "বন / জঙ্গল", "Forest"),

        // YA-row (や行)
        hBasic("YA-Row (や)", "や", "ya", "ইয়া (ইয় + আ)", "Palatal approximant 'y' + 'a'.", 3,
            "১. বাম থেকে ডানে উঠে নিচে গোল বাঁক; ২. উপরে ছোট ফোঁটা; ৩. বাম থেকে তীর্যকভাবে নিচে লম্বা রেখা।",
            "1. Rising curve looping right; 2. Short dash above; 3. Long diagonal stem cutting down.",
            "やま", "やま", "yama", "পাহাড় / পর্বত", "Mountain"),
        hBasic("YA-Row (や)", "ゆ", "yu", "ইউ (ইয় + উ)", "Palatal approximant 'y' + 'u'.", 2,
            "১. বামে নিচে নেমে উপরে ডানে বড় ডিম্বাকৃতি বাঁক; ২. উপর থেকে নিচে লম্বা ছেদকারী রেখা।",
            "1. Vertical line sweeping up into a wide oval arch; 2. Vertical center-right stroke cutting through.",
            "ゆき", "ゆき", "yuki", "তুষার / বরফ", "Snow"),
        hBasic("YA-Row (や)", "よ", "yo", "ইয়ো (ইয় + ও)", "Palatal approximant 'y' + 'o'.", 2,
            "১. ডান পাশে ছোট অনুভূমিক রেখা; ২. উপর থেকে নিচে নেমে বামে লুপ।",
            "1. Short horizontal bar on right of stem; 2. Vertical stem starting at top and looping at bottom.",
            "よる", "よる", "yoru", "রাত", "Night"),

        // RA-row (ら行)
        hBasic("RA-Row (ら)", "ら", "ra", "রা (জিহ্বার ডগা তালুতে হালকা ছুঁয়ে র ও ড-এর মাঝামাঝি)", "Alveolar tap: tongue lightly taps roof of mouth (between English 'r' and 'd').", 2,
            "১. উপরে ছোট তীর্যক ফোঁটা; ২. নিচে নেমে ডানে গোল পেট তৈরি করে বাঁক।",
            "1. Top angled dash; 2. Downward vertical turning into a rounded right belly.",
            "らいねん", "らいねん", "rainen", "আগামী বছর", "Next year"),
        hBasic("RA-Row (ら)", "り", "ri", "রি (র + ই)", "Alveolar tap 'r' + 'i'.", 2,
            "১. বামে ছোট উল্লম্ব রেখা ও হুক; ২. ডানে লম্বা বাঁকা উল্লম্ব রেখা।",
            "1. Short left vertical with upward hook; 2. Longer right vertical curving down-left.",
            "りんご", "りんご", "ringo", "আপেল", "Apple"),
        hBasic("RA-Row (ら)", "る", "ru", "রু (র + উ)", "Alveolar tap 'r' + 'u'.", 1,
            "১. এক টানে অনুভূমিক রেখা, তীর্যক নিচে, ডানে গোল বাঁক এবং শেষে ভেতরে ছোট লুপ।",
            "1. Single stroke like 'ro' but ending with a small inward loop at the base.",
            "るす", "るす", "rusu", "বাসায় অনুপস্থিতি", "Absence from home"),
        hBasic("RA-Row (ら)", "れ", "re", "রে (র + এ)", "Alveolar tap 'r' + 'e'.", 2,
            "১. বামে উল্লম্ব রেখা; ২. Z-এর মতো রেখা টেনে উপরে উঠে নিচে ডানে বাইরে হুক।",
            "1. Left vertical stem; 2. Z-cross rising high on right and sweeping down with an outward flick.",
            "れきし", "れきし", "rekishi", "ইতিহাস", "History"),
        hBasic("RA-Row (ら)", "ろ", "ro", "রো (র + ও)", "Alveolar tap 'r' + 'o'.", 1,
            "১. এক টানে উপরে অনুভূমিক রেখা থেকে কোণাকুণি নেমে ডানে গোল বাঁক (লুপ ছাড়া)।",
            "1. Single stroke: horizontal top, diagonal down, wide rounded curve with no bottom loop.",
            "ろく", "ろく", "roku", "ছয় (৬)", "Six (6)"),

        // WA-row & N (わ行・ん)
        hBasic("WA-Row (わ)", "わ", "wa", "ওয়া (ওয় + আ)", "Bilabial approximant 'w' + 'a'.", 2,
            "১. বামে সোজা উল্লম্ব রেখা; ২. বাম থেকে ডানে উঠে বড় গোল বাঁক দিয়ে নিচে নামুন।",
            "1. Left vertical stem; 2. Crossbar slanting up then curving wide down on the right.",
            "わたし", "わたし", "watashi", "আমি", "I / Me"),
        hBasic("WA-Row (わ)", "を", "wo", "ও (শুধুমাত্র কর্মকারক কণা হিসেবে ব্যবহৃত, উচ্চারণ 'ও')", "Direct object grammatical particle (pronounced 'o', typed 'wo').", 3,
            "১. অনুভূমিক রেখা; ২. নিচে নেমে ছোট হুক; ৩. ডান থেকে নিচে সি-আকৃতির (C) বাঁক।",
            "1. Top horizontal bar; 2. Diagonal down to a central step/hook; 3. C-curve sweeping across the bottom.",
            "みずをのむ", "みずをのむ", "mizu wo nomu", "পানি পান করা", "To drink water"),
        hBasic("WA-Row (わ)", "ん", "n", "ন / ং / ম (পরবর্তী ধ্বনি অনুযায়ী নাসিক্য ধ্বনি)", "Syllabic nasal (n / m / ng depending on following consonant; never begins a word).", 1,
            "১. এক টানে উপর থেকে তীর্যক নিচে নেমে মাঝখানে ছোট ভাঁজ দিয়ে ডানে উপরে বাঁক।",
            "1. Single cursive 'h'-shaped stroke ending with an upward sweep.",
            "にほんご", "にほんご", "nihongo", "জাপানি ভাষা", "Japanese language")
    )

    private fun buildHiraganaDakuten(): List<KanaItem> {
        data class Entry(val row: String, val ch: String, val rom: String, val bn: String, val word: String, val wRom: String, val mBn: String, val mEn: String)
        val entries = listOf(
            Entry("GA-Row (が)", "が", "ga", "গা (গ + আ)", "がっこう", "gakkou", "বিদ্যালয় / স্কুল", "School"),
            Entry("GA-Row (が)", "ぎ", "gi", "গি (গ + ই)", "ぎんこう", "ginkou", "ব্যাংক", "Bank"),
            Entry("GA-Row (が)", "ぐ", "gu", "গু (গ + উ)", "ぐんて", "gunte", "কাজের দস্তানা", "Work gloves"),
            Entry("GA-Row (が)", "げ", "ge", "গে (গ + এ)", "げんき", "genki", "সুস্থ ও প্রাণবন্ত", "Healthy / Energetic"),
            Entry("GA-Row (が)", "ご", "go", "গো (গ + ও)", "ごはん", "gohan", "ভাত / খাবার", "Cooked rice / Meal"),

            Entry("ZA-Row (ざ)", "ざ", "za", "যা (য/জ + আ)", "ざっし", "zasshi", "ম্যাগাজিন / পত্রিকা", "Magazine"),
            Entry("ZA-Row (ざ)", "じ", "ji", "জি (জ + ই)", "じしょ", "jisho", "অভিধান", "Dictionary"),
            Entry("ZA-Row (ざ)", "ず", "zu", "যু (য/জ + উ)", "しずか", "shizuka", "শান্ত / নীরব", "Quiet"),
            Entry("ZA-Row (ざ)", "ぜ", "ze", "যে (য/জ + এ)", "かぜ", "kaze", "বাতাস / ঠান্ডা লাগা", "Wind / Cold"),
            Entry("ZA-Row (ざ)", "ぞ", "zo", "যো (য/জ + ও)", "かぞく", "kazoku", "পরিবার", "Family"),

            Entry("DA-Row (だ)", "だ", "da", "দা (দ + আ)", "だいがく", "daigaku", "বিশ্ববিদ্যালয়", "University"),
            Entry("DA-Row (だ)", "ぢ", "ji (di)", "জি (ছি-তে ডাকুতেন, উচ্চারণ 'জি')", "はなぢ", "hanaji", "নাক দিয়ে রক্ত পড়া", "Nosebleed"),
            Entry("DA-Row (だ)", "づ", "zu (du)", "যু (ৎসু-তে ডাকুতেন, উচ্চারণ 'যু')", "つづく", "tsuzuku", "চলমান থাকা", "To continue"),
            Entry("DA-Row (だ)", "で", "de", "দে (দ + এ)", "でんしゃ", "densha", "ট্রেন", "Train"),
            Entry("DA-Row (だ)", "ど", "do", "দো (দ + ও)", "どこ", "doko", "কোথায়", "Where"),

            Entry("BA-Row (ば)", "ば", "ba", "বা (ব + আ)", "かばん", "kaban", "ব্যাগ", "Bag"),
            Entry("BA-Row (ば)", "び", "bi", "বি (ব + ই)", "びょういん", "byouin", "হাসপাতাল", "Hospital"),
            Entry("BA-Row (ば)", "ぶ", "bu", "বু (ব + উ)", "しんぶん", "shinbun", "সংবাদপত্র", "Newspaper"),
            Entry("BA-Row (ば)", "べ", "be", "বে (ব + এ)", "かべ", "kabe", "দেয়াল", "Wall"),
            Entry("BA-Row (ば)", "ぼ", "bo", "বো (ব + ও)", "ぼうし", "boushi", "টুপি", "Hat / Cap")
        )
        return entries.map { e ->
            KanaItem(
                id = "h_dak_${e.rom}",
                script = KanaScript.HIRAGANA,
                category = KanaCategory.DAKUTEN,
                rowGroup = e.row,
                character = e.ch,
                romaji = e.rom,
                pronunciationBn = e.bn,
                explanationEn = "Voiced Dakuten sound (${e.rom}): write the base character first, then add two small dakuten strokes at top-right.",
                strokeCount = 0,
                strokeDirectionsBn = "প্রথমে মূল বর্ণটি লিখুন, তারপর ডান পাশে উপরে দুটি ছোট দাগ (゛) দিন।",
                strokeDirectionsEn = "Write the base Hiragana character first, then add the two short dakuten ticks at the top-right.",
                exampleWordJp = e.word,
                exampleWordReading = e.word,
                exampleWordRomaji = e.wRom,
                exampleMeaningBn = e.mBn,
                exampleMeaningEn = e.mEn
            )
        }
    }

    private fun buildHiraganaHandakuten(): List<KanaItem> {
        data class Entry(val ch: String, val rom: String, val bn: String, val strokes: Int, val word: String, val wRom: String, val mBn: String, val mEn: String)
        val entries = listOf(
            Entry("ぱ", "pa", "পা (প + আ)", 4, "ぱん", "pan", "রুটি / পাউরুটি", "Bread"),
            Entry("ぴ", "pi", "পি (প + ই)", 2, "えんぴつ", "enpitsu", "পেন্সিল", "Pencil"),
            Entry("ぷ", "pu", "পু (প + উ)", 5, "てんぷら", "tenpura", "তেম্পুরা (ভাজা খাবার)", "Tempura"),
            Entry("ぺ", "pe", "পে (প + এ)", 2, "きっぺ", "kippe", "কাঠের টুকরো", "Wood piece"),
            Entry("ぽ", "po", "পো (প + ও)", 5, "さんぽ", "sanpo", "হাঁটাহাঁটি / ভ্রমণ", "Stroll / Walk")
        )
        return entries.map { e ->
            KanaItem(
                id = "h_han_${e.rom}",
                script = KanaScript.HIRAGANA,
                category = KanaCategory.HANDAKUTEN,
                rowGroup = "PA-Row (ぱ)",
                character = e.ch,
                romaji = e.rom,
                pronunciationBn = e.bn,
                explanationEn = "Voiceless bilabial plosive '${e.rom}' formed by adding the handakuten circle (゜) to the HA-row character.",
                strokeCount = e.strokes,
                strokeDirectionsBn = "প্রথমে হ-সারির মূল বর্ণটি লিখুন, এরপর ডান পাশে উপরে ছোট বৃত্ত (゜) আঁকুন।",
                strokeDirectionsEn = "Write the base HA-row character first, then draw the small handakuten circle at the top-right.",
                exampleWordJp = e.word,
                exampleWordReading = e.word,
                exampleWordRomaji = e.wRom,
                exampleMeaningBn = e.mBn,
                exampleMeaningEn = e.mEn
            )
        }
    }

    private fun buildHiraganaYoon(): List<KanaItem> {
        data class Entry(val group: String, val ch: String, val rom: String, val bn: String, val word: String, val wRom: String, val mBn: String, val mEn: String)
        val entries = listOf(
            Entry("KYA", "きゃ", "kya", "কিয়া", "きゃく", "kyaku", "অতিথি / ক্রেতা", "Guest / Customer"),
            Entry("KYA", "きゅ", "kyu", "কিউ", "きゅう", "kyuu", "নয় (৯)", "Nine (9)"),
            Entry("KYA", "きょ", "kyo", "কিয়ো", "きょう", "kyou", "আজ", "Today"),

            Entry("SHA", "しゃ", "sha", "শা", "しゃしん", "shashin", "ছবি / আলোকচিত্র", "Photograph"),
            Entry("SHA", "しゅ", "shu", "শু", "しゅくだい", "shukudai", "বাড়ির কাজ", "Homework"),
            Entry("SHA", "しょ", "sho", "শো", "としょかん", "toshokan", "গ্রন্থাগার / লাইব্রেরি", "Library"),

            Entry("CHA", "ちゃ", "cha", "চা", "おちゃ", "ocha", "গ্রিন টি / চা", "Green tea"),
            Entry("CHA", "ちゅ", "chu", "চু", "ちゅうごく", "chuugoku", "চীন", "China"),
            Entry("CHA", "ちょ", "cho", "চো", "ちょうちょ", "choucho", "প্রজাপতি", "Butterfly"),

            Entry("NYA", "にゃ", "nya", "নিয়া", "にゃんこ", "nyanko", "বিড়ালছানা (আদুরে)", "Kitty"),
            Entry("NYA", "にゅ", "nyu", "নিউ", "ぎゅうにゅう", "gyuunyuu", "গরুর দুধ", "Cow's milk"),
            Entry("NYA", "にょ", "nyo", "নিয়ো", "にょうぼう", "nyoubou", "স্ত্রী", "Wife"),

            Entry("HYA", "ひゃ", "hya", "হিয়া", "ひゃく", "hyaku", "একশো (১০০)", "One hundred"),
            Entry("HYA", "ひゅ", "hyu", "হিউ", "ひゅうが", "hyuuga", "রৌদ্রোজ্জ্বল স্থান", "Sunny place"),
            Entry("HYA", "ひょ", "hyo", "হিয়ো", "ひょう", "hyou", "তালিকা / চার্ট", "Table / Chart"),

            Entry("MYA", "みゃ", "mya", "মিয়া", "みゃく", "myaku", "নাড়ি / স্পন্দন", "Pulse"),
            Entry("MYA", "みゅ", "myu", "মিউ", "みゅうじあむ", "myuujiamu", "জাদুঘর", "Museum"),
            Entry("MYA", "みょ", "myo", "মিয়ো", "みょうじ", "myouji", "বংশনাম / পদবি", "Surname"),

            Entry("RYA", "りゃ", "rya", "রিয়া", "りゃくご", "ryakugo", "সংক্ষিপ্ত রূপ", "Abbreviation"),
            Entry("RYA", "りゅ", "ryu", "রিউ", "りゅうがく", "ryuugaku", "বিদেশে উচ্চশিক্ষা", "Study abroad"),
            Entry("RYA", "りょ", "ryo", "রিয়ো", "りょこう", "ryokou", "ভ্রমণ", "Travel / Trip"),

            Entry("GYA", "ぎゃ", "gya", "গিয়া", "ぎゃく", "gyaku", "বিপরীত", "Opposite / Reverse"),
            Entry("GYA", "ぎゅ", "gyu", "গিউ", "ぎゅうにく", "gyuuniku", "গরুর মাংস", "Beef"),
            Entry("GYA", "ぎょ", "gyo", "গিয়ো", "きんぎょ", "kingyo", "গোল্ডফিশ", "Goldfish"),

            Entry("JA", "じゃ", "ja", "জা", "じゃがいも", "jagaimo", "আলু", "Potato"),
            Entry("JA", "じゅ", "ju", "জু", "じゅう", "juu", "দশ (১০)", "Ten (10)"),
            Entry("JA", "じょ", "jo", "জো", "じょうず", "jouzu", "দক্ষ / পারদর্শী", "Skillful"),

            Entry("BYA", "びゃ", "bya", "বিয়া", "さんびゃく", "sanbyaku", "তিনশো (৩০০)", "Three hundred"),
            Entry("BYA", "びゅ", "byu", "বিউ", "びゅう", "byuu", "বাতাসের শোঁ শোঁ শব্দ", "Whoosh of wind"),
            Entry("BYA", "びょ", "byo", "বিয়ো", "びょういん", "byouin", "হাসপাতাল", "Hospital"),

            Entry("PYA", "ぴゃ", "pya", "পিয়া", "ろっぴゃく", "roppyaku", "ছয়শো (৬০০)", "Six hundred"),
            Entry("PYA", "ぴゅ", "pyu", "পিউ", "ぴゅう", "pyuu", "দ্রুত ছুটে যাওয়ার শব্দ", "Whiz / Zip sound"),
            Entry("PYA", "ぴょ", "pyo", "পিয়ো", "はっぴょう", "happyou", "উপস্থাপনা / ঘোষণা", "Presentation / Announcement")
        )
        return entries.map { e ->
            KanaItem(
                id = "h_yoon_${e.rom}",
                script = KanaScript.HIRAGANA,
                category = KanaCategory.YOON,
                rowGroup = "${e.group}-Group",
                character = e.ch,
                romaji = e.rom,
                pronunciationBn = "${e.bn} (এক মাত্রায় সংযুক্ত উচ্চারণ)",
                explanationEn = "Contracted Yōon syllable (${e.rom}) pronounced smoothly as a single beat (1 mora).",
                strokeCount = 0,
                strokeDirectionsBn = "প্রথমে পূর্ণ আকারের ই-কলামের বর্ণ এবং ডান পাশে নিচে অর্ধেক আকারের ছোট ゃ/ゅ/ょ লিখুন।",
                strokeDirectionsEn = "Write the full-size i-column character followed by a half-sized ゃ, ゅ, or ょ at the bottom-right.",
                exampleWordJp = e.word,
                exampleWordReading = e.word,
                exampleWordRomaji = e.wRom,
                exampleMeaningBn = e.mBn,
                exampleMeaningEn = e.mEn
            )
        }
    }

    private fun buildHiraganaSpecial(): List<KanaItem> = listOf(
        KanaItem(
            id = "h_spec_sokuon",
            script = KanaScript.HIRAGANA,
            category = KanaCategory.SPECIAL_AND_EXTENDED,
            rowGroup = "Small Tsu (促音)",
            character = "っ",
            romaji = "(double consonant)",
            pronunciationBn = "ছোট 'っ' (পরবর্তী ব্যঞ্জনধ্বনিকে দ্বিত্ব বা ডাবল করে, যেমন: কিত্তে)",
            explanationEn = "Sokuon (small tsu): creates a 1-beat pause that doubles the following consonant (kk, pp, ss, tt).",
            strokeCount = 1,
            strokeDirectionsBn = "১. সাধারণ つ-এর মতোই এক টানে, তবে আকারে অর্ধেক ছোট করে নিচে লিখুন।",
            strokeDirectionsEn = "1. Single curved stroke identical to 'tsu' but written at half size in the lower quadrant.",
            exampleWordJp = "きって",
            exampleWordReading = "きって",
            exampleWordRomaji = "kitte",
            exampleMeaningBn = "ডাকটিকিট (উচ্চারণ: কিত্-তে)",
            exampleMeaningEn = "Postage stamp"
        ),
        KanaItem(
            id = "h_spec_long_aa",
            script = KanaScript.HIRAGANA,
            category = KanaCategory.SPECIAL_AND_EXTENDED,
            rowGroup = "Long Vowels (長音)",
            character = "ああ / おう",
            romaji = "aa / ii / uu / ei / ou",
            pronunciationBn = "দীর্ঘ স্বরধ্বনি (স্বরবর্ণ দুই মাত্রা পর্যন্ত টেনে উচ্চারণ করতে হয়)",
            explanationEn = "Long vowels in Hiragana are formed by adding あ, い, う, or う after o/e sounds, holding the vowel for two beats.",
            strokeCount = 0,
            strokeDirectionsBn = "হিরাগানায় দীর্ঘ স্বর বোঝাতে আ-ধ্বনির পর あ, ই-ধ্বনির পর い এবং ও-ধ্বনির পর সাধারণত う বসে।",
            strokeDirectionsEn = "Extend 'a' with あ, 'i' with い, 'u' with う, 'e' with い/え, and 'o' with う (or お in select words like おおきい).",
            exampleWordJp = "おかあさん",
            exampleWordReading = "おかあさん",
            exampleWordRomaji = "okaasan",
            exampleMeaningBn = "মা",
            exampleMeaningEn = "Mother"
        )
    )

    private fun kBasic(
        row: String,
        char: String,
        romaji: String,
        pronBn: String,
        expEn: String,
        strokes: Int,
        dirBn: String,
        dirEn: String,
        wordJp: String,
        wordRom: String,
        meanBn: String,
        meanEn: String
    ) = KanaItem(
        id = "k_basic_$romaji",
        script = KanaScript.KATAKANA,
        category = KanaCategory.BASIC,
        rowGroup = row,
        character = char,
        romaji = romaji,
        pronunciationBn = pronBn,
        explanationEn = expEn,
        strokeCount = strokes,
        strokeDirectionsBn = dirBn,
        strokeDirectionsEn = dirEn,
        exampleWordJp = wordJp,
        exampleWordReading = wordJp,
        exampleWordRomaji = wordRom,
        exampleMeaningBn = meanBn,
        exampleMeaningEn = meanEn
    )

    private fun buildKatakanaBasic(): List<KanaItem> = listOf(
        // A-row (ア行)
        kBasic("A-Row (ア)", "ア", "a", "আ", "Katakana 'a' used in loanwords and names.", 2,
            "১. বাম থেকে ডানে অনুভূমিক রেখা টেনে কোণাকুণি নিচে ছোট ভাঁজ; ২. মাঝখান থেকে নিচে বামে বাঁকা রেখা।",
            "1. Horizontal bar left-to-right with sharp angled hook down-left; 2. Central downward curve sweeping left.",
            "アジア", "Ajia", "এশিয়া", "Asia"),
        kBasic("A-Row (ア)", "イ", "i", "ই", "Katakana 'i'.", 2,
            "১. উপর থেকে বামে তীর্যক রেখা; ২. প্রথম রেখার মাঝখান থেকে সোজা নিচে উল্লম্ব রেখা।",
            "1. Diagonal stroke from top-right to mid-left; 2. Vertical stroke dropping down from the middle of stroke 1.",
            "インド", "Indo", "ভারত", "India"),
        kBasic("A-Row (ア)", "ウ", "u", "উ", "Katakana 'u'.", 3,
            "১. উপরে মাঝখানে ছোট উল্লম্ব রেখা; ২. বামে ছোট উল্লম্ব রেখা; ৩. অনুভূমিক ছাদ থেকে ডানে নিচে বাঁকা রেখা।",
            "1. Short top center tick; 2. Short left vertical; 3. Horizontal top turning down and curving left.",
            "ウール", "uuru", "পশম / উল", "Wool"),
        kBasic("A-Row (ア)", "エ", "e", "এ", "Katakana 'e'.", 3,
            "১. উপরের অনুভূমিক রেখা; ২. মাঝখানের উল্লম্ব রেখা; ৩. নিচের লম্বা অনুভূমিক রেখা।",
            "1. Top horizontal bar; 2. Center vertical stem; 3. Wider bottom horizontal base.",
            "エアコン", "eakon", "এয়ার কন্ডিশনার", "Air conditioner"),
        kBasic("A-Row (ア)", "オ", "o", "ও", "Katakana 'o'.", 3,
            "১. অনুভূমিক রেখা; ২. উল্লম্ব রেখা ও নিচে বামে হুক; ৩. ছেদবিন্দু থেকে বামে নিচে তীর্যক রেখা।",
            "1. Horizontal bar; 2. Vertical stem with bottom-left hook; 3. Diagonal sweep from intersection down-left.",
            "オレンジ", "orenji", "কমলালেবু", "Orange"),

        // KA-row (カ行)
        kBasic("KA-Row (カ)", "カ", "ka", "কা", "Katakana 'ka'.", 2,
            "১. অনুভূমিক রেখা টেনে নিচে নেমে হুক; ২. উপর থেকে বামে নিচে বাঁকা রেখা।",
            "1. Horizontal line turning down with a hook; 2. Long downward curve cutting through to bottom-left.",
            "カメラ", "kamera", "ক্যামেরা", "Camera"),
        kBasic("KA-Row (カ)", "キ", "ki", "কি", "Katakana 'ki'.", 3,
            "১. উপরের সমান্তরাল রেখা; ২. নিচের সমান্তরাল রেখা; ৩. উপর থেকে নিচে তীর্যক ছেদকারী রেখা।",
            "1. Top slanted horizontal; 2. Second slanted horizontal; 3. Diagonal vertical stem cutting through both.",
            "キロ", "kiro", "কিলোগ্রাম / কিলোমিটার", "Kilo"),
        kBasic("KA-Row (カ)", "ク", "ku", "কু", "Katakana 'ku'.", 2,
            "১. বামে ছোট তীর্যক রেখা; ২. অনুভূমিক রেখা টেনে নিচে বামে দীর্ঘ বাঁক।",
            "1. Short left diagonal; 2. Horizontal top turning into a long curve sweeping down-left.",
            "クラス", "kurasu", "শ্রেণি / ক্লাস", "Class"),
        kBasic("KA-Row (カ)", "ケ", "ke", "কে", "Katakana 'ke'.", 3,
            "১. বামে তীর্যক রেখা; ২. অনুভূমিক রেখা; ৩. মাঝখান থেকে নিচে বামে বাঁকা রেখা।",
            "1. Short left diagonal; 2. Horizontal bar meeting stroke 1; 3. Downward curve from middle of bar.",
            "ケーキ", "keeki", "কেক", "Cake"),
        kBasic("KA-Row (カ)", "コ", "ko", "কো", "Katakana 'ko'.", 2,
            "১. উপরে অনুভূমিক রেখা টেনে ডানে কোণ করে নিচে নামুন; ২. বাম থেকে ডানে নিচের অনুভূমিক রেখা।",
            "1. Top horizontal turning sharply down on the right; 2. Bottom horizontal closing the right-side bracket.",
            "コーヒー", "koohii", "কফি", "Coffee"),

        // SA-row (サ行)
        kBasic("SA-Row (サ)", "サ", "sa", "সা", "Katakana 'sa'.", 3,
            "১. অনুভূমিক রেখা; ২. বামে ছোট উল্লম্ব রেখা; ৩. ডানে লম্বা উল্লম্ব রেখা যা নিচে বামে বাঁকে।",
            "1. Horizontal crossbar; 2. Short left vertical; 3. Longer right vertical curving down-left.",
            "サラダ", "sarada", "সালাদ", "Salad"),
        kBasic("SA-Row (サ)", "シ", "shi", "শি", "Katakana 'shi' (two dots aligned vertically on left, long stroke sweeps UP from bottom-left).", 3,
            "১. বামে উপরের ছোট দাগ; ২. নিচে দ্বিতীয় ছোট দাগ; ৩. নিচ থেকে ডানে উপরে ওঠা বাঁকা রেখা।",
            "1. Top-left short dash; 2. Lower-left short dash; 3. Long curve sweeping UPWARDS from bottom-left to top-right.",
            "シャツ", "shatsu", "শার্ট", "Shirt"),
        kBasic("SA-Row (サ)", "ス", "su", "সু", "Katakana 'su'.", 2,
            "১. অনুভূমিক রেখা থেকে কোণাকুণি বামে নিচে নামুন; ২. মাঝখান থেকে ডানে নিচে তীর্যক পা।",
            "1. Horizontal top turning diagonally down-left; 2. Short diagonal leg kicking down-right from center.",
            "スマホ", "sumaho", "স্মার্টফোন", "Smartphone"),
        kBasic("SA-Row (サ)", "セ", "se", "সে", "Katakana 'se'.", 2,
            "১. অনুভূমিক রেখা টেনে শেষে ছোট হুক; ২. উপর থেকে নিচে নেমে ডানে অনুভূমিক সমাপ্তি।",
            "1. Horizontal bar with sharp downward hook; 2. Vertical line bending flat to the right at base.",
            "セーター", "seetaa", "সোয়েটার", "Sweater"),
        kBasic("SA-Row (サ)", "ソ", "so", "সো", "Katakana 'so' (top dot slants down, long stroke sweeps DOWN from top-right).", 2,
            "১. বামে উপরে ছোট উল্লম্ব ফোঁটা; ২. ডান পাশে উপর থেকে বামে নিচে নামা বাঁকা রেখা।",
            "1. Short top-left downward tick; 2. Long stroke sweeping DOWNWARDS from top-right to bottom-left.",
            "ソース", "soosu", "সস", "Sauce"),

        // TA-row (タ行)
        kBasic("TA-Row (タ)", "タ", "ta", "তা", "Katakana 'ta'.", 3,
            "১. বামে তীর্যক রেখা; ২. অনুভূমিক রেখা থেকে বামে দীর্ঘ বাঁক; ৩. ভেতরে মাঝখানে ছোট দাগ।",
            "1. Short left diagonal; 2. Horizontal top curving down-left; 3. Short inner diagonal tick.",
            "タクシー", "takushii", "ট্যাক্সি", "Taxi"),
        kBasic("TA-Row (タ)", "チ", "chi", "চি", "Katakana 'chi'.", 3,
            "১. উপরে ডান থেকে বামে তীর্যক রেখা; ২. মাঝখানে অনুভূমিক রেখা; ৩. উপর থেকে নিচে বামে বাঁকা রেখা।",
            "1. Short top slanting stroke right-to-left; 2. Horizontal crossbar; 3. Central stem curving down-left.",
            "チーズ", "chiizu", "পনির / চিজ", "Cheese"),
        kBasic("TA-Row (タ)", "ツ", "tsu", "ৎসু", "Katakana 'tsu' (two dots aligned horizontally at top, long stroke sweeps DOWN).", 3,
            "১. বামে উপরে ছোট দাগ; ২. মাঝখানে উপরে দ্বিতীয় দাগ; ৩. ডান পাশে উপর থেকে নিচে বামে বাঁকা রেখা।",
            "1. Left top vertical tick; 2. Middle top vertical tick; 3. Long stroke sweeping DOWN from top-right to bottom-left.",
            "ツアー", "tsuaa", "ভ্রমণ / ট্যুর", "Tour"),
        kBasic("TA-Row (タ)", "テ", "te", "তে", "Katakana 'te'.", 3,
            "১. উপরের ছোট অনুভূমিক রেখা; ২. নিচের লম্বা অনুভূমিক রেখা; ৩. মাঝখান থেকে নিচে বামে বাঁকা রেখা।",
            "1. Short top horizontal; 2. Longer second horizontal; 3. Central stem curving down-left.",
            "テレビ", "terebi", "টেলিভিশন", "Television"),
        kBasic("TA-Row (タ)", "ト", "to", "তো", "Katakana 'to'.", 2,
            "১. সোজা উল্লম্ব রেখা; ২. মাঝখান থেকে ডানে নিচে ছোট তীর্যক রেখা।",
            "1. Straight vertical stem; 2. Short diagonal dash angling down-right from center.",
            "トマト", "tomato", "টমেটো", "Tomato"),

        // NA-row (ナ行)
        kBasic("NA-Row (ナ)", "ナ", "na", "না", "Katakana 'na'.", 2,
            "১. অনুভূমিক রেখা; ২. উপর থেকে নিচে বামে বাঁকা উল্লম্ব রেখা।",
            "1. Horizontal bar; 2. Vertical stroke cutting through and curving left at bottom.",
            "ナイフ", "naifu", "ছুরি", "Knife"),
        kBasic("NA-Row (ナ)", "ニ", "ni", "নি", "Katakana 'ni'.", 2,
            "১. উপরের ছোট অনুভূমিক রেখা; ২. নিচের তুলনামূলক লম্বা অনুভূমিক রেখা।",
            "1. Shorter top horizontal bar; 2. Longer bottom horizontal bar.",
            "ニュース", "nyuusu", "সংবাদ / খবর", "News"),
        kBasic("NA-Row (ナ)", "ヌ", "nu", "নু", "Katakana 'nu'.", 2,
            "১. অনুভূমিক রেখা থেকে কোণাকুণি বামে নিচে বাঁক; ২. মাঝখান দিয়ে ডানে নিচে ছেদকারী রেখা।",
            "1. Horizontal top turning down-left; 2. Diagonal stroke crossing through the center.",
            "ヌードル", "nuudoru", "নুডলস", "Noodles"),
        kBasic("NA-Row (ナ)", "ネ", "ne", "নে", "Katakana 'ne'.", 4,
            "১. উপরে ছোট দাগ; ২. অনুভূমিক রেখা থেকে বামে নিচে বাঁক; ৩. মাঝখান থেকে সোজা নিচে রেখা; ৪. ডানে ছোট দাগ।",
            "1. Short top tick; 2. Horizontal turning down-left; 3. Vertical stem down center; 4. Short right side dash.",
            "ネクタイ", "nekutai", "টাই", "Necktie"),
        kBasic("NA-Row (ナ)", "ノ", "no", "নো", "Katakana 'no'.", 1,
            "১. এক টানে ডান পাশের উপর থেকে বামে নিচে বাঁকা রেখা।",
            "1. Single diagonal sweep from top-right to bottom-left.",
            "ノート", "nooto", "নোটবুক / খাতা", "Notebook"),

        // HA-row (ハ行)
        kBasic("HA-Row (ハ)", "ハ", "ha", "হা", "Katakana 'ha'.", 2,
            "১. বামে উপর থেকে নিচে তীর্যক রেখা; ২. ডানে উপর থেকে নিচে তীর্যক রেখা।",
            "1. Left downward-left slant; 2. Right downward-right slant.",
            "ハンカチ", "hankachi", "রুমাল", "Handkerchief"),
        kBasic("HA-Row (ハ)", "ヒ", "hi", "হি", "Katakana 'hi'.", 2,
            "১. বাম পাশে মাঝখান থেকে ডানে অনুভূমিক রেখা; ২. উপর থেকে নিচে নেমে ডানে অনুভূমিক বাঁক।",
            "1. Short horizontal bar left-to-right; 2. Left vertical stem curving flat right at bottom.",
            "ヒント", "hinto", "ইঙ্গিত / সূত্র", "Hint"),
        kBasic("HA-Row (ハ)", "フ", "fu", "ফু", "Katakana 'fu'.", 1,
            "১. এক টানে বাম থেকে ডানে অনুভূমিক রেখা টেনে নিচে বামে বাঁক।",
            "1. Single stroke: horizontal top turning into a smooth curve down-left.",
            "フォーク", "fooku", "কাঁটাচামচ", "Fork"),
        kBasic("HA-Row (ハ)", "ヘ", "he", "হে", "Katakana 'he'.", 1,
            "১. এক টানে বাম থেকে উপরে উঠে ডানে নিচে ঢালু রেখা।",
            "1. Single mountain-peak stroke identical in shape to Hiragana 'he'.",
            "ヘルメット", "herumetto", "হেলমেট", "Helmet"),
        kBasic("HA-Row (ハ)", "ホ", "ho", "হো", "Katakana 'ho'.", 4,
            "১. অনুভূমিক রেখা; ২. মাঝখানে উল্লম্ব রেখা ও হুক; ৩. বামে নিচে তীর্যক দাগ; ৪. ডানে নিচে তীর্যক দাগ।",
            "1. Horizontal bar; 2. Center vertical stem with hook; 3. Left diagonal dot; 4. Right diagonal dot.",
            "ホテル", "hoteru", "হোটেল", "Hotel"),

        // MA-row (マ行)
        kBasic("MA-Row (マ)", "マ", "ma", "মা", "Katakana 'ma'.", 2,
            "১. অনুভূমিক রেখা থেকে কোণাকুণি বামে নিচে নামুন; ২. নিচে ছোট তীর্যক দাগ।",
            "1. Horizontal top turning diagonally down-left; 2. Short diagonal dash catching the tip.",
            "マスク", "masuku", "মাস্ক", "Mask"),
        kBasic("MA-Row (マ)", "ミ", "mi", "মি", "Katakana 'mi'.", 3,
            "১. উপরের তীর্যক রেখা; ২. মাঝখানের সমান্তরাল রেখা; ৩. নিচের সমান্তরাল রেখা।",
            "1. Top slanted horizontal; 2. Middle slanted horizontal; 3. Bottom slanted horizontal.",
            "ミルク", "miruku", "দুধ", "Milk"),
        kBasic("MA-Row (マ)", "ム", "mu", "মু", "Katakana 'mu'.", 2,
            "১. উপর থেকে বামে নিচে গিয়ে ডানে অনুভূমিক রেখা; ২. ডানে নিচে ছোট তীর্যক দাগ।",
            "1. Diagonal down-left turning sharply flat right; 2. Short downward tick closing the right corner.",
            "ゲーム", "geemu", "খেলা / গেম", "Game"),
        kBasic("MA-Row (マ)", "メ", "me", "মে", "Katakana 'me'.", 2,
            "১. ডান থেকে বামে নিচে দীর্ঘ বাঁকা রেখা; ২. মাঝখানে ছোট ছেদকারী তীর্যক রেখা।",
            "1. Long diagonal curve from top-right to bottom-left; 2. Short cross-dash through center.",
            "メモ", "memo", "সংক্ষিপ্ত নোট / মেমো", "Memo"),
        kBasic("MA-Row (マ)", "モ", "mo", "মো", "Katakana 'mo'.", 3,
            "১. উপরের অনুভূমিক রেখা; ২. নিচের অনুভূমিক রেখা; ৩. উপর থেকে নিচে নেমে ডানে সমান বাঁক।",
            "1. Top horizontal; 2. Second wider horizontal; 3. Vertical stem starting at top bar and bending flat right.",
            "モデル", "moderu", "মডেল", "Model"),

        // YA-row (ヤ行)
        kBasic("YA-Row (ヤ)", "ヤ", "ya", "ইয়া", "Katakana 'ya'.", 2,
            "১. অনুভূমিক রেখা টেনে কোণাকুণি ভেতরে ছোট হুক; ২. উপর থেকে নিচে লম্বা তীর্যক রেখা।",
            "1. Slanted horizontal bar with sharp hook; 2. Long diagonal stem cutting down.",
            "タイヤ", "taiya", "টায়ার / চাকা", "Tire"),
        kBasic("YA-Row (ヤ)", "ユ", "yu", "ইউ", "Katakana 'yu'.", 2,
            "১. অনুভূমিক রেখা টেনে ডানে কোণ করে নিচে নামুন; ২. নিচে বাম থেকে ডানে লম্বা অনুভূমিক রেখা।",
            "1. Top horizontal turning sharply down; 2. Wider bottom horizontal base.",
            "ユニフォーム", "yunifoomu", "ইউনিফর্ম / পোশাক", "Uniform"),
        kBasic("YA-Row (ヤ)", "ヨ", "yo", "ইয়ো", "Katakana 'yo'.", 3,
            "১. উপরে অনুভূমিক রেখা টেনে ডানে নিচে নামুন; ২. মাঝখানের অনুভূমিক রেখা; ৩. নিচের অনুভূমিক রেখা।",
            "1. Top horizontal turning down on right; 2. Middle horizontal crossbar; 3. Bottom horizontal crossbar.",
            "ヨット", "yotto", "পালতোলা নৌকা / ইয়ট", "Yacht"),

        // RA-row (ラ行)
        kBasic("RA-Row (ラ)", "ラ", "ra", "রা", "Katakana 'ra'.", 2,
            "১. উপরের ছোট অনুভূমিক রেখা; ২. নিচের অনুভূমিক রেখা থেকে বামে নিচে বাঁক।",
            "1. Short top horizontal; 2. Second horizontal turning smoothly down-left.",
            "ラジオ", "rajio", "রেডিও", "Radio"),
        kBasic("RA-Row (ラ)", "リ", "ri", "রি", "Katakana 'ri'.", 2,
            "১. বামে ছোট সোজা উল্লম্ব রেখা; ২. ডানে লম্বা বাঁকা উল্লম্ব রেখা।",
            "1. Short straight left vertical; 2. Longer right vertical curving down-left.",
            "リボン", "ribon", "ফিতা / রিবন", "Ribbon"),
        kBasic("RA-Row (ラ)", "ル", "ru", "রু", "Katakana 'ru'.", 2,
            "১. বামে নিচে বাঁকা উল্লম্ব রেখা; ২. ডানে সোজা নিচে নেমে উপরে ডানে হুক।",
            "1. Left vertical curving down-left; 2. Right vertical bending at base and flicking up-right.",
            "ルール", "ruuru", "নিয়ম / আইন", "Rule"),
        kBasic("RA-Row (ラ)", "レ", "re", "রে", "Katakana 're'.", 1,
            "১. এক টানে সোজা নিচে নেমে কোণ করে ডানে উপরে উঠুন।",
            "1. Single stroke: straight vertical stem turning sharply up-right at bottom.",
            "レモン", "remon", "লেবু", "Lemon"),
        kBasic("RA-Row (ラ)", "ロ", "ro", "রো", "Katakana 'ro'.", 3,
            "১. বাম পাশের উল্লম্ব রেখা; ২. উপরে অনুভূমিক রেখা টেনে ডানে নিচে নামুন; ৩. নিচের অনুভূমিক রেখা।",
            "1. Left vertical; 2. Top horizontal turning down on right; 3. Bottom horizontal closing the box.",
            "ロボット", "robotto", "রোবট", "Robot"),

        // WA-row & N (ワ行・ン)
        kBasic("WA-Row (ワ)", "ワ", "wa", "ওয়া", "Katakana 'wa'.", 2,
            "১. বামে ছোট উল্লম্ব রেখা; ২. অনুভূমিক ছাদ থেকে ডানে নিচে বামে বাঁক।",
            "1. Short left vertical; 2. Horizontal top turning down and sweeping left.",
            "ワイン", "wain", "ওয়াইন", "Wine"),
        kBasic("WA-Row (ワ)", "ヲ", "wo", "ও", "Katakana 'wo'.", 3,
            "১. উপরের অনুভূমিক রেখা; ২. মাঝখানের ছোট অনুভূমিক রেখা; ৩. ডান থেকে নিচে বামে বাঁক (অথবা ১+২ একত্রে)।",
            "1. Top horizontal; 2. Middle horizontal; 3. Right downward curve sweeping left.",
            "ヲタク", "wotaku", "ওটাকু (বিশেষ ভক্ত)", "Enthusiast / Otaku"),
        kBasic("WA-Row (ワ)", "ン", "n", "ন / ং / ম", "Katakana 'n' (top dot slants horizontally, long stroke sweeps UP from bottom-left).", 2,
            "১. বামে উপরে ছোট দাগ; ২. নিচ থেকে ডানে উপরে ওঠা দীর্ঘ বাঁকা রেখা।",
            "1. Short top-left dash; 2. Long curve sweeping UPWARDS from bottom-left to top-right.",
            "パン", "pan", "রুটি / পাউরুটি", "Bread")
    )

    private fun buildKatakanaDakuten(): List<KanaItem> {
        data class Entry(val row: String, val ch: String, val rom: String, val bn: String, val word: String, val wRom: String, val mBn: String, val mEn: String)
        val entries = listOf(
            Entry("GA-Row (ガ)", "ガ", "ga", "গা", "ガラス", "garasu", "কাঁচ / গ্লাস", "Glass"),
            Entry("GA-Row (ガ)", "ギ", "gi", "গি", "ギター", "gitaa", "গিটার", "Guitar"),
            Entry("GA-Row (ガ)", "グ", "gu", "গু", "グループ", "guruupu", "দল / গ্রুপ", "Group"),
            Entry("GA-Row (ガ)", "ゲ", "ge", "গে", "ゲーム", "geemu", "গেম / খেলা", "Game"),
            Entry("GA-Row (ガ)", "ゴ", "go", "গো", "ゴム", "gomu", "রাবার", "Rubber"),

            Entry("ZA-Row (ザ)", "ザ", "za", "যা", "ピザ", "piza", "পিৎজা", "Pizza"),
            Entry("ZA-Row (ザ)", "ジ", "ji", "জি", "ジュース", "juusu", "ফলের রস / জুস", "Juice"),
            Entry("ZA-Row (ザ)", "ズ", "zu", "যু", "ズボン", "zubon", "প্যান্ট / ট্রাউজার", "Trousers"),
            Entry("ZA-Row (ザ)", "ゼ", "ze", "যে", "ゼロ", "zero", "শূন্য (০)", "Zero"),
            Entry("ZA-Row (ザ)", "ゾ", "zo", "যো", "ゾーン", "zoon", "এলাকা / জোন", "Zone"),

            Entry("DA-Row (ダ)", "ダ", "da", "দা", "ダンス", "dansu", "নাচ / ড্যান্স", "Dance"),
            Entry("DA-Row (ダ)", "ヂ", "ji (di)", "জি", "ヂャイアント", "jaianto", "বিশাল", "Giant"),
            Entry("DA-Row (ダ)", "ヅ", "zu (du)", "যু", "ツヅキ", "tsuzuki", "ধারাবাহিক", "Continuation"),
            Entry("DA-Row (ダ)", "デ", "de", "দে", "デザイン", "dezain", "নকশা / ডিজাইন", "Design"),
            Entry("DA-Row (ダ)", "ド", "do", "দো", "ドア", "doa", "দরজা", "Door"),

            Entry("BA-Row (バ)", "バ", "ba", "বা", "バス", "basu", "বাস", "Bus"),
            Entry("BA-Row (バ)", "ビ", "bi", "বি", "ビル", "biru", "ভবন / বিল্ডিং", "Building"),
            Entry("BA-Row (バ)", "ブ", "bu", "বু", "テーブル", "teeburu", "টেবিল", "Table"),
            Entry("BA-Row (バ)", "ベ", "be", "বে", "ベッド", "beddo", "বিছানা", "Bed"),
            Entry("BA-Row (バ)", "ボ", "bo", "বো", "ボタン", "botan", "বোতাম", "Button")
        )
        return entries.map { e ->
            KanaItem(
                id = "k_dak_${e.rom}",
                script = KanaScript.KATAKANA,
                category = KanaCategory.DAKUTEN,
                rowGroup = e.row,
                character = e.ch,
                romaji = e.rom,
                pronunciationBn = e.bn,
                explanationEn = "Voiced Katakana '${e.rom}' formed by adding dakuten ticks to the base character.",
                strokeCount = 0,
                strokeDirectionsBn = "প্রথমে মূল কাতাকানা বর্ণটি লিখুন, তারপর ডান পাশে উপরে দুটি ছোট দাগ (゛) যোগ করুন।",
                strokeDirectionsEn = "Write the base Katakana character first, then add two short dakuten strokes at top-right.",
                exampleWordJp = e.word,
                exampleWordReading = e.word,
                exampleWordRomaji = e.wRom,
                exampleMeaningBn = e.mBn,
                exampleMeaningEn = e.mEn
            )
        }
    }

    private fun buildKatakanaHandakuten(): List<KanaItem> {
        data class Entry(val ch: String, val rom: String, val bn: String, val strokes: Int, val word: String, val wRom: String, val mBn: String, val mEn: String)
        val entries = listOf(
            Entry("パ", "pa", "পা", 3, "パソコン", "pasokon", "পার্সোনাল কম্পিউটার", "Personal computer"),
            Entry("ピ", "pi", "পি", 3, "ピアノ", "piano", "পিয়ানো", "Piano"),
            Entry("プ", "pu", "পু", 2, "プール", "puuru", "সুইমিং পুল", "Swimming pool"),
            Entry("ペ", "pe", "পে", 2, "ペン", "pen", "কলম", "Pen"),
            Entry("ポ", "po", "পো", 5, "ポスト", "posuto", "ডাকবাক্স", "Postbox / Mailbox")
        )
        return entries.map { e ->
            KanaItem(
                id = "k_han_${e.rom}",
                script = KanaScript.KATAKANA,
                category = KanaCategory.HANDAKUTEN,
                rowGroup = "PA-Row (パ)",
                character = e.ch,
                romaji = e.rom,
                pronunciationBn = e.bn,
                explanationEn = "Katakana P-sound '${e.rom}' formed by adding the small handakuten circle to the HA-row.",
                strokeCount = e.strokes,
                strokeDirectionsBn = "প্রথমে মূল কাতাকানা বর্ণটি লিখুন, এরপর ডান পাশে উপরে ছোট বৃত্ত (゜) আঁকুন।",
                strokeDirectionsEn = "Write the base Katakana character first, then draw the small handakuten circle at top-right.",
                exampleWordJp = e.word,
                exampleWordReading = e.word,
                exampleWordRomaji = e.wRom,
                exampleMeaningBn = e.mBn,
                exampleMeaningEn = e.mEn
            )
        }
    }

    private fun buildKatakanaYoon(): List<KanaItem> {
        data class Entry(val group: String, val ch: String, val rom: String, val bn: String, val word: String, val wRom: String, val mBn: String, val mEn: String)
        val entries = listOf(
            Entry("KYA", "キャ", "kya", "কিয়া", "キャンプ", "kyanpu", "ক্যাম্পিং", "Camping"),
            Entry("KYA", "キュ", "kyu", "কিউ", "バーベキュー", "baabekyuu", "বারবিকিউ", "Barbecue"),
            Entry("KYA", "キョ", "kyo", "কিয়ো", "キョロキョロ", "kyorokyoro", "চারদিকে তাকানো", "Looking around"),

            Entry("SHA", "シャ", "sha", "শা", "シャワー", "shawaa", "শাওয়ার", "Shower"),
            Entry("SHA", "シュ", "shu", "শু", "シュート", "shuuto", "শুট (খেলায়)", "Shoot"),
            Entry("SHA", "ショ", "sho", "শো", "ショップ", "shoppu", "দোকান / শপ", "Shop"),

            Entry("CHA", "チャ", "cha", "চা", "チャンス", "chansu", "সুযোগ", "Chance"),
            Entry("CHA", "チュ", "chu", "চু", "チューリップ", "chuurippu", "টিউলিপ ফুল", "Tulip"),
            Entry("CHA", "チョ", "cho", "চো", "チョコレート", "chokoreeto", "চকলেট", "Chocolate"),

            Entry("NYA", "ニャ", "nya", "নিয়া", "ニャー", "nyaa", "বিড়ালের ডাক (মিউ)", "Meow"),
            Entry("NYA", "ニュ", "nyu", "নিউ", "メニュー", "menyuu", "খাবারের তালিকা / মেনু", "Menu"),
            Entry("NYA", "ニョ", "nyo", "নিয়ো", "ニョッキ", "nyokki", "নিওক্কি (ইতালীয় খাবার)", "Gnocchi"),

            Entry("HYA", "ヒャ", "hya", "হিয়া", "ヒヤシンス", "hiyashinsu", "হায়াসিন্থ ফুল", "Hyacinth"),
            Entry("HYA", "ヒュ", "hyu", "হিউ", "ヒューズ", "hyuuzu", "বৈদ্যুতিক ফিউজ", "Fuse"),
            Entry("HYA", "ヒョ", "hyo", "হিয়ো", "ヒョウ", "hyou", "চিতাবাঘ", "Leopard"),

            Entry("MYA", "ミャ", "mya", "মিয়া", "ミャンマー", "myanmaa", "মিয়ানমার", "Myanmar"),
            Entry("MYA", "ミュ", "myu", "মিউ", "ミュージック", "myuujikku", "সংগীত / মিউজিক", "Music"),
            Entry("MYA", "ミョ", "myo", "মিয়ো", "ミョウバン", "myouban", "ফিটকিরি", "Alum"),

            Entry("RYA", "リャ", "rya", "রিয়া", "リャマ", "ryama", "লামা (প্রাণী)", "Llama"),
            Entry("RYA", "リュ", "ryu", "রিউ", "リュック", "ryukku", "পিঠের ব্যাগ / ব্যাকপ্যাক", "Backpack"),
            Entry("RYA", "リョ", "ryo", "রিয়ো", "マトリョーシカ", "matoryooshika", "রাশিয়ান পুতুল", "Matryoshka"),

            Entry("GYA", "ギャ", "gya", "গিয়া", "ギャラリー", "gyararii", "গ্যালারি", "Gallery"),
            Entry("GYA", "ギュ", "gyu", "গিউ", "フィギュア", "figyua", "ফিগার / মূর্তি", "Figure"),
            Entry("GYA", "ギョ", "gyo", "গিয়ো", "ギョーザ", "gyooza", "গিয়োজা (ডাম্পলিং)", "Gyoza dumpling"),

            Entry("JA", "ジャ", "ja", "জা", "ジャケット", "jaketto", "জ্যাকেট", "Jacket"),
            Entry("JA", "ジュ", "ju", "জু", "ジュース", "juusu", "জুস / ফলের রস", "Juice"),
            Entry("JA", "ジョ", "jo", "জো", "ジョギング", "jogingu", "জগিং / দৌড়ানো", "Jogging"),

            Entry("BYA", "ビャ", "bya", "বিয়া", "ビャクダン", "byakudan", "চন্দন কাঠ", "Sandalwood"),
            Entry("BYA", "ビュ", "byu", "বিউ", "インタビュー", "intabyuu", "সাক্ষাৎকার", "Interview"),
            Entry("BYA", "ビョ", "byo", "বিয়ো", "ビョーク", "byooku", "বিওর্ক (নাম)", "Bjork"),

            Entry("PYA", "ピャ", "pya", "পিয়া", "ピャン", "pyan", "লাফানোর শব্দ", "Hop sound"),
            Entry("PYA", "ピュ", "pyu", "পিউ", "コンピュータ", "konpyuuta", "কম্পিউটার", "Computer"),
            Entry("PYA", "ピョ", "pyo", "পিয়ো", "ピョンヤン", "pyonyan", "পিয়ংইয়ং", "Pyongyang")
        )
        return entries.map { e ->
            KanaItem(
                id = "k_yoon_${e.rom}",
                script = KanaScript.KATAKANA,
                category = KanaCategory.YOON,
                rowGroup = "${e.group}-Group",
                character = e.ch,
                romaji = e.rom,
                pronunciationBn = e.bn,
                explanationEn = "Contracted Katakana combination (${e.rom}) pronounced in a single beat.",
                strokeCount = 0,
                strokeDirectionsBn = "প্রথমে পূর্ণ আকারের ই-কলামের কাতাকানা এবং ডান পাশে নিচে ছোট ャ/ュ/ョ লিখুন।",
                strokeDirectionsEn = "Write the full-size i-column Katakana character followed by a half-sized ャ, ュ, or ョ.",
                exampleWordJp = e.word,
                exampleWordReading = e.word,
                exampleWordRomaji = e.wRom,
                exampleMeaningBn = e.mBn,
                exampleMeaningEn = e.mEn
            )
        }
    }

    private fun buildKatakanaSpecialAndForeign(): List<KanaItem> {
        data class Entry(val group: String, val ch: String, val rom: String, val bn: String, val exp: String, val word: String, val wRom: String, val mBn: String, val mEn: String)
        val entries = listOf(
            Entry("Special Symbols", "ッ", "(double consonant)", "ছোট 'ッ' (পরবর্তী ব্যঞ্জন দ্বিত্ব করে)", "Katakana Sokuon: holds a 1-beat pause doubling the next consonant.", "コップ", "koppu", "গ্লাস / কাপ", "Cup / Tumbler"),
            Entry("Special Symbols", "ー", "(long vowel dash)", "দীর্ঘ স্বরচিহ্ন 'ー' (স্বরবর্ণ দীর্ঘ করে)", "Chōonpu dash: extends the preceding vowel sound by one full beat.", "ケーキ", "keeki", "কেক", "Cake"),

            // Foreign Word Combinations (Section 06 requirement)
            Entry("F-Sounds (ファ)", "ファ", "fa", "ফা", "Foreign 'fa' combination (フ + small ァ).", "ファミリー", "famirii", "পরিবার (ফ্যামিলি)", "Family"),
            Entry("F-Sounds (ファ)", "フィ", "fi", "ফি", "Foreign 'fi' combination (フ + small ィ).", "オフィス", "ofisu", "অফিস / কার্যালয়", "Office"),
            Entry("F-Sounds (ファ)", "フェ", "fe", "ফে", "Foreign 'fe' combination (フ + small ェ).", "カフェ", "kafe", "ক্যাফে", "Cafe"),
            Entry("F-Sounds (ファ)", "フォ", "fo", "ফো", "Foreign 'fo' combination (フ + small ォ).", "フォーク", "fooku", "কাঁটাচামচ", "Fork"),

            Entry("T/D-Sounds (ティ)", "ティ", "ti", "টি (ইংরেজি ti ধ্বনি)", "Foreign 'ti' sound (テ + small ィ) distinct from 'chi'.", "パーティー", "paatii", "পার্টি / উৎসব", "Party"),
            Entry("T/D-Sounds (ティ)", "ディ", "di", "ডি (ইংরেজি di ধ্বনি)", "Foreign 'di' sound (デ + small ィ).", "ディナー", "dinaa", "রাতের খাবার / ডিনার", "Dinner"),
            Entry("T/D-Sounds (ティ)", "トゥ", "tu", "টু (ইংরেজি tu ধ্বনি)", "Foreign 'tu' sound (ト + small ゥ).", "トゥデイ", "tudei", "টুডে / আজ", "Today (loanword)"),
            Entry("T/D-Sounds (ティ)", "ドゥ", "du", "ডু (ইংরেজি du ধ্বনি)", "Foreign 'du' sound (ド + small ゥ).", "ヒンドゥー", "hinduu", "হিন্দু", "Hindu"),

            Entry("W/V-Sounds (ウィ・ヴ)", "ウィ", "wi", "উই", "Foreign 'wi' sound (ウ + small ィ).", "ウィンドウ", "windou", "জানালা / উইন্ডো", "Window"),
            Entry("W/V-Sounds (ウィ・ヴ)", "ウェ", "we", "ওয়ে", "Foreign 'we' sound (ウ + small ェ).", "ウェブ", "webu", "ওয়েব / ইন্টারনেট", "Web"),
            Entry("W/V-Sounds (ウィ・ヴ)", "ウォ", "wo", "ওয়া/ও", "Foreign 'wo' sound (ウ + small ォ).", "ウォーター", "wootaa", "পানি (ওয়াটার)", "Water"),
            Entry("W/V-Sounds (ウィ・ヴ)", "ヴァ", "va", "ভা", "Foreign 'va' sound (ヴ + small ァ).", "ヴァイオリン", "vaiorin", "বেহালা / ভায়োলিন", "Violin"),

            Entry("SH/J/CH-E (シェ)", "シェ", "she", "শে", "Foreign 'she' sound (シ + small ェ).", "シェフ", "shefu", "শেফ / বাবুর্চি", "Chef"),
            Entry("SH/J/CH-E (シェ)", "ジェ", "je", "জে", "Foreign 'je' sound (ジ + small ェ).", "ジェット", "jetto", "জেট বিমান", "Jet"),
            Entry("SH/J/CH-E (シェ)", "チェ", "che", "চে", "Foreign 'che' sound (チ + small ェ).", "チェック", "chekku", "যাচাই / চেক", "Check")
        )
        return entries.mapIndexed { idx, e ->
            KanaItem(
                id = "k_ext_$idx",
                script = KanaScript.KATAKANA,
                category = KanaCategory.SPECIAL_AND_EXTENDED,
                rowGroup = e.group,
                character = e.ch,
                romaji = e.rom,
                pronunciationBn = e.bn,
                explanationEn = e.exp,
                strokeCount = if (e.ch == "ッ") 3 else if (e.ch == "ー") 1 else 0,
                strokeDirectionsBn = if (e.ch == "ー") "১. বাম থেকে ডানে সোজা অনুভূমিক রেখা টানুন।" else "প্রথমে মূল বর্ণ এবং ডান পাশে নিচে ছোট স্বরবর্ণটি লিখুন।",
                strokeDirectionsEn = if (e.ch == "ー") "1. Draw a single horizontal line from left to right." else "Write the base character followed by the half-sized vowel.",
                exampleWordJp = e.word,
                exampleWordReading = e.word,
                exampleWordRomaji = e.wRom,
                exampleMeaningBn = e.mBn,
                exampleMeaningEn = e.mEn
            )
        }
    }
}
