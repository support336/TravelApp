package com.travelapp.ui.points

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelapp.databinding.FragmentPointsBinding
import com.travelapp.ui.points.adapter.POIsAdapter

class PointsOfInterestFragment : Fragment() {

    private var _binding: FragmentPointsBinding? = null
    private val binding get() = _binding!!

    private lateinit var pointsViewModel: PointsViewModel
    private lateinit var poisAdapter: POIsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPointsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewModel() {
        pointsViewModel = ViewModelProvider(this)[PointsViewModel::class.java]
    }

    private fun setupRecyclerView() {
        poisAdapter = POIsAdapter { poi, action ->
            when (action) {
                com.travelapp.ui.points.adapter.POIAction.NAVIGATE -> navigateToPOI(poi)
                com.travelapp.ui.points.adapter.POIAction.DELETE -> deletePOI(poi)
            }
        }
        binding.poisRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = poisAdapter
        }
    }

    private fun setupClickListeners() {
        binding.addPoiFab.setOnClickListener {
            showAddPOIDialog()
        }
    }

    private fun observeViewModel() {
        pointsViewModel.pois.observe(viewLifecycleOwner) { pois ->
            poisAdapter.submitList(pois)
            binding.noPoisText.visibility = if (pois.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun navigateToPOI(poi: com.travelapp.data.PointOfInterest) {
        // TODO: Navigate to map tab and show POI
    }

    private fun deletePOI(poi: com.travelapp.data.PointOfInterest) {
        pointsViewModel.deletePOI(poi)
    }

    private fun showAddPOIDialog() {
        // TODO: Implement add POI dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
