package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingevent.databinding.FragmentDetailBinding
import com.example.dicodingevent.data.response.Event
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDetailEventData(event: Event) {
        val today = LocalDate.now()

        val separatedBeginTime = event.beginTime?.split(" ")
        val convertedBeginDate = LocalDate.parse(separatedBeginTime?.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val convertedBeginTime = LocalTime.parse(separatedBeginTime?.get(1), DateTimeFormatter.ofPattern("HH:mm:ss"))

        val separatedEndTime = event.endTime?.split(" ")
        val convertedEndDate = LocalDate.parse(separatedEndTime?.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val convertedEndTime = LocalTime.parse(separatedEndTime?.get(1), DateTimeFormatter.ofPattern("HH:mm:ss"))

        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivMediaCover)
        binding.tvName.text = "${event.name}"
        binding.tvOwner.text = "Diselenggarakan oleh: ${event.ownerName}"
        binding.tvCategory.text = "${event.category}"
        binding.tvCityName.text = "${event.cityName}"
        binding.tvQuota.text = if (ChronoUnit.DAYS.between(today, convertedBeginDate) > 0) "${(event.registrants?.let { event.quota?.minus(it) })}" else "Kuota habis"
        binding.tvBeginTime.text = "Mulai\t: ${convertedBeginDate} ${convertedBeginTime}"
        binding.tvEndTime.text = "Selesai\t: ${convertedEndDate} ${convertedEndTime}"
        binding.tvDescriptionContent.text = Html.fromHtml(event.description)
        binding.btRegister.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW)
            urlIntent.data = Uri.parse(event.link)
            startActivity(urlIntent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbDetailEvent.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val detailEventViewModel = ViewModelProvider(this)[DetailEventViewModel::class.java]

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        detailEventViewModel.detailEvent.observe(viewLifecycleOwner) {event ->
            if (event != null) {
                getDetailEventData(event)
            }
        }

        detailEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}