package com.andrian.mydicodingeventapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrian.mydicodingeventapp.data.response.ListEventsItem
import com.andrian.mydicodingeventapp.databinding.ItemCardRowBinding
import com.bumptech.glide.Glide

class EventAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private val eventList: MutableList<ListEventsItem> = mutableListOf()

    interface OnItemClickListener {
        fun onItemClick(eventId: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EventViewHolder {
        val binding = ItemCardRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }


    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val upcoming = eventList[position]
        holder.bind(upcoming)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(events: List<ListEventsItem>) {
        eventList.clear()
        eventList.addAll(events)
        notifyDataSetChanged()
    }

    inner class EventViewHolder(private val binding: ItemCardRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(event: ListEventsItem) {
            Glide.with(binding.root).load(event.imageLogo).into(binding.headerImage)
            binding.title.text = event.name
            binding.summary.text = event.summary
            binding.quota.text = "Kuota: " + event.quota.toString()
            binding.ownername.text = "Diselenggarakan oleh: " + event.ownerName
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
}
