package com.example.news.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.ui.NewsActivity
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.ui.NewsViewModel
import com.example.util.Constants.Companion.QUERY_PAGE_SIZE
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
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) {response ->
            Log.d(TAG, "onViewCreated: $response")
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        Log.d(TAG, "Fetch list success: ${newsResponse.articles.toList()}")
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        isLastPage = newsResponse.nextPage.isNullOrEmpty()
                        if(isLastPage){
                            view.findViewById<RecyclerView>(R.id.rvBreakingNews).setPadding(0,0,0,0)
                        }

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(context, "An error occured: $message", Toast.LENGTH_LONG).show()
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
            isLoading = false
        }
    }
    private fun showProgressBar() {
        view?.apply {
           findViewById<ProgressBar>(R.id.paginationProgressBar).visibility = View.VISIBLE
            isLoading = true
        }

    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(
            recyclerView: RecyclerView,
            dx: Int,
            dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPos + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPos >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage &&
                    isAtLastItem &&
                    isNotAtBeginning &&
                    isTotalMoreThanVisible &&
                    isScrolling

            Log.d(TAG,"firstVisibleItemPos $firstVisibleItemPos")
            Log.d(TAG,"visibleItemCount $visibleItemCount")
            Log.d(TAG,"totalItemCount $totalItemCount")
            Log.d(TAG,"isNotLoadingAndNotLastPage $isNotLoadingAndNotLastPage")
            Log.d(TAG,"isAtLastItem $isAtLastItem")
            Log.d(TAG,"isNotAtBeginning $isNotAtBeginning")
            Log.d(TAG,"isTotalMoreThanVisible $isTotalMoreThanVisible")
            Log.d(TAG,"shouldPaginate $shouldPaginate")

            if(shouldPaginate){
                viewModel.getBreakingNews("vi")
                isScrolling = false
            }

        }
    }

    private fun setupRecycleView(){
        newsAdapter = NewsAdapter()
        view?.apply {
            findViewById<RecyclerView>(R.id.rvBreakingNews).apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@BreakingNewsFragment.scrollListener)
            }
        }
    }
}