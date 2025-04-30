package com.example.dicodingevent.ui.finished

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
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvFinishedEvents = binding?.rvFinishedEvents
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        val finishedEventViewModelFactory: FinishedEventViewModelFactory = FinishedEventViewModelFactory.getInstance(requireActivity())

        val finishedEventViewModel: FinishedEventViewModel by viewModels {
            finishedEventViewModelFactory
        }

        val finishedEventAdapter = FinishedEventAdapter()

        finishedEventViewModel.getFinishedEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.pbFinishedEvents?.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding?.pbFinishedEvents?.visibility = View.GONE
                        val events = result.data
                        finishedEventAdapter.submitList(events)
                    }

                    is Result.Error -> {
                        binding?.pbFinishedEvents?.visibility = View.GONE
                        Toast.makeText(context, "Error: " + result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        rvFinishedEvents?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = finishedEventAdapter
        }

        if (bottomNavigationView == null) {
            Log.e("FinishedFragment", "BottomNavigationView is null")
            return
        }

        rvFinishedEvents?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    bottomNavigationView.animate()?.translationY(bottomNavigationView.height.toFloat())?.duration =
                        200
                } else if (dy < 0) {
                    bottomNavigationView.animate()?.translationY(0f)?.duration = 200
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}