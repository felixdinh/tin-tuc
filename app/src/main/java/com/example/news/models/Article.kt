package com.example.news.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "article_id")
    val articleId: String,
    val category: List<String>,
    val content: String,
    val country: List<String>,
    val creator: String?,
    val description: String,
    val duplicate: Boolean,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    val keywords: List<String>?,
    val language: String?,
    val link: String,
    val pubDate: String,
    val pubDateTZ: String,
    val sentiment: String,
    @ColumnInfo(name = "source_icon")
    val sourceIcon: String,
    @ColumnInfo(name = "source_name")
    val sourceName: String,
    @ColumnInfo(name = "source_url")
    val sourceUrl: String,
    val title: String,
    @ColumnInfo(name = "video_url")
    val videoUrl: String?
)