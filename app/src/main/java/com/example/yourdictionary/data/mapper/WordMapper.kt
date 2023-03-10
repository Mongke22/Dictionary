package com.example.yourdictionary.data.mapper

import com.example.yourdictionary.data.database.RussianWordInfoDbModel
import com.example.yourdictionary.data.database.WordInfoDbModel
import com.example.yourdictionary.data.network.Model.WordInfoListDto
import com.example.yourdictionary.domain.model.WordInfo

class WordMapper {
    fun mapDtoToDbModel(dto: WordInfoListDto, usedLanguages: String): WordInfoDbModel {
        val currentTime = System.currentTimeMillis()
        val text = diacriticToNormal(dto.wordInfoDto?.get(FIRST_ENTRY)?.originalText?.lowercase() ?: "ErrorText") + "-$usedLanguages"
        val originalLanguage = englishTermsToRussian(usedLanguages.split("-")[FIRST_ENTRY])
        var partOfSpeech = ""
        var transcription = ""
        var gender = ""
        var russianTranslation = ""
        dto.wordInfoDto?.let { definitions ->
            for (definition in definitions) {
                partOfSpeech += englishTermsToRussian(definition.partOfSpeech) + "/"
                transcription += (definition.transcription ?: "") + "/"
                definition.translations?.let { translations ->
                    for (translation in translations) {
                        russianTranslation += (translation.translation ?: "") + "&"
                        gender += (translation.gender ?: "") + "&"
                    }
                }
                russianTranslation += "/"
                gender += "/"
            }
        }
        return WordInfoDbModel(
            text,
            currentTime,
            originalLanguage,
            partOfSpeech,
            transcription,
            gender,
            russianTranslation
        )
    }

    fun mapDtoToRussianDbModel(dto: WordInfoListDto, time: Long): RussianWordInfoDbModel{
        return TODO()
    }

    fun mapDbModelToEntity(dbModel: WordInfoDbModel) = WordInfo(
        timeRequest = dbModel.timeRequest,
        text = dbModel.textPlusLanguage,
        originalLanguage = dbModel.originalLanguage,
        partOfSpeech = dbModel.partOfSpeech,
        transcription = dbModel.transcription,
        gender = dbModel.gender,
        translations = dbModel.russianTranslation
    )

    fun mapRussianDbModelToEntity(dbModel: RussianWordInfoDbModel) = WordInfo(
        timeRequest = dbModel.timeRequest,
        text = dbModel.textOnRussian,
        originalLanguage = "??????????????",
        partOfSpeech = dbModel.partOfSpeech,
        transcription = "${dbModel.textOnRussian}/${dbModel.meanings}",
        gender = "",
        translations = "${dbModel.textOnEnglish}/${dbModel.textOnFrench}/${dbModel.textOnGermany}"
    )

    fun englishTermsToRussian(str: String?): String{
        if(str == null) return ""
        return when(str){
            "noun" -> "??????."
            "verb" -> "????."
            "adjective" -> "????????."
            "adverb" -> "??????."
            "pronoun" -> "????????."
            "preposition" -> "??????????????"
            "conjunction" -> "????????"
            "interjection" -> "????????."
            "numeral" -> "????????."
            "article" -> "??????????????"
            "determiner" -> "??????????."
            "particle" -> "????????."
            "foreign word" -> "??????????."
            "participle" -> "????????."
            "ru" -> "??????????????"
            "en" -> "????????????????????"
            "fr" -> "??????????????????????"
            "de" -> "????????????????"
            else -> "????????????"
        }
    }

    fun diacriticToNormal(s: String): String{
        var str = s
        str = str.replace("[??????????????]".toRegex(),"a")
        str = str.replace("[????????????]".toRegex(),"o")
        str = str.replace("[????????]".toRegex(),"e")
        str = str.replace("[????????]".toRegex(),"u")
        str = str.replace("[??]".toRegex(),"ss")
        return str
    }


    companion object {

        private const val FIRST_ENTRY = 0
    }
}

