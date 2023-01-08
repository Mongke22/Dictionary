package com.example.yourdictionary.data.network

import com.example.yourdictionary.data.network.Model.WordInfoListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/v1/dicservice.json/lookup")
    suspend fun getTranslation(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_LANGUAGE) languages: String,
        @Query(QUERY_PARAM_TEXT) text: String
    ): WordInfoListDto

    companion object {
        private const val QUERY_PARAM_API_KEY = "key"
        private const val QUERY_PARAM_LANGUAGE = "lang"
        private const val QUERY_PARAM_TEXT = "text"


        private const val API_KEY = "dict.1.1.20221211T171619Z.918521352e817ab6.52825a50b71b24b8167db4815a5eaba2d7b3d28c"
    }
}