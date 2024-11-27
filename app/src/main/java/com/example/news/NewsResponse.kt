package com.example.news

data class NewsResponse(
    val nextPage: Any,
    val results: List<Article>,
    val status: String,
    val totalResults: Int
) {
    data class Article(
        val category: List<String>,
        val country: List<String>,
        val description: String,
        val icon: String,
        val id: String,
        val language: List<String>,
        val lastFetch: String,
        val name: String,
        val priority: Int,
        val url: String
    )
}