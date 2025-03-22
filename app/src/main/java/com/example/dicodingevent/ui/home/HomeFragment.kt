package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private fun getUpcomingEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = HomeUpcomingEventAdapter()
        adapter.submitList(eventsItem)
        binding.rvHomeUpcomingEvents.adapter = adapter

        if (eventsItem.isEmpty()) {
            binding.apply {
                ivIllustration.visibility = View.VISIBLE
                tvNoUpcomingEvent.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                ivIllustration.visibility = View.GONE
                tvNoUpcomingEvent.visibility = View.GONE
            }
        }
    }

    private fun getFinishedEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = HomeFinishedEventAdapter()
        adapter.submitList(eventsItem)
        binding.rvHomeFinishedEvents.adapter = adapter
    }

    private fun showLoadingUpcomingEvents(isLoading: Boolean) {
        binding.pbHomeUpcomingEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingFinishedEvents(isLoading: Boolean) {
        binding.pbHomeFinishedEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessageUpcomingEvents(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessageFinishedEvents(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedScrollView = binding.nsvHome
        val homeUpcomingEventViewModel = ViewModelProvider(this)[HomeUpcomingEventViewModel::class.java]
        val homeFinishedEventViewModel = ViewModelProvider(this)[HomeFinishedEventViewModel::class.java]
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        binding.btTryAgain.setOnClickListener {
            findNavController().navigateUp()
            findNavController().navigate(R.id.navigation_home)
        }

        homeUpcomingEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
            binding.rvHomeUpcomingEvents.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            getUpcomingEventsData(eventsItem)
        }

        homeUpcomingEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoadingUpcomingEvents(it)
        }

        homeUpcomingEventViewModel.errorMessage.observe(viewLifecycleOwner) {
            showErrorMessageUpcomingEvents(it)
        }

        homeFinishedEventViewModel.listEvents.observe(viewLifecycleOwner) {eventsItem ->
            binding.rvHomeFinishedEvents.layoutManager = LinearLayoutManager(requireActivity())
            getFinishedEventsData(eventsItem)
        }

        homeFinishedEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoadingFinishedEvents(it)
        }

        homeFinishedEventViewModel.errorMessage.observe(viewLifecycleOwner) {
            showErrorMessageFinishedEvents(it)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (homeUpcomingEventViewModel.listEvents.value?.size == null && homeFinishedEventViewModel.listEvents.value?.size == null) {
                Log.e("HomeFragment", "No data available")
                binding.apply {
                    clUpcomingEvents.visibility = View.GONE
                    clFinishedEvents.visibility = View.GONE
                    clFailedToGetData.visibility = View.VISIBLE
                }
            }
        }, 5000)

        if (bottomNavigationView == null) {
            Log.e("HomeFragment", "BottomNavigationView is null")
            return
        }

        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
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