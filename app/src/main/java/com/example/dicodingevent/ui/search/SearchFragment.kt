package com.example.dicodingevent.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.R
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentSearchBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private fun getSearchEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = SearchEventAdapter()
        adapter.submitList(eventsItem)
        binding.rvSearchEvents.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbSearchEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBar = binding.searchBar
        val searchView = binding.searchView
        val searchEventViewModel = ViewModelProvider(this)[SearchEventViewModel::class.java]
        val rvSearchEvents = binding.rvSearchEvents
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        searchBar.setOnClickListener {
            searchView.show()
        }

        searchView.setupWithSearchBar(searchBar)
        searchView.editText.setOnEditorActionListener { _, _, _ ->
            searchBar.setText(searchView.text)
            val query = searchView.text
            if (query.isNotEmpty()) {
                searchEventViewModel.searchEvent(query.toString())
            }
            searchView.hide()
            false
        }

        searchEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
            binding.rvSearchEvents.layoutManager = LinearLayoutManager(requireActivity())
            getSearchEventsData(eventsItem)
        }

        searchEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        if (bottomNavigationView == null) {
            Log.e("SearchFragment", "BottomNavigationView is null")
            return
        }

        rvSearchEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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