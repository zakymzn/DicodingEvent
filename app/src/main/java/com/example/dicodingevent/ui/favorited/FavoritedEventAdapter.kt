package com.example.dicodingevent.ui.favorited

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity
import com.example.dicodingevent.databinding.ItemEventBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.ranges.contains

class FavoritedEventAdapter : ListAdapter<FavoriteEventEntity, FavoritedEventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
       val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(event: FavoriteEventEntity) {
            val today = LocalDateTime.now()
            val parsedDateTime = LocalDateTime.parse(event.beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            val id = event.id

            Glide.with(this@MyViewHolder.itemView.context)
                .load(event.imageLogo)
                .into(binding.ivLogo)
            binding.apply {
                tvCategory.text = "${event.category}"
                tvName.text = "${event.name}"
                tvOwner.text = this@MyViewHolder.itemView.context.getString(R.string.oleh, event.ownerName)
                tvSummary.text = "${event.summary}"
                tvQuota.text = if (ChronoUnit.MINUTES.between(today, parsedDateTime) > 0) "Sisa kuota: ${(event.registrants?.let { event.quota?.minus(it) })}" else null
                tvCountdown.text = if (ChronoUnit.DAYS.between(today, parsedDateTime) > 0) {
                    "${ChronoUnit.DAYS.between(today, parsedDateTime)} hari lagi"
                } else if (ChronoUnit.HOURS.between(today, parsedDateTime) in 1..24) {
                    "${ChronoUnit.HOURS.between(today, parsedDateTime)} jam lagi"
                } else if (ChronoUnit.MINUTES.between(today, parsedDateTime) in 1..60) {
                    "${ChronoUnit.MINUTES.between(today, parsedDateTime)} menit lagi"
                } else {
                    "Selesai"
                }
                itemEvent.setOnClickListener { view ->
                    val toDetailFragment = FavoritedFragmentDirections.actionNavigationFavoritedToNavigationDetail()
                    if (id != null) {
                        toDetailFragment.id = id
                    }
                    view.findNavController().navigate(toDetailFragment)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FavoriteEventEntity> = object : DiffUtil.ItemCallback<FavoriteEventEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}