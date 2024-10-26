package com.andrian.mydicodingeventapp.ui.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrian.mydicodingeventapp.R
import com.andrian.mydicodingeventapp.data.Result
import com.andrian.mydicodingeventapp.databinding.FragmentHomeBinding
import com.andrian.mydicodingeventapp.di.Injection
import com.andrian.mydicodingeventapp.ui.adapter.FinishedEventAdapter
import com.andrian.mydicodingeventapp.ui.adapter.UpcomingEventAdapter
import com.andrian.mydicodingeventapp.ui.detail.DetailActivity
import com.andrian.mydicodingeventapp.ui.favorite.FavoriteActivity
import com.andrian.mydicodingeventapp.ui.search.SearchActivity
import com.andrian.mydicodingeventapp.utils.ViewModelFactory
import com.google.android.material.carousel.CarouselLayoutManager

@Suppress("DEPRECATION")
class HomeFragment : Fragment(), UpcomingEventAdapter.OnItemClickListener,
    FinishedEventAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvHome: RecyclerView
    private lateinit var rvHomeFinished: RecyclerView
    private lateinit var adapter: UpcomingEventAdapter
    private lateinit var adapter1: FinishedEventAdapter
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(Injection.provideRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        adapter = UpcomingEventAdapter(this)
        adapter1 = FinishedEventAdapter(this)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#2d3e50")))
        }
        val root: View = binding.root

        setupRecyclerView()

        val textView: TextView = binding.textFinished
        val textView1: TextView = binding.textUpcoming

        homeViewModel.textUpcoming.observe(viewLifecycleOwner) {
            textView1.text = it
        }

        homeViewModel.textFinihsed.observe(viewLifecycleOwner) {
            textView.text = it
        }

        homeViewModel.getUpcomingEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    adapter.submitListUpcoming(result.data)
                    binding.progressBarUpcoming.visibility = View.GONE
                }

                is Result.Error -> {
                    Toast.makeText(context, result.exception, Toast.LENGTH_SHORT).show()
                    binding.progressBarUpcoming.visibility = View.GONE
                }

                is Result.Loading -> {
                    binding.progressBarUpcoming.visibility = View.VISIBLE
                }
            }
        }

        homeViewModel.getFinishedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    adapter1.submitListFinished(result.data)
                    binding.progressBarFinished.visibility = View.GONE
                }

                is Result.Error -> {
                    Toast.makeText(context, result.exception, Toast.LENGTH_SHORT).show()
                    binding.progressBarFinished.visibility = View.GONE
                }

                is Result.Loading -> {
                    binding.progressBarFinished.visibility = View.VISIBLE
                }
            }
        }
        return root
    }

    private fun setupRecyclerView() {
        rvHome = binding.itemHome
        adapter = UpcomingEventAdapter(this)
        rvHome.layoutManager = CarouselLayoutManager()
        rvHome.adapter = adapter
        rvHomeFinished = binding.itemHomeFinished
        adapter1 = FinishedEventAdapter(this)
        rvHomeFinished.layoutManager = GridLayoutManager(requireContext(), 2)
        rvHomeFinished.adapter = adapter1
    }

    override fun onItemClick(eventId: String) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("eventId", eventId)
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.action_search)
        val favoriteItem = menu.findItem(R.id.action_favorite)

        favoriteItem?.setOnMenuItemClickListener {
            val intent = Intent(requireContext(), FavoriteActivity::class.java)
            startActivity(intent)
            true
        }

        searchItem?.setOnMenuItemClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
