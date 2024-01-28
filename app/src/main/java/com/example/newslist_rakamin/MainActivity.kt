package com.example.newslist_rakamin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newslist_rakamin.ui.theme.NewsList_RakaminTheme
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NewsAdapter
    private lateinit var apiService: ApiService

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService: ApiService = ApiClient.getClient().create(ApiService::class.java)
        val callTopHeadlines: Call<NewsResponse> = apiService.getTopHeadlines()
        val callAllArticles: Call<NewsResponse> = apiService.getAllArticles()

        callTopHeadlines.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("HIT_NEWS", "Failed to fetch top headlines: ${t.message}", t)
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val topHeadlines = response.body()

                    if (topHeadlines?.articles ?.isNotEmpty() == true) {
                        val topHeadline = topHeadlines.articles[0] // Access the first article in the list

                        // Load and Display Image using Picasso
                        topHeadline.imageUrl?.let {
                            Picasso.get().load(it).into(binding.imgHeadlines)
                        }

                        binding.titleHeadlines.text = topHeadline.title
                        binding.authorHeadlines.text = topHeadline.source.name

                        try {
                            val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                            val outputFormat = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale("id"))

                            val dateTime = LocalDateTime.parse(topHeadline.publishedAt, inputFormat)
                            val formattedDate = dateTime.format(outputFormat)

                            binding.publishDateHeadlines.text = formattedDate
                        } catch (e: Exception) {
                            e.printStackTrace()
                            binding.publishDateHeadlines.text = topHeadline.publishedAt
                        }
                    } else {
                        Log.e("HIT_NEWS", "Top headlines response is empty.")
                    }
                } else {
                    Log.e("HIT_NEWS", "Top headlines response failed: ${response.code()}")
                }
            }
        })

        callAllArticles.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("HIT_NEWS", "Failed to fetch data: ${t.message}", t)
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    val newsModel = newsResponse?.articles ?: emptyList()

                    val adapter = NewsAdapter(newsModel)
                    binding.rvNewslist.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rvNewslist.adapter = adapter
                } else {
                    Log.e("HIT_NEWS", "Response failed: ${response.code()}")
                }
            }
        })

    }
}