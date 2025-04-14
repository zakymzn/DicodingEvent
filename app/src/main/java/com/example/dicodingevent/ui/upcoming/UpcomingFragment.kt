package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.R
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvUpcomingEvents = binding?.rvUpcomingEvents
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        val upcomingEventViewModelFactory: UpcomingEventViewModelFactory = UpcomingEventViewModelFactory.getInstance(requireActivity())

        val upcomingEventViewModel: UpcomingEventViewModel by viewModels {
            upcomingEventViewModelFactory
        }

        val upcomingEventAdapter = UpcomingEventAdapter { event ->
            if (event.isFavorited) {
                upcomingEventViewModel.deleteEvent(event)
            } else {
                upcomingEventViewModel.saveEvent(event)
            }
        }

        upcomingEventViewModel.getUpcomingEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.pbUpcomingEvents?.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding?.pbUpcomingEvents?.visibility = View.GONE
                        val events = result.data
                        if (events.isEmpty()) {
                            binding?.ivIllustration?.visibility = View.VISIBLE
                            binding?.tvNoUpcomingEvent?.visibility = View.VISIBLE
                            upcomingEventAdapter.submitList(events)
                        } else {
                            binding?.ivIllustration?.visibility = View.GONE
                            binding?.tvNoUpcomingEvent?.visibility = View.GONE
                            upcomingEventAdapter.submitList(events)
                        }
                    }

                    is Result.Error -> {
                        binding?.pbUpcomingEvents?.visibility = View.GONE
                        Toast.makeText(context, "Error: " + result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        rvUpcomingEvents?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = upcomingEventAdapter
        }

        if (bottomNavigationView == null) {
            Log.e("UpcomingFragment", "BottomNavigationView is null")
            return
        }

        rvUpcomingEvents?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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