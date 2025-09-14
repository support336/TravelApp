package com.travelapp.data.repository

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.travelapp.data.EmailData
import com.travelapp.data.TravelItem
import com.travelapp.data.TravelItemType
import com.google.api.client.util.DateTime
import java.util.*

class GoogleIntegrationRepository(private val context: Context) {

    private val calendarScopes = listOf(CalendarScopes.CALENDAR_READONLY)
    private val gmailScopes = listOf(GmailScopes.GMAIL_READONLY)
    private val allScopes = calendarScopes + gmailScopes

    private val credential = GoogleAccountCredential.usingOAuth2(
        context, allScopes
    )

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(com.google.android.gms.common.api.Scope(CalendarScopes.CALENDAR_READONLY))
            .requestScopes(com.google.android.gms.common.api.Scope(GmailScopes.GMAIL_READONLY))
            .requestServerAuthCode("163020410697-a3ekt22gdusdng1f9il23i0e7mpc7nhg.apps.googleusercontent.com")
            .build()

        GoogleSignIn.getClient(context, gso)
    }

    fun getSignInClient(): GoogleSignInClient = googleSignInClient

    fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(context) != null
    }

    suspend fun syncCalendarEvents(): List<TravelItem> {
        return try {
            val calendar = Calendar.Builder(
                NetHttpTransport(),
                GsonFactory(),
                credential
            )
                .setApplicationName("Travel App")
                .build()

            val events = calendar.events().list("primary")
                .setTimeMin(DateTime(System.currentTimeMillis()))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()

            events.items?.mapNotNull { event ->
                parseCalendarEvent(event)
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun syncGmailMessages(): List<EmailData> {
        return try {
            val gmail = Gmail.Builder(
                NetHttpTransport(),
                GsonFactory(),
                credential
            )
                .setApplicationName("Travel App")
                .build()

            val messages = gmail.users().messages().list("me")
                .setQ("travel OR flight OR hotel OR booking OR reservation")
                .execute()

            messages.messages?.mapNotNull { message ->
                val fullMessage = gmail.users().messages().get("me", message.id).execute()
                parseGmailMessage(fullMessage)
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseCalendarEvent(event: com.google.api.services.calendar.model.Event): TravelItem? {
        val title = event.summary ?: return null
        val description = event.description
        val startTime = event.start?.dateTime?.value ?: event.start?.date?.value
        val endTime = event.end?.dateTime?.value ?: event.end?.date?.value
        val location = event.location

        val type = determineTravelItemType(title, description)
        if (type == null) return null

        return TravelItem(
            id = event.id ?: UUID.randomUUID().toString(),
            tripId = "default_trip", // TODO: Group by trip
            title = title,
            description = description,
            type = type,
            startDate = startTime?.let { Date(it) },
            endDate = endTime?.let { Date(it) },
            location = location,
            address = null,
            latitude = null,
            longitude = null,
            confirmationNumber = null,
            source = "calendar"
        )
    }

    private fun parseGmailMessage(message: com.google.api.services.gmail.model.Message): EmailData? {
        val headers = message.payload?.headers ?: return null
        val subject = headers.find { it.name == "Subject" }?.value ?: return null
        val sender = headers.find { it.name == "From" }?.value ?: return null
        val date = headers.find { it.name == "Date" }?.value?.let { 
            // Parse date string - simplified
            Date()
        } ?: Date()

        val body = message.payload?.body?.data?.let { 
            // Decode base64 body
            String(android.util.Base64.decode(it, android.util.Base64.URL_SAFE))
        } ?: ""

        val isTravelRelated = isTravelRelatedEmail(subject, body)
        val extractedData = if (isTravelRelated) {
            extractTravelDataFromEmail(subject, body)
        } else null

        return EmailData(
            id = message.id ?: UUID.randomUUID().toString(),
            subject = subject,
            sender = sender,
            date = date,
            body = body,
            isTravelRelated = isTravelRelated,
            extractedData = extractedData
        )
    }

    private fun determineTravelItemType(title: String, description: String?): TravelItemType? {
        val text = "$title ${description ?: ""}".lowercase()
        return when {
            text.contains("flight") || text.contains("airline") -> TravelItemType.FLIGHT
            text.contains("hotel") || text.contains("accommodation") -> TravelItemType.HOTEL
            text.contains("restaurant") || text.contains("dining") -> TravelItemType.RESTAURANT
            text.contains("car") || text.contains("taxi") || text.contains("uber") -> TravelItemType.TRANSPORTATION
            text.contains("activity") || text.contains("tour") -> TravelItemType.ACTIVITY
            else -> null
        }
    }

    private fun isTravelRelatedEmail(subject: String, body: String): Boolean {
        val text = "$subject $body".lowercase()
        val travelKeywords = listOf(
            "flight", "hotel", "booking", "reservation", "travel", "trip",
            "airline", "airport", "check-in", "boarding pass", "confirmation"
        )
        return travelKeywords.any { text.contains(it) }
    }

    private fun extractTravelDataFromEmail(subject: String, body: String): com.travelapp.data.TravelExtractedData? {
        // Simplified extraction - in a real app, you'd use more sophisticated parsing
        val type = determineTravelItemType(subject, body) ?: return null
        
        return com.travelapp.data.TravelExtractedData(
            type = type,
            title = subject,
            description = body.take(200), // First 200 chars
            startDate = null, // Would need more sophisticated parsing
            endDate = null,
            location = null,
            address = null,
            latitude = null,
            longitude = null,
            confirmationNumber = null, // Would need regex extraction
            price = null,
            airline = null,
            hotelName = null,
            restaurantName = null
        )
    }
}
