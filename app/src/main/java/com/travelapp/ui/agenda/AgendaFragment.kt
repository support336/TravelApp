package com.travelapp.ui.agenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelapp.databinding.FragmentAgendaBinding
import com.travelapp.ui.agenda.adapter.TripsAdapter

class AgendaFragment : Fragment() {

    private var _binding: FragmentAgendaBinding? = null
    private val binding get() = _binding!!

    private lateinit var agendaViewModel: AgendaViewModel
    private lateinit var tripsAdapter: TripsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgendaBinding.inflate(inflater, container, false)
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
        agendaViewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
    }

    private fun setupRecyclerView() {
        tripsAdapter = TripsAdapter()
        binding.tripsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tripsAdapter
        }
    }

    private fun setupClickListeners() {
        binding.signInButton.setOnClickListener {
            agendaViewModel.signInWithGoogle()
        }

        binding.addTravelItemFab.setOnClickListener {
            showAddTravelItemDialog()
        }
    }

    private fun observeViewModel() {
        agendaViewModel.isSignedIn.observe(viewLifecycleOwner) { isSignedIn ->
            binding.signInLayout.visibility = if (isSignedIn) View.GONE else View.VISIBLE
        }

        agendaViewModel.isSyncing.observe(viewLifecycleOwner) { isSyncing ->
            binding.syncProgressBar.visibility = if (isSyncing) View.VISIBLE else View.GONE
            binding.syncStatusText.visibility = if (isSyncing) View.VISIBLE else View.GONE
        }

        agendaViewModel.trips.observe(viewLifecycleOwner) { trips ->
            tripsAdapter.submitList(trips)
            binding.noTripsText.visibility = if (trips.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showAddTravelItemDialog() {
        // TODO: Implement add travel item dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
