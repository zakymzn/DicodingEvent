package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.UpcomingEventViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private fun getUpcomingEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = UpcomingEventAdapter()
        adapter.submitList(eventsItem)
        binding.rvUpcomingEvents.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbUpcomingEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val upcomingEventViewModel = ViewModelProvider(this).get(UpcomingEventViewModel::class.java)
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        upcomingEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
            binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireActivity())
            getUpcomingEventsData(eventsItem)
        }

        upcomingEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}