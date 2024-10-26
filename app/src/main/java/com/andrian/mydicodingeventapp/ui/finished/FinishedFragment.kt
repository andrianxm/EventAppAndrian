package com.andrian.mydicodingeventapp.ui.finished

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
import com.andrian.mydicodingeventapp.databinding.FragmentFinishedBinding
import com.andrian.mydicodingeventapp.di.Injection
import com.andrian.mydicodingeventapp.ui.adapter.EventAdapter
import com.andrian.mydicodingeventapp.ui.detail.DetailActivity
import com.andrian.mydicodingeventapp.utils.ViewModelFactory

class FinishedFragment : Fragment(), EventAdapter.OnItemClickListener {

    private var _binding: FragmentFinishedBinding? = null
    private lateinit var rvFinished: RecyclerView
    private lateinit var adapter: EventAdapter
    private val finishedViewModel: FinishedViewModel by viewModels {
        ViewModelFactory(Injection.provideRepository(requireContext()))
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        adapter = EventAdapter(this)
        setupRecyclerView()

        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(result.data)
                    binding.itemFinished.adapter = adapter
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
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("eventId", eventId)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        rvFinished = binding.itemFinished
        rvFinished.layoutManager = LinearLayoutManager(requireContext())
        rvFinished.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        rvFinished.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}