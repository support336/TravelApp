package com.travelapp.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.travelapp.R
import com.travelapp.databinding.FragmentMapBinding
import com.travelapp.data.PointOfInterest
import com.travelapp.data.POICategory

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapViewModel: MapViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private var selectedLocation: LatLng? = null
    private var selectedMarker: Marker? = null
    private var poiMarkers: MutableMap<String, PointOfInterest> = mutableMapOf()
    private var isSelectedLocationMarker = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupMap()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewModel() {
        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        // Initialize repository with context
        mapViewModel.initializeRepository(requireContext())
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun setupClickListeners() {
        binding.currentLocationFab.setOnClickListener {
            getCurrentLocation()
        }

        binding.savePoiFab.setOnClickListener {
            selectedLocation?.let { location ->
                showSavePOIDialog(location)
            }
        }
        
        // Add a test button for debugging (temporary)
        binding.root.post {
            val testButton = android.widget.Button(requireContext()).apply {
                text = "TEST INFO WINDOW"
                id = android.view.View.generateViewId()
                layoutParams = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                    androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToTop = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
                    topMargin = 100
                }
                setOnClickListener {
                    testInfoWindow()
                }
            }
            (binding.root as? androidx.constraintlayout.widget.ConstraintLayout)?.addView(testButton)
            android.util.Log.d("MapFragment", "Test button added to ConstraintLayout")
        }
    }
    
    private fun testInfoWindow() {
        android.util.Log.d("MapFragment", "=== TEST INFO WINDOW ===")
        
        // Create a test marker
        val testLocation = LatLng(37.7749, -122.4194) // San Francisco
        val testMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(testLocation)
                .title("TEST MARKER")
                .snippet("This is a test marker")
        )
        
        if (testMarker != null) {
            android.util.Log.d("MapFragment", "Created test marker with ID: ${testMarker.id}")
            android.util.Log.d("MapFragment", "Showing InfoWindow for test marker")
            
            // Try multiple approaches to show InfoWindow
            testMarker.showInfoWindow()
            android.util.Log.d("MapFragment", "Test InfoWindow show command executed immediately")
            
            // Also try with delay
            binding.root.post {
                testMarker.showInfoWindow()
                android.util.Log.d("MapFragment", "Test InfoWindow show command executed with delay")
            }
            
            // Try again after a longer delay
            binding.root.postDelayed({
                testMarker.showInfoWindow()
                android.util.Log.d("MapFragment", "Test InfoWindow show command executed with longer delay")
            }, 1000)
            
            // Also try moving the camera to the marker
            googleMap?.animateCamera(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(testLocation, 15f)
            )
            
        } else {
            android.util.Log.e("MapFragment", "Failed to create test marker")
        }
        
        android.util.Log.d("MapFragment", "=== END TEST INFO WINDOW ===")
    }

    private fun observeViewModel() {
        mapViewModel.pois.observe(viewLifecycleOwner) { pois ->
            updateMapMarkers(pois)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        map.setOnMapClickListener(this)
        map.setOnMarkerClickListener(this)
        map.setInfoWindowAdapter(this)
        
        android.util.Log.d("MapFragment", "Map listeners set up successfully")
        
        // Enable POI clicks
        map.setOnPoiClickListener { poi ->
            android.util.Log.d("MapFragment", "=== POI CLICK DEBUG ===")
            android.util.Log.d("MapFragment", "POI clicked: ${poi.name}")
            android.util.Log.d("MapFragment", "POI location: ${poi.latLng.latitude}, ${poi.latLng.longitude}")
            
            // Handle POI clicks (restaurants, stores, etc.)
            val location = poi.latLng
            selectedLocation = location
            selectedMarker?.remove()
            selectedMarker = googleMap?.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(poi.name)
                    .snippet("Restaurant • Tap Save to add this location")
            )
            isSelectedLocationMarker = true
            binding.savePoiFab.visibility = View.VISIBLE
            
            android.util.Log.d("MapFragment", "Created POI marker with ID: ${selectedMarker?.id}")
            
            // Immediately show InfoWindow for POI clicks
            selectedMarker?.let { marker ->
                android.util.Log.d("MapFragment", "Showing InfoWindow for POI marker: ${marker.title}")
                marker.showInfoWindow()
            }
            
            android.util.Log.d("MapFragment", "=== END POI CLICK DEBUG ===")
        }
        
        // Enable current location button
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            getCurrentLocation()
        }
        
        // Load existing POIs
        mapViewModel.loadPOIs()
    }

    override fun onMapClick(latLng: LatLng) {
        android.util.Log.d("MapFragment", "=== MAP CLICK DEBUG ===")
        android.util.Log.d("MapFragment", "Map clicked at: ${latLng.latitude}, ${latLng.longitude}")
        
        selectedLocation = latLng
        selectedMarker?.remove()
        selectedMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Selected Location")
                .snippet("Custom Location • Tap Save to add this location")
        )
        isSelectedLocationMarker = true
        binding.savePoiFab.visibility = View.VISIBLE
        
        android.util.Log.d("MapFragment", "Created selected marker with ID: ${selectedMarker?.id}")
        
        // Immediately show InfoWindow for map clicks
        selectedMarker?.let { marker ->
            android.util.Log.d("MapFragment", "Showing InfoWindow for map marker: ${marker.title}")
            marker.showInfoWindow()
        }
        
        android.util.Log.d("MapFragment", "=== END MAP CLICK DEBUG ===")
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        android.util.Log.d("MapFragment", "=== MARKER CLICK DEBUG ===")
        android.util.Log.d("MapFragment", "Marker clicked: ${marker.title}")
        android.util.Log.d("MapFragment", "Marker ID: ${marker.id}")
        android.util.Log.d("MapFragment", "Is selected marker: ${marker == selectedMarker}")
        android.util.Log.d("MapFragment", "Is selected location marker: $isSelectedLocationMarker")
        android.util.Log.d("MapFragment", "Is in POI markers: ${poiMarkers.containsKey(marker.id)}")
        android.util.Log.d("MapFragment", "POI markers count: ${poiMarkers.size}")
        
        // Always show InfoWindow immediately for any marker
        android.util.Log.d("MapFragment", "Showing InfoWindow for marker: ${marker.title}")
        
        // Try immediate show first
        marker.showInfoWindow()
        android.util.Log.d("MapFragment", "InfoWindow show command executed immediately")
        
        // Also try with delay as backup
        binding.root.post {
            marker.showInfoWindow()
            android.util.Log.d("MapFragment", "InfoWindow show command executed with delay")
        }
        
        // Return true to consume the event
        android.util.Log.d("MapFragment", "=== END MARKER CLICK DEBUG ===")
        return true
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLocation = LatLng(it.latitude, it.longitude)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
            }
        }
    }

    private fun updateMapMarkers(pois: List<PointOfInterest>) {
        // Clear only POI markers, keep selected marker
        poiMarkers.clear()
        googleMap?.let { map ->
            // Remove only POI markers, not the selected marker
            // Note: We'll track markers manually since GoogleMap.markers is not available in older versions
        }
        
        pois.forEach { poi ->
            val location = LatLng(poi.latitude, poi.longitude)
            val marker = googleMap?.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(poi.name)
                    .snippet(poi.description ?: poi.category.name)
            )
            marker?.let { poiMarkers[it.id] = poi }
        }
    }

    private fun showSavePOIDialog(location: LatLng) {
        // Create a simple dialog for POI details
        val builder = android.app.AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_save_poi, null)
        
        builder.setView(dialogView)
            .setTitle("Save Point of Interest")
            .setPositiveButton("Save") { _, _ ->
                val name = dialogView.findViewById<android.widget.EditText>(R.id.poiNameEditText).text.toString()
                val description = dialogView.findViewById<android.widget.EditText>(R.id.poiDescriptionEditText).text.toString()
                val category = POICategory.OTHER // Default category
                
                if (name.isNotEmpty()) {
                    android.util.Log.d("MapFragment", "Saving POI: $name at ${location.latitude}, ${location.longitude}")
                    mapViewModel.savePOI(
                        name = name,
                        description = if (description.isNotEmpty()) description else "",
                        category = category,
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    binding.savePoiFab.visibility = View.GONE
                    selectedLocation = null
                    selectedMarker?.remove()
                    isSelectedLocationMarker = false
                    
                    // Show success message
                    android.widget.Toast.makeText(requireContext(), "POI saved successfully!", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    android.widget.Toast.makeText(requireContext(), "Please enter a name for the POI", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun getInfoWindow(marker: Marker): View? {
        android.util.Log.d("MapFragment", "=== INFO WINDOW DEBUG ===")
        android.util.Log.d("MapFragment", "Creating InfoWindow for marker: ${marker.title}")
        android.util.Log.d("MapFragment", "Marker ID: ${marker.id}")
        
        // Create a custom InfoWindow with POI details
        val view = layoutInflater.inflate(R.layout.infowindow_poi, null)
        
        // Set basic information
        view.findViewById<android.widget.TextView>(R.id.poiNameText).text = marker.title ?: "Unknown Location"
        view.findViewById<android.widget.TextView>(R.id.poiCategoryText).text = marker.snippet ?: "Tap Save to add this location"
        
        // For now, show placeholder data for POI details
        // In a real app, you'd fetch this from Google Places API
        view.findViewById<android.widget.TextView>(R.id.poiRatingText).text = "4.2" // Placeholder rating
        view.findViewById<android.widget.TextView>(R.id.poiHoursText).text = "Open now" // Placeholder hours
        
        // Store marker data in the view for later use
        view.tag = marker
        
        // Set up InfoWindow click handling
        view.setOnTouchListener { v, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                android.util.Log.d("MapFragment", "InfoWindow TOUCHED for ${marker.title}")
                handleInfoWindowClick(marker, event.x, event.y, view)
                true
            } else {
                false
            }
        }
        
        android.util.Log.d("MapFragment", "Created custom InfoWindow with POI details")
        return view
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
    
    private fun handleInfoWindowClick(marker: Marker, x: Float, y: Float, view: View) {
        android.util.Log.d("MapFragment", "=== INFO WINDOW CLICK HANDLER ===")
        android.util.Log.d("MapFragment", "Touch coordinates: x=$x, y=$y")
        
        // Get button views
        val directionsButton = view.findViewById<android.widget.Button>(R.id.directionsButton)
        val saveButton = view.findViewById<android.widget.Button>(R.id.saveButton)
        
        // Get button locations
        val directionsLocation = IntArray(2)
        val saveLocation = IntArray(2)
        directionsButton?.getLocationOnScreen(directionsLocation)
        saveButton?.getLocationOnScreen(saveLocation)
        
        // Convert screen coordinates to view coordinates
        val viewLocation = IntArray(2)
        view.getLocationOnScreen(viewLocation)
        
        val relativeX = x
        val relativeY = y
        
        android.util.Log.d("MapFragment", "Directions button bounds: ${directionsButton?.left}-${directionsButton?.right}, ${directionsButton?.top}-${directionsButton?.bottom}")
        android.util.Log.d("MapFragment", "Save button bounds: ${saveButton?.left}-${saveButton?.right}, ${saveButton?.top}-${saveButton?.bottom}")
        
        // Check if touch is within directions button bounds
        if (directionsButton != null && 
            relativeX >= directionsButton.left && relativeX <= directionsButton.right &&
            relativeY >= directionsButton.top && relativeY <= directionsButton.bottom) {
            
            android.util.Log.d("MapFragment", "Directions button clicked!")
            android.widget.Toast.makeText(requireContext(), "Directions clicked!", android.widget.Toast.LENGTH_SHORT).show()
            
            // Handle directions
            try {
                val location = marker.position
                val address = "${marker.title}, ${location.latitude}, ${location.longitude}"
                
                // First try: Google Maps navigation
                val navUri = "google.navigation:q=${location.latitude},${location.longitude}"
                val navIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(navUri))
                navIntent.setPackage("com.google.android.apps.maps")
                
                if (navIntent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(navIntent)
                    android.util.Log.d("MapFragment", "Opened Google Maps navigation")
                } else {
                    // Fallback: Generic maps intent
                    val mapsUri = "geo:${location.latitude},${location.longitude}?q=${android.net.Uri.encode(address)}"
                    val mapsIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(mapsUri))
                    startActivity(mapsIntent)
                    android.util.Log.d("MapFragment", "Opened generic maps")
                }
            } catch (e: Exception) {
                android.util.Log.e("MapFragment", "Error opening directions", e)
                android.widget.Toast.makeText(requireContext(), "Could not open directions", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        // Check if touch is within save button bounds
        else if (saveButton != null && 
                 relativeX >= saveButton.left && relativeX <= saveButton.right &&
                 relativeY >= saveButton.top && relativeY <= saveButton.bottom) {
            
            android.util.Log.d("MapFragment", "Save button clicked!")
            android.widget.Toast.makeText(requireContext(), "Save clicked!", android.widget.Toast.LENGTH_SHORT).show()
            
            selectedLocation = marker.position
            selectedMarker = marker
            isSelectedLocationMarker = true
            binding.savePoiFab.visibility = View.VISIBLE
        }
        else {
            android.util.Log.d("MapFragment", "InfoWindow clicked but not on a button")
        }
        
        android.util.Log.d("MapFragment", "=== END INFO WINDOW CLICK HANDLER ===")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
