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
import com.example.dicodingevent.ui.FinishedEventViewModel
import com.example.dicodingevent.ui.UpcomingEventViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private fun getUpcomingEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = HomeUpcomingEventAdapter()
        if (eventsItem.count() <= 5) adapter.submitList(eventsItem) else adapter.submitList(eventsItem.subList(0,5))
        binding.rvHomeUpcomingEvents.adapter = adapter
    }

    private fun getFinishedEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = HomeFinishedEventAdapter()
        adapter.submitList(eventsItem.subList(0,5))
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
        val upcomingEventViewModel = ViewModelProvider(this).get(UpcomingEventViewModel::class.java)
        val finishedEventViewModel = ViewModelProvider(this).get(FinishedEventViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        upcomingEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
            binding.rvHomeUpcomingEvents.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            getUpcomingEventsData(eventsItem)
        }

        upcomingEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        finishedEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
            binding.rvHomeFinishedEvents.layoutManager = LinearLayoutManager(requireActivity())
            getFinishedEventsData(eventsItem)
        }

        finishedEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}