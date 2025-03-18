package com.example.dicodingevent.ui.upcoming

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
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private fun getUpcomingEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = UpcomingEventAdapter()
        adapter.submitList(eventsItem)
        binding.rvUpcomingEvents.adapter = adapter

        if (eventsItem.isEmpty()) {
            binding.ivIllustration.visibility = View.VISIBLE
            binding.tvNoUpcomingEvent.visibility = View.VISIBLE
        } else {
            binding.ivIllustration.visibility = View.GONE
            binding.tvNoUpcomingEvent.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbUpcomingEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val upcomingEventViewModel = ViewModelProvider(this)[UpcomingEventViewModel::class.java]
        val swipeRefresh = binding.swiperefresh
        val rvUpcomingEvents = binding.rvUpcomingEvents
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        upcomingEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
            binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireActivity())
            getUpcomingEventsData(eventsItem)
        }

        upcomingEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        swipeRefresh.setOnRefreshListener {
            upcomingEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
                binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireActivity())
                getUpcomingEventsData(eventsItem)
            }
            swipeRefresh.isRefreshing = false
        }

        if (bottomNavigationView == null) {
            Log.e("UpcomingFragment", "BottomNavigationView is null")
            return
        }

        rvUpcomingEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
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