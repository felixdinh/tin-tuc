package com.example.news.repositories

import com.example.news.api.RetrofitInstance
import com.example.news.db.ArticleDatabase
import com.example.news.models.Article
import com.example.util.Constants.Companion.QUERY_PAGE_SIZE

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, page: String?, pageSize: Int? = QUERY_PAGE_SIZE) =
        RetrofitInstance.api.getBreakingNews(countryCode, page, pageSize = pageSize)

    suspend fun searchNews(searchQuery: String, page: String?) =
        RetrofitInstance.api.searchForNews(searchQuery, page)

    suspend fun upsert(article: Article) = db.getArticleDao().updateOrInsert(article)
    fun getSavedNews() = db.getArticleDao().getArticles()
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}