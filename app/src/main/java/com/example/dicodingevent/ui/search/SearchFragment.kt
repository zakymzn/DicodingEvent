package com.example.dicodingevent.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.R
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentSearchBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding

//    private fun getSearchEventsData(eventsItem: List<ListEventsItem>) {
//        val adapter = SearchEventAdapter()
//        adapter.submitList(eventsItem)
//        binding.rvSearchEvents.adapter = adapter
//
//        if (eventsItem.isEmpty()) {
//            binding.apply {
//                binding.ivEventNotFound.visibility = View.VISIBLE
//                binding.tvEventNotFound.visibility = View.VISIBLE
//            }
//        } else {
//            binding.apply {
//                binding.ivEventNotFound.visibility = View.GONE
//                binding.tvEventNotFound.visibility = View.GONE
//            }
//        }
//    }
//
//    private fun showLoading(isLoading: Boolean) {
//        binding.pbSearchEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
//
//    private fun showErrorMessage(errorMessage: String) {
//        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
//        val root: View = binding.root

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBar = binding?.searchBar
        val searchView = binding?.searchView
        val rvSearchEvents = binding?.rvSearchEvents
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        val searchEventViewModelFactory: SearchEventViewModelFactory = SearchEventViewModelFactory.getInstance(requireActivity())

        val searchEventViewModel: SearchEventViewModel by viewModels {
            searchEventViewModelFactory
        }

        val searchEventAdapter = SearchEventAdapter { event ->
            if (event.isFavorited) {
                searchEventViewModel.deleteEvent(event)
            } else {
                searchEventViewModel.saveEvent(event)
            }
        }

//        val searchEventViewModel = ViewModelProvider(this)[SearchEventViewModel::class.java]

        searchBar?.setOnClickListener {
            searchView?.show()
        }

        searchView?.setupWithSearchBar(searchBar)
        searchView?.editText?.setOnEditorActionListener { _, _, _ ->
            searchBar?.setText(searchView.text)
            val query = searchView.text
            if (query.isNotEmpty()) {
//                searchEventViewModel.searchEvent(query.toString())
                searchEventViewModel.searchEvent(query.toString()).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.pbSearchEvents?.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding?.pbSearchEvents?.visibility = View.GONE
                                val events = result.data
                                if (events.isEmpty()) {
                                    binding?.apply {
                                        ivEventNotFound.visibility = View.VISIBLE
                                        tvEventNotFound.visibility = View.VISIBLE
                                        searchEventAdapter.submitList(events)
                                    }
                                } else {
                                    binding?.apply {
                                        ivEventNotFound.visibility = View.GONE
                                        tvEventNotFound.visibility = View.GONE
                                        searchEventAdapter.submitList(events)
                                    }
                                }
                            }

                            is Result.Error -> {
                                binding?.pbSearchEvents?.visibility = View.GONE
                                Toast.makeText(context, "Error: " + result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            searchView.hide()
            false
        }

        rvSearchEvents?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = searchEventAdapter
        }
//        searchEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
//            binding.rvSearchEvents.layoutManager = LinearLayoutManager(requireActivity())
//            getSearchEventsData(eventsItem)
//        }
//
//        searchEventViewModel.isLoading.observe(viewLifecycleOwner) {
//            showLoading(it)
//        }
//
//        searchEventViewModel.errorMessage.observe(viewLifecycleOwner) {
//            showErrorMessage(it)
//        }

        if (bottomNavigationView == null) {
            Log.e("SearchFragment", "BottomNavigationView is null")
            return
        }

        rvSearchEvents?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 ) {
                    bottomNavigationView.animate()?.translationY(bottomNavigationView.height.toFloat())?.setDuration(200)
                } else if (dy < 0) {
                    bottomNavigationView.animate()?.translationY(0f)?.setDuration(200)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}