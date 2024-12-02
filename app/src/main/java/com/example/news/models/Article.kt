package com.example.news.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerializedName("article_id")
    val articleId: String,
    val category: List<String>,
    val content: String,
    val country: List<String>,
    val creator: List<String>?,
    val description: String,
    val duplicate: Boolean,
    @SerializedName( "image_url")
    val imageUrl: String,
    val keywords: List<String>?,
    val language: String?,
    val link: String,
    val pubDate: String,
    val pubDateTZ: String,
    val sentiment: String,
    @SerializedName("source_icon")
    val sourceIcon: String,
    @SerializedName( "source_name")
    val sourceName: String,
    @SerializedName("source_url")
    val sourceUrl: String,
    val title: String,
    @SerializedName("video_url")
    val videoUrl: String?
) : Serializable