package com.travelapp.ui.agenda.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.travelapp.data.Trip
import com.travelapp.data.TravelItem
import com.travelapp.databinding.ItemTripBinding
import java.text.SimpleDateFormat
import java.util.*

class TripsAdapter : ListAdapter<Trip, TripsAdapter.TripViewHolder>(TripDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TripViewHolder(private val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())

        fun bind(trip: Trip) {
            binding.tripNameText.text = trip.name
            binding.tripDestinationText.text = trip.destination ?: ""
            
            val dateRange = buildString {
                trip.startDate?.let { append(dateFormat.format(it)) }
                trip.endDate?.let { 
                    if (trip.startDate != null) append(" - ")
                    append(dateFormat.format(it))
                }
            }
            binding.tripDatesText.text = dateRange

            // TODO: Load travel items for this trip
            // For now, show empty list
        }
    }

    class TripDiffCallback : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem == newItem
        }
    }
}
