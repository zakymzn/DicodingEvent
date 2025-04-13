package com.example.dicodingevent.ui.favorited

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.FragmentFavoritedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoritedFragment : Fragment() {

    private var _binding: FragmentFavoritedBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritedBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvFavoritedEvents = binding?.rvFavoritedEvents
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        val favoritedEventViewModelFactory: FavoritedEventViewModelFactory =
            FavoritedEventViewModelFactory.getInstance(requireActivity())

        val favoritedEventViewModel: FavoritedEventViewModel by viewModels {
            favoritedEventViewModelFactory
        }

        val favoritedEventAdapter = FavoritedEventAdapter { event ->
            if (event.isFavorited) {
                favoritedEventViewModel.deleteEvent(event)
            } else {
                favoritedEventViewModel.saveEvent(event)
            }
        }

        favoritedEventViewModel.getFavoritedEvent().observe(viewLifecycleOwner) { favoritedEvent ->
            binding?.pbFavoritedEvents?.visibility = View.GONE
            if (favoritedEvent.isEmpty()) {
                binding?.ivIllustration?.visibility = View.VISIBLE
                binding?.tvNoFavoritedEvent?.visibility = View.VISIBLE
                favoritedEventAdapter.submitList(favoritedEvent)
            } else {
                binding?.ivIllustration?.visibility = View.GONE
                binding?.tvNoFavoritedEvent?.visibility = View.GONE
                favoritedEventAdapter.submitList(favoritedEvent)
            }
        }

        rvFavoritedEvents?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = favoritedEventAdapter
        }

        if (bottomNavigationView == null) {
            Log.e("FavoritedFragment", "BottomNavigationView is null")
            return
        }

        rvFavoritedEvents?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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