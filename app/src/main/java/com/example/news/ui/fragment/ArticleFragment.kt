package com.example.news.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.news.R
import com.example.news.databinding.FragmentArticleBinding
import com.example.news.models.Article
import com.example.news.ui.NewsActivity
import com.example.news.ui.NewsViewModel

class ArticleFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    var _binding: FragmentArticleBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        val article = args.article
        binding.webview.apply {
            webViewClient = WebViewClient()
            loadUrl(article.link)
        }
    }
}