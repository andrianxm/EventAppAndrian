package com.andrian.mydicodingeventapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrian.mydicodingeventapp.data.response.ListEventsItem
import com.andrian.mydicodingeventapp.databinding.ItemGridBinding
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class FinishedEventAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FinishedEventAdapter.FinsihedEventViewHolder>() {

    private val eventList: MutableList<ListEventsItem> = mutableListOf()

    inner class FinsihedEventViewHolder(private val binding: ItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            Glide.with(binding.root)
                .load(event.imageLogo)
                .into(binding.headerImage)
            binding.title.text = event.name
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
    fun submitListFinished(events: List<ListEventsItem>) {
        eventList.clear()
        eventList.addAll(events)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinsihedEventViewHolder {
        val binding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FinsihedEventViewHolder(binding)
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: FinsihedEventViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    interface OnItemClickListener {
        fun onItemClick(eventId: String) // Ganti tipe data sesuai kebutuhan
    }
}