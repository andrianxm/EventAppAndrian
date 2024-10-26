package com.andrian.mydicodingeventapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrian.mydicodingeventapp.R
import com.andrian.mydicodingeventapp.data.local.FavoriteEvent
import com.bumptech.glide.Glide

class FavoriteEventAdapter(private val favoriteEvents: List<FavoriteEvent>) :
    RecyclerView.Adapter<FavoriteEventAdapter.ViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(eventId: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class ViewHolder(itemView: View, private val listener: OnItemClickListener, private val favoriteEvents: List<FavoriteEvent>) : RecyclerView.ViewHolder(itemView) {
        val mediaCoverImageView: ImageView = itemView.findViewById(R.id.headerImage)
        val nameTextView: TextView = itemView.findViewById(R.id.title)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(favoriteEvents[position].id)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view, onItemClickListener, favoriteEvents)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = favoriteEvents[position]
        holder.nameTextView.text = event.name
        Glide.with(holder.itemView.context)
            .load(event.mediaCover)
            .into(holder.mediaCoverImageView)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(favoriteEvents[position].id)
        }
    }

    override fun getItemCount(): Int = favoriteEvents.size
}
