package com.example.eventmanager.data.repository

import com.example.eventmanager.data.model.Event
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class EventRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun eventsRef() =
        db.collection("users").document(auth.currentUser!!.uid).collection("events")

    fun observeEvents(onChange: (List<Event>) -> Unit, onError: (String) -> Unit) =
        eventsRef().orderBy("dateTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onError(error.message ?: "Unable to load events"); return@addSnapshotListener
                }
                val events = snapshot?.documents?.map { d ->
                    Event(
                        d.id,
                        d.getString("title").orEmpty(),
                        d.getString("description").orEmpty(),
                        d.getTimestamp("dateTime"),
                        d.getString("location").orEmpty()
                    )
                }.orEmpty()
                onChange(events)
            }

    fun add(event: Event, onResult: (Boolean, String?) -> Unit) {
        val data = hashMapOf(
            "title" to event.title,
            "description" to event.description,
            "dateTime" to event.dateTime,
            "location" to event.location
        )
        eventsRef().add(data)
            .addOnCompleteListener { onResult(it.isSuccessful, it.exception?.message) }
    }

    fun update(event: Event, onResult: (Boolean, String?) -> Unit) {
        val data = hashMapOf(
            "title" to event.title,
            "description" to event.description,
            "dateTime" to event.dateTime,
            "location" to event.location
        )
        eventsRef().document(event.id).set(data)
            .addOnCompleteListener { onResult(it.isSuccessful, it.exception?.message) }
    }

    fun delete(id: String, onResult: (Boolean, String?) -> Unit) =
        eventsRef().document(id).delete()
            .addOnCompleteListener { onResult(it.isSuccessful, it.exception?.message) }
}
