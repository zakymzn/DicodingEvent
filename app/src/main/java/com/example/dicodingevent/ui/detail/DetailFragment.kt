package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.FragmentDetailBinding
import com.example.dicodingevent.data.response.Event
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDetailEventData(event: Event) {
        val today = LocalDateTime.now()

        val parsedBeginTime = LocalDateTime.parse(event.beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val formattedBeginDate = parsedBeginTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        val formattedBeginTime = parsedBeginTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        val parsedEndTime = LocalDateTime.parse(event.endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val formattedEndDate = parsedEndTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        val formattedEndTime = parsedEndTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivMediaCover)
        binding.tvName.text = "${event.name}"
        binding.tvOwner.text = "Diselenggarakan oleh: ${event.ownerName}"
        binding.tvCategory.text = "${event.category}"
        binding.tvCityName.text = "${event.cityName}"
        binding.tvQuota.text = if (ChronoUnit.MINUTES.between(today, parsedBeginTime) > 0) "${(event.registrants?.let { event.quota?.minus(it) })}" else "Kuota habis"
        binding.tvBeginTime.text = "Mulai\t: ${formattedBeginDate} ${formattedBeginTime}"
        binding.tvEndTime.text = "Selesai\t: ${formattedEndDate} ${formattedEndTime}"
        binding.tvDescriptionContent.text = Html.fromHtml(event.description)
        binding.btRegister.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW)
            urlIntent.data = Uri.parse(event.link)
            startActivity(urlIntent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbDetailEvent.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.clDetailEvent.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val detailEventViewModel = ViewModelProvider(this)[DetailEventViewModel::class.java]

        val toolbar: Toolbar = binding.toolbar
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

        detailEventViewModel.detailEvent.observe(viewLifecycleOwner) {event ->
            if (event != null) {
                getDetailEventData(event)
            }
        }

        detailEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }


        if (bottomNavigationView == null) {
            Log.e("DetailFragment", "BottomNavigationView is null")
            return
        }

        bottomNavigationView.animate()?.translationY(bottomNavigationView.height.toFloat())?.setDuration(200)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
            bottomNavigationView.animate()?.translationY(0f)?.setDuration(200)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}