package com.example.dicodingevent.ui.home

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemEventCardBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HomeUpcomingEventAdapter : ListAdapter<ListEventsItem, HomeUpcomingEventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(val binding: ItemEventCardBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(event: ListEventsItem) {
            val today = LocalDate.now()
            val separatedDateTime = event.beginTime?.split(" ")
            val convertedEventDate = LocalDate.parse(separatedDateTime?.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val convertedEventTime = LocalTime.parse(separatedDateTime?.get(1), DateTimeFormatter.ofPattern("HH:mm:ss"))

            Glide.with(this@MyViewHolder.itemView.context)
                .load(event.imageLogo)
                .into(binding.ivLogo)
            binding.tvCategory.text = "${event.category}"
            binding.tvName.text = "${event.name}"
            binding.tvOwner.text = "oleh ${event.ownerName}"
            binding.tvSummary.text = "${event.summary}"
            binding.tvQuota.text = if (ChronoUnit.DAYS.between(today, convertedEventDate) > 0) "Sisa kuota: ${(event.registrants?.let { event.quota?.minus(it) })}" else null
            binding.tvCountdown.text = if (ChronoUnit.DAYS.between(today, convertedEventDate) > 0) "${ChronoUnit.DAYS.between(today, convertedEventDate)} hari lagi" else "Selesai"
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}