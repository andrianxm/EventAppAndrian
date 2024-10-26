package com.andrian.mydicodingeventapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrian.mydicodingeventapp.data.response.ListEventsItem
import com.andrian.mydicodingeventapp.databinding.ItemCarouselBinding
import com.bumptech.glide.Glide


class UpcomingEventAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<UpcomingEventAdapter.UpcomingEventViewHolder>() {

    private val eventList: MutableList<ListEventsItem> = mutableListOf()

    inner class UpcomingEventViewHolder(private val binding: ItemCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: ListEventsItem) {
            Glide.with(binding.root)
                .load(event.mediaCover)
                .into(binding.headerImage)
            binding.title.text = event.name
            binding.quota.text = "Kuota: " + event.quota.toString()
            binding.ownername.text = event.ownerName
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = eventList[position]
                    listener.onItemClick(event.id.toString())
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitListUpcoming(events: List<ListEventsItem>) {
        eventList.clear()
        eventList.addAll(events)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): UpcomingEventViewHolder {
        val binding =
            ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingEventViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    interface OnItemClickListener {
        fun onItemClick(eventId: String)
    }

    override fun getItemCount(): Int = eventList.size
}