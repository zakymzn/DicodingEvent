package com.example.dicodingevent.ui.finished

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
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private fun getFinishedEventsData(eventsItem: List<ListEventsItem>) {
        val adapter = FinishedEventAdapter()
        adapter.submitList(eventsItem)
        binding.rvFinishedEvents.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbFinishedEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val finishedEventViewModel = ViewModelProvider(this)[FinishedEventViewModel::class.java]

        finishedEventViewModel.listEvents.observe(viewLifecycleOwner) { eventsItem ->
            binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireActivity())
            getFinishedEventsData(eventsItem)
        }

        finishedEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        val rvFinishedEvents = binding.rvFinishedEvents

        if (bottomNavigationView == null) {
            Log.e("FinishedFragment", "BottomNavigationView is null")
            return
        }

        rvFinishedEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    bottomNavigationView.animate()?.translationY(bottomNavigationView.height.toFloat())?.setDuration(200)
                } else {
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