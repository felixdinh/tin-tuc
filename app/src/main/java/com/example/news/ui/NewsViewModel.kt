package com.example.news.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.news.models.NewsResponse
import com.example.news.repositories.NewsRepository
import com.example.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel (
    val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var prePage:String? = null
    var nextPage:String? = null

    init {
        getBreakingNews("vi")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, nextPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Log.d("TAG", "handleBreakingNewsResponse: ${resultResponse}")
                prePage = nextPage
                nextPage = resultResponse.nextPage
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}


class NewsViewModelFactory(val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }

}