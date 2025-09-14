# Travel App

A comprehensive Android travel planning application that integrates with Google Calendar and Gmail for automatic data population, interactive Google Maps for Points of Interest (POI) management, and a structured UI with four main tabs.

## Features

### 📅 Agenda Tab
- Google Sign-In integration
- Google Calendar sync for travel events
- Gmail integration for parsing travel confirmations
- Manual travel item creation
- Trip grouping and organization

### 🗺️ Map Tab
- Interactive Google Maps
- POI selection and saving
- Custom InfoWindow with detailed POI information
- Directions integration
- Current location display

### 📍 Points Tab
- List of saved Points of Interest
- POI management (add, delete, navigate)
- Integration with Map tab

### ⚙️ Settings Tab
- Google integration status
- Sync controls
- App configuration

## Technical Stack

- **Platform**: Android (Kotlin)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Persistence Library
- **Maps**: Google Maps SDK for Android
- **Authentication**: Google Sign-In API
- **APIs**: Google Calendar API, Gmail API
- **UI**: Material Design 3, Bottom Navigation

## Project Structure

```
app/
├── src/main/
│   ├── java/com/travelapp/
│   │   ├── data/                    # Data layer (entities, DAOs, repositories)
│   │   ├── ui/                      # UI layer (fragments, viewmodels, adapters)
│   │   │   ├── agenda/              # Agenda tab components
│   │   │   ├── map/                 # Map tab components
│   │   │   ├── points/              # Points tab components
│   │   │   └── settings/            # Settings tab components
│   │   └── TravelApplication.kt     # Application class
│   ├── res/                         # Resources (layouts, drawables, values)
│   └── AndroidManifest.xml
├── build.gradle                     # App-level build configuration
└── google-services.json            # Google services configuration
```

## Key Data Models

- **Trip**: Groups related travel items
- **TravelItem**: Individual travel events (flights, hotels, restaurants)
- **PointOfInterest**: Saved map locations with details
- **EmailData**: Parsed travel confirmation emails

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Travel
   ```

2. **Open in Android Studio**
   - Import the project
   - Sync Gradle files

3. **Configure Google Services**
   - Replace `google-services.json` with your own configuration
   - Update API keys in `res/values/google_maps_key.xml`

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

## API Keys Required

- Google Maps API Key
- Google OAuth Client ID
- Google Calendar API (enabled)
- Gmail API (enabled)

## Current Status

- ✅ Basic app structure and navigation
- ✅ Google Maps integration with custom InfoWindow
- ✅ POI selection and display
- ✅ In-memory POI storage
- 🔄 Google Sign-In integration (in progress)
- 🔄 Calendar and Gmail API integration (planned)
- 🔄 Room database implementation (planned)

## Development Notes

- Uses in-memory storage (`POIStorage`) for POI management
- Custom InfoWindow with touch-based button handling
- Material Design 3 components
- Comprehensive logging for debugging

## License

This project is for educational and development purposes.