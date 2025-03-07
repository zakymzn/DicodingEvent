package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private fun getUpcomingEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = HomeUpcomingEventAdapter()
        adapter.submitList(eventsItem)
        binding.rvHomeUpcomingEvents.adapter = adapter
    }

    private fun getFinishedEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = HomeFinishedEventAdapter()
        adapter.submitList(eventsItem)
        binding.rvHomeFinishedEvents.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbHomeUpcomingEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeUpcomingEventViewModel = ViewModelProvider(this).get(HomeUpcomingEventViewModel::class.java)
        val homeFinishedEventViewModel = ViewModelProvider(this).get(HomeFinishedEventViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeUpcomingEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
            binding.rvHomeUpcomingEvents.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            getUpcomingEventsData(eventsItem)
        }

        homeUpcomingEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeFinishedEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
            binding.rvHomeFinishedEvents.layoutManager = LinearLayoutManager(requireActivity())
            getFinishedEventsData(eventsItem)
        }

        homeFinishedEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}