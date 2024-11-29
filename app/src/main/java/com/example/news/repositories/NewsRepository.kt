package com.example.news.repositories

import com.example.news.api.RetrofitInstance
import com.example.news.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, page: String?) =
        RetrofitInstance.api.getBreakingNews(countryCode, page)

}