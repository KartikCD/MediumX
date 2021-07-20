package com.kartikcd.mediumx.ui.feed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayout
import com.kartikcd.mediumx.R
import com.kartikcd.mediumx.databinding.FragmentFeedBinding
import com.kartikcd.mediumx.domain.MediumXRepository

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentFeedBinding.inflate(inflater, container, false)

        return _binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = _binding?.let {
            Navigation.findNavController(it.root.findViewById(R.id.feedFragmentNavHost))
        }

        _binding?.feedTabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {
                        navController?.navigate(R.id.gotoGlobalFeedFragment)
                    }
                    1 -> {
                        navController?.navigate(R.id.gotoLocalFeedFragment)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}