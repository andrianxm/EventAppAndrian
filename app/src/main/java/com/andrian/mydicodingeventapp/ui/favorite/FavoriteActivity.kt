package com.andrian.mydicodingeventapp.ui.favorite

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrian.mydicodingeventapp.R
import com.andrian.mydicodingeventapp.databinding.ActivityFavoriteBinding
import com.andrian.mydicodingeventapp.ui.adapter.FavoriteEventAdapter
import com.andrian.mydicodingeventapp.ui.adapter.SearchEventAdapter
import com.andrian.mydicodingeventapp.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity(), SearchEventAdapter.OnItemClickListener,
    FavoriteEventAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoriteEventAdapter
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#2d3e50")))

        setContentView(binding.root)

        supportActionBar?.title = "Event Favorit"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.item_favorite)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        viewModel.favoriteEvents.observe(this) { favoriteEvents ->
            binding.progressBar.visibility = View.GONE
            adapter = FavoriteEventAdapter(favoriteEvents)
            adapter.setOnItemClickListener(this)
            recyclerView.adapter = adapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(eventId: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("eventId", eventId)
        startActivity(intent)
    }
}
