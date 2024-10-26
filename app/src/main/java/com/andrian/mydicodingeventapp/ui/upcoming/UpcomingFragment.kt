package com.andrian.mydicodingeventapp.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrian.mydicodingeventapp.data.Result
import com.andrian.mydicodingeventapp.databinding.FragmentUpcomingBinding
import com.andrian.mydicodingeventapp.di.Injection
import com.andrian.mydicodingeventapp.ui.adapter.EventAdapter
import com.andrian.mydicodingeventapp.ui.detail.DetailActivity
import com.andrian.mydicodingeventapp.utils.ViewModelFactory

class UpcomingFragment : Fragment(), EventAdapter.OnItemClickListener {

    private var _binding: FragmentUpcomingBinding? = null
    private lateinit var rvUpcoming: RecyclerView
    private lateinit var adapter: EventAdapter
    private val upcomingViewModel: UpcomingViewModel by viewModels {
        ViewModelFactory(Injection.provideRepository(requireContext()))
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        adapter = EventAdapter(this)

        setupRecyclerView()

        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(result.data)
                    binding.itemUpcoming.adapter = adapter
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, result.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }

    override fun onItemClick(eventId: String) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("eventId", eventId)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        rvUpcoming = binding.itemUpcoming
        rvUpcoming.layoutManager = LinearLayoutManager(requireContext())
        rvUpcoming.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        rvUpcoming.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}