package com.kartikcd.mediumx.ui.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kartikcd.mediumx.R
import com.kartikcd.mediumx.databinding.FragmentGlobalFeedBinding
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.util.Resource

class GlobalFeedFragment : Fragment() {

    private var _binding: FragmentGlobalFeedBinding? = null
    private lateinit var viewModel: FeedViewModel
    private lateinit var feedListAdapter: FeedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGlobalFeedBinding.inflate(layoutInflater, container, false)
        val factory = FeedViewModelFactory(requireActivity().application, MediumXRepository())

        viewModel = ViewModelProvider(this, factory).get(FeedViewModel::class.java)
        feedListAdapter = FeedListAdapter()
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        fetchGlobalFeed()

        feedListAdapter.setOnArticleClickListener {
            println("Debug: ${it}")
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
                        feedListAdapter.differ.submitList(it?.articles?.toList())
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
        }
    }

    private fun initRecyclerView() {
        _binding?.globalFeedRecyclerView?.apply {
            adapter = feedListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}