package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.databinding.FragmentDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import androidx.core.net.toUri

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDetailEventData(event: EventEntity) {
        val today = LocalDateTime.now()

        val parsedBeginTime = LocalDateTime.parse(event.beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val formattedBeginDate = parsedBeginTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        val formattedBeginTime = parsedBeginTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        val parsedEndTime = LocalDateTime.parse(event.endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val formattedEndDate = parsedEndTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        val formattedEndTime = parsedEndTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        Glide.with(this)
            .load(event.mediaCover)
            .into(binding?.ivMediaCover!!)
        binding?.apply {
            tvName.text = "${event.name}"
            tvOwner.text = getString(R.string.diselenggarakan_oleh, event.ownerName)
            tvCategory.text = "${event.category}"
            tvCityName.text = "${event.cityName}"
            tvQuota.text = if (ChronoUnit.MINUTES.between(today, parsedBeginTime) > 0) "${(event.registrants?.let { event.quota?.minus(it) })} peserta" else "Kuota habis"
            tvBeginTime.text = getString(R.string.mulai, formattedBeginDate, formattedBeginTime)
            tvEndTime.text = getString(R.string.selesai, formattedEndDate, formattedEndTime)
            tvDescriptionContent.text = Html.fromHtml(event.description)
        }

        if (ChronoUnit.MINUTES.between(today, parsedBeginTime) > 0) {
            binding?.apply {
                tvEventFinished.visibility = View.GONE
                efabRegister.visibility = View.VISIBLE
                efabSeeWebPage.visibility = View.GONE
                efabRegister.setOnClickListener {
                    val urlIntent = Intent(Intent.ACTION_VIEW)
                    urlIntent.data = event.link?.toUri()
                    startActivity(urlIntent)
                }
            }
        } else {
            binding?.apply {
                tvEndTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.soft_red))
                tvEventFinished.visibility = View.VISIBLE
                efabRegister.visibility = View.GONE
                efabSeeWebPage.visibility = View.VISIBLE
                efabSeeWebPage.setOnClickListener {
                    val urlIntent = Intent(Intent.ACTION_VIEW)
                    urlIntent.data = event.link?.toUri()
                    startActivity(urlIntent)
                }
            }
        }

        println("Is event favorited? = " + event.isFavorited)
        if (event.isFavorited) {
            binding?.fabFavorite?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_24))
        } else {
            binding?.fabFavorite?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_border_24))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = binding?.toolbar!!
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        val detailEventViewModel: DetailEventViewModel by viewModels()

        detailEventViewModel.getDetailEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.pbDetailEvent?.visibility = View.VISIBLE
                        binding?.clDetailEvent?.visibility = View.GONE
                    }

                    is Result.Success -> {
                        binding?.pbDetailEvent?.visibility = View.GONE
                        binding?.clDetailEvent?.visibility = View.VISIBLE
                        val event = result.data
                        getDetailEventData(event)
                        binding?.fabFavorite?.setOnClickListener {
                            if (event.isFavorited) {
                                detailEventViewModel.deleteEvent(event)
                            } else {
                                detailEventViewModel.saveEvent(event)
                            }
                        }
                    }

                    is Result.Error -> {
                        binding?.pbDetailEvent?.visibility = View.GONE
                        Toast.makeText(context, "Error: " + result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
            bottomNavigationView.animate()?.translationY(0f)?.duration = 200
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigateUp()
            bottomNavigationView.animate()?.translationY(0f)?.duration = 200
        }

        if (bottomNavigationView == null) {
            Log.e("DetailFragment", "BottomNavigationView is null")
            return
        }

        bottomNavigationView.animate()?.translationY(bottomNavigationView.height.toFloat())?.duration = 200
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}