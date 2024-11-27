package com.example.news.api

import com.example.news.NewsResponse
import com.example.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("api/1/latest")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "vi",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("api/1/archive")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("country")
        countryCode: String = "vi",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

}