package com.example.news.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.news.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getArticles() : LiveData<List<Article>>

    @Delete
    fun deleteArticle(article: Article)
}