package com.travelapp.ui.points.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.travelapp.data.PointOfInterest
import com.travelapp.databinding.ItemPoiBinding

class POIsAdapter(
    private val onPOIAction: (PointOfInterest, POIAction) -> Unit
) : ListAdapter<PointOfInterest, POIsAdapter.POIViewHolder>(POIDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): POIViewHolder {
        val binding = ItemPoiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return POIViewHolder(binding, onPOIAction)
    }

    override fun onBindViewHolder(holder: POIViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class POIViewHolder(
        private val binding: ItemPoiBinding,
        private val onPOIAction: (PointOfInterest, POIAction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(poi: PointOfInterest) {
            binding.poiNameText.text = poi.name
            binding.poiDescriptionText.text = poi.description ?: ""
            binding.poiCategoryText.text = poi.category.name
            binding.poiRatingText.text = poi.rating?.let { "$it ⭐" } ?: ""

            // Set appropriate icon based on category
            val iconRes = when (poi.category) {
                com.travelapp.data.POICategory.RESTAURANT -> com.travelapp.R.drawable.ic_restaurant_24
                com.travelapp.data.POICategory.ATTRACTION -> com.travelapp.R.drawable.ic_attraction_24
                com.travelapp.data.POICategory.HOTEL -> com.travelapp.R.drawable.ic_hotel_24
                else -> com.travelapp.R.drawable.ic_points_24
            }
            binding.poiIcon.setImageResource(iconRes)

            binding.navigateButton.setOnClickListener {
                onPOIAction(poi, POIAction.NAVIGATE)
            }

            binding.deleteButton.setOnClickListener {
                onPOIAction(poi, POIAction.DELETE)
            }
        }
    }

    class POIDiffCallback : DiffUtil.ItemCallback<PointOfInterest>() {
        override fun areItemsTheSame(oldItem: PointOfInterest, newItem: PointOfInterest): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PointOfInterest, newItem: PointOfInterest): Boolean {
            return oldItem == newItem
        }
    }
}

enum class POIAction {
    NAVIGATE,
    DELETE
}
