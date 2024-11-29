package com.example.news.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.ui.NewsActivity
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.ui.NewsViewModel
import com.example.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    val TAG = "BreakingNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    lateinit var newsAdapter: NewsAdapter
    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecycleView()
        viewModel.breakingNews.observe(viewLifecycleOwner) {response ->
            Log.d(TAG, "onViewCreated: $response")
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        Log.d(TAG, "Fetch list success: ${newsResponse.articles}")
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e( TAG,"An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }

            }
        }

    }

    private fun hideProgressBar() {
        view?.apply {
            findViewById<ProgressBar>(R.id.paginationProgressBar).visibility = View.INVISIBLE
        }
    }
    private fun showProgressBar() {
        view?.apply {
           findViewById<ProgressBar>(R.id.paginationProgressBar).visibility = View.VISIBLE
        }

    }

    private fun setupRecycleView(){
        newsAdapter = NewsAdapter()
        view?.apply {
            findViewById<RecyclerView>(R.id.rvBreakingNews).apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }
    }
}