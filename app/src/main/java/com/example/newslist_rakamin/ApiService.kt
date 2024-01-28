package com.example.newslist_rakamin

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("sources") sources: String = "techcrunch",
        @Query("apiKey") apiKey: String = "0d0f17778da44371b7baf714eaf27ef2"
    ): Call<NewsResponse>

    @GET("v2/everything")
    fun getAllArticles(
        @Query("q") query: String = "tesla",
        @Query("from") from: String = "2024-01-20",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = "0d0f17778da44371b7baf714eaf27ef2"
    ): Call<NewsResponse>
}