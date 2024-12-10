package com.example.news.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.news.NewsApplication
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.repositories.NewsRepository
import com.example.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel (
    app: Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsNextPage:String? = null
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNextPage:String? = null
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("vi")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
       safeBreakingNewsCall(countryCode)
    }

    fun searchNews(search: String) = viewModelScope.launch {
       safeSearchNewsCall(search)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsNextPage = resultResponse.nextPage
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)

                }

                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNextPage = resultResponse.nextPage
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)

                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()
    
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsNextPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
                return
            }
            breakingNews.postValue(Resource.Error("No internet connection"))
        } catch (t: Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion error"))
            }
        }

    }
    private suspend fun safeSearchNewsCall(search: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = newsRepository.searchNews(search, searchNextPage)
                searchNews.postValue(handleSearchNewsResponse(response))
                return
            }
            searchNews.postValue(Resource.Error("No internet connection"))
        } catch (t: Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network failure"))
                else -> searchNews.postValue(Resource.Error("Conversion error"))
            }
        }

    }

    private fun hasInternetConnection()  : Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activityNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false
        return  when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}


class NewsViewModelFactory(
    val app: Application,
    val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository) as T
    }

}