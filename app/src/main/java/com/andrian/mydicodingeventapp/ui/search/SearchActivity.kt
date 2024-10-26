package com.andrian.mydicodingeventapp.ui.search

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrian.mydicodingeventapp.data.Result
import com.andrian.mydicodingeventapp.databinding.ActivitySearchBinding
import com.andrian.mydicodingeventapp.ui.adapter.SearchEventAdapter
import com.andrian.mydicodingeventapp.ui.detail.DetailActivity
import com.andrian.mydicodingeventapp.utils.ViewModelFactory
import java.util.Locale

class SearchActivity : AppCompatActivity(), SearchEventAdapter.OnItemClickListener {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var rvSearch: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: SearchEventAdapter
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Search event"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#2d3e50")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchViewModel =
            ViewModelProvider(this, ViewModelFactory.getInstance(this))[SearchViewModel::class.java]

        searchViewModel.searchEvent.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val events = result.data.listEvents
                    adapter.submitList(events)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, result.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }

        rvSearch = binding.recyclerView
        searchView = binding.searchView

        setupRecyclerView()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        rvSearch = binding.recyclerView
        adapter = SearchEventAdapter(mutableListOf(), this)
        rvSearch.layoutManager = LinearLayoutManager(this)
        rvSearch.adapter = adapter
    }

    override fun onItemClick(eventId: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("eventId", eventId)
        startActivity(intent)
    }

    private fun filterList(newText: String?) {
        val query = newText?.lowercase(Locale.ROOT)
        val result = searchViewModel.searchEvent.value
        if (result is Result.Success) {
            val events = result.data.listEvents
            if (query != null) {
                val filteredList = events.filter { event ->
                    event.name?.lowercase(Locale.ROOT)?.contains(query) == true
                }

                if (filteredList.isEmpty()) {
                    Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.submitList(filteredList)
                }
            } else {
                adapter.submitList(events)
            }
        } else {
            if (result is Result.Error) {
                Toast.makeText(this, result.exception, Toast.LENGTH_SHORT).show()
            }
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
}