package com.example.news

import androidx.room.Entity
import androidx.room.PrimaryKey

data class NewsResponse(
    val nextPage: Any,
    val results: List<Article>,
    val status: String,
    val totalResults: Int
)

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val category: List<String>,
    val country: List<String>,
    val description: String,
    val icon: String,
    val language: List<String>,
    val lastFetch: String,
    val name: String,
    val priority: Int,
    val url: String
)