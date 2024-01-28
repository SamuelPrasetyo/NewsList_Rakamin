package com.example.newslist_rakamin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newslist_rakamin.databinding.NewslistBinding
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class NewsAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewslistBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    class NewsViewHolder(val binding: NewslistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            // Load and Display Image using Picasso
            article.imageUrl?.let{
                Picasso.get().load(it).into(binding.imgNews)
            }

            binding.titleNews.text = article.title
            // Display the name from the Source class
            binding.author.text = article.source.name

            // Format the publishedAt date
            try {
                val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormat = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale("id"))

                val dateTime = LocalDateTime.parse(article.publishedAt, inputFormat)
                val formattedDate = dateTime.format(outputFormat)

                binding.publishDate.text = formattedDate
            } catch (e: Exception) {
                e.printStackTrace()
                binding.publishDate.text = article.publishedAt
            }
        }
    }
}