package com.kartikcd.mediumx.ui.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kartikcd.mediumx.R
import com.kartikcd.mediumx.data.local.ArticleDAO
import com.kartikcd.mediumx.data.local.MediumXLocalClient
import com.kartikcd.mediumx.databinding.FragmentLocalFeedBinding
import com.kartikcd.mediumx.domain.MediumXRepository

class   LocalFeedFragment : Fragment() {

    private var _binding: FragmentLocalFeedBinding? = null
    private lateinit var viewModel: FeedViewModel
    private lateinit var localFeedListAdapter: LocalFeedListAdapter
    private lateinit var articleDAO: ArticleDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentLocalFeedBinding.inflate(layoutInflater, container, false)
        val factory = FeedViewModelFactory(requireActivity().application, MediumXRepository(), PagingRepository(MediumXRepository()))


        viewModel = ViewModelProvider(this, factory).get(FeedViewModel::class.java)
        localFeedListAdapter = LocalFeedListAdapter()

        articleDAO = MediumXLocalClient().getDAO(requireActivity().application)

        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        viewModel.fetchLocalFeed(articleDAO).observe({ lifecycle }) {
            localFeedListAdapter.differ.submitList(it)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val dbArticle = localFeedListAdapter.differ.currentList[position]
                viewModel.deleteSaveArticle(dbArticle, articleDAO)
                Snackbar.make(view, "Delete successfully.", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            viewModel.saveArticle(dbArticle, articleDAO)
                        }
                    }
                    .show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(_binding?.localFeedRecyclerView)
        }

    }

    private fun initRecyclerView() {
        _binding?.localFeedRecyclerView?.apply {
            adapter = localFeedListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}