package com.kartikcd.mediumx.ui.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kartikcd.api.models.db.DBArticle
import com.kartikcd.mediumx.R
import com.kartikcd.mediumx.data.local.ArticleDAO
import com.kartikcd.mediumx.data.local.MediumXLocalClient
import com.kartikcd.mediumx.databinding.FragmentGlobalFeedBinding
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.util.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class   GlobalFeedFragment : Fragment() {

    private var _binding: FragmentGlobalFeedBinding? = null
    private lateinit var viewModel: FeedViewModel
    private lateinit var feedListAdapter: FeedListAdapter
    private lateinit var articleDAO: ArticleDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGlobalFeedBinding.inflate(layoutInflater, container, false)
        val mediumXRepository = MediumXRepository()
        val factory = FeedViewModelFactory(requireActivity().application, mediumXRepository, PagingRepository(mediumXRepository))

        viewModel = ViewModelProvider(this, factory).get(FeedViewModel::class.java)
        feedListAdapter = FeedListAdapter()

        articleDAO = MediumXLocalClient().getDAO(requireActivity().application)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        fetchFeedWithPaging()

        feedListAdapter.setOnArticleClickListener {
            println("Debug: ${it}")
            val dbArticle = DBArticle(
                null,
                it.slug,
                it.body,
                it.createdAt,
                it.description,
                it.favorited,
                it.favoritesCount,
                it.title,
                it.updatedAt,
                it.author.username,
                it.author.image)

            viewModel.saveArticle(dbArticle, articleDAO)
            Toast.makeText(activity, "Article saved successfully.", Toast.LENGTH_LONG).show()
        }

        _binding?.pullToRefresh?.setOnRefreshListener {
            fetchFeedWithPaging()
        }
    }

    private fun fetchFeedWithPaging() {
        lifecycleScope.launch {
            viewModel.getArticleList().collect {
                it.let {
                    feedListAdapter.submitData(it)
                    _binding?.pullToRefresh?.isRefreshing = false
                }
            }
        }
    }

    private fun fetchGlobalFeed() {
        viewModel.fetchGlobalFeed()
        viewModel.globalFeed.observe({ lifecycle }) { response ->
            when(response) {
                is Resource.Success -> {
                    _binding?.loadingImageView?.visibility = View.GONE
                    _binding?.globalFeedRecyclerView?.visibility = View.VISIBLE
                    response.data.let {

                    }
                }
                is Resource.Error -> {
                    _binding?.loadingImageView?.visibility = View.GONE
                    _binding?.globalFeedRecyclerView?.visibility = View.GONE
                    response.message.let {
                        Toast.makeText(activity, "An error occured: $it", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    _binding?.loadingImageView?.visibility = View.VISIBLE
                    _binding?.globalFeedRecyclerView?.visibility = View.GONE
                }
            }
            _binding?.pullToRefresh?.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        _binding?.globalFeedRecyclerView?.apply {
            adapter = feedListAdapter
            layoutManager = LinearLayoutManager(activity)
            feedListAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}