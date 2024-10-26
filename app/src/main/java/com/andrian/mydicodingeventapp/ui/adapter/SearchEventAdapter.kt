package com.andrian.mydicodingeventapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrian.mydicodingeventapp.data.response.ListEventsItem
import com.andrian.mydicodingeventapp.databinding.ItemCardRowBinding
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class SearchEventAdapter(
    private var searchResult: MutableList<ListEventsItem>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<SearchEventAdapter.SearchViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(eventId: String)
    }

    inner class SearchViewHolder(private val binding: ItemCardRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            Glide.with(binding.root)
                .load(event.imageLogo)
                .into(binding.headerImage)
            binding.title.text = event.name
            binding.summary.text = event.summary
            binding.quota.text = event.quota.toString()
            binding.ownername.text = event.ownerName
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = searchResult[position]
                    listener.onItemClick(event.id.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchEventAdapter.SearchViewHolder {
        val binding = ItemCardRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchEventAdapter.SearchViewHolder, position: Int) {
        val event = searchResult[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(search: List<ListEventsItem>) {
        searchResult.clear()
        searchResult.addAll(search)
        notifyDataSetChanged()
    }

}