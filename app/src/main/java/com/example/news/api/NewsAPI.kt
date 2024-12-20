package com.example.news.api

import com.example.news.models.NewsResponse
import com.example.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("api/1/latest")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        page: String?,
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("size")
        pageSize: Int?
    ): Response<NewsResponse>

    @GET("api/1/latest")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: String?,
        @Query("country")
        countryCode: String? = "us",
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

}