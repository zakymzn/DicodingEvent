package com.example.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.settings.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.toolbar
        val nestedScrollView = binding?.nsvHome
        val rvHomeUpcomingEvents = binding?.rvHomeUpcomingEvents
        val rvHomeFinishedEvents = binding?.rvHomeFinishedEvents
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        toolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
                    val intent = Intent(requireActivity(), SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val homeUpcomingEventViewModelFactory: HomeUpcomingEventViewModelFactory = HomeUpcomingEventViewModelFactory.getInstance(requireActivity())
        val homeFinishedEventViewModelFactory: HomeFinishedEventViewModelFactory = HomeFinishedEventViewModelFactory.getInstance(requireActivity())

        val homeUpcomingEventViewModel: HomeUpcomingEventViewModel by viewModels {
            homeUpcomingEventViewModelFactory
        }

        val homeFinishedEventViewModel: HomeFinishedEventViewModel by viewModels {
            homeFinishedEventViewModelFactory
        }

        val homeUpcomingEventAdapter = HomeUpcomingEventAdapter { event ->
            if (event.isFavorited) {
                homeUpcomingEventViewModel.deleteEvent(event)
            } else {
                homeUpcomingEventViewModel.saveEvent(event)
            }
        }

        val homeFinishedEventAdapter = HomeFinishedEventAdapter { event ->
            if (event.isFavorited) {
                homeFinishedEventViewModel.deleteEvent(event)
            } else {
                homeFinishedEventViewModel.saveEvent(event)
            }
        }

        homeUpcomingEventViewModel.getHomeUpcomingEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.pbHomeUpcomingEvents?.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding?.pbHomeUpcomingEvents?.visibility = View.GONE
                        val events = result.data
                        if (events.isEmpty()) {
                            binding?.apply {
                                ivIllustration.visibility = View.VISIBLE
                                tvNoUpcomingEvent.visibility = View.VISIBLE
                                homeUpcomingEventAdapter.submitList(events)
                            }
                        } else {
                            binding?.apply {
                                ivIllustration.visibility = View.GONE
                                tvNoUpcomingEvent.visibility = View.GONE
                                homeUpcomingEventAdapter.submitList(events)
                            }
                        }
                    }

                    is Result.Error -> {
                        binding?.pbHomeUpcomingEvents?.visibility = View.GONE
                        Toast.makeText(context, "Error: " + result.error , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        homeFinishedEventViewModel.getHomeFinishedEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.pbHomeFinishedEvents?.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding?.pbHomeFinishedEvents?.visibility = View.GONE
                        val events = result.data
                        homeFinishedEventAdapter.submitList(events)
                    }

                    is Result.Error -> {
                        binding?.pbHomeFinishedEvents?.visibility = View.GONE
                        Toast.makeText(context, "Error: " + result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        rvHomeUpcomingEvents?.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = homeUpcomingEventAdapter
        }

        rvHomeFinishedEvents?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = homeFinishedEventAdapter
        }

        binding?.btTryAgain?.setOnClickListener {
            findNavController().navigateUp()
            findNavController().navigate(R.id.navigation_home)
        }

        if (bottomNavigationView == null) {
            Log.e("HomeFragment", "BottomNavigationView is null")
            return
        }

        nestedScrollView?.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                bottomNavigationView.animate()?.translationY(bottomNavigationView.height.toFloat())?.setDuration(200)
            } else if (scrollY < oldScrollY) {
                bottomNavigationView.animate()?.translationY(0f)?.setDuration(200)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}