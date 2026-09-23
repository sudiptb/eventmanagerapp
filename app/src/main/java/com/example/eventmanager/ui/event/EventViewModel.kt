package com.example.eventmanager.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventmanager.data.model.Event
import com.example.eventmanager.data.repository.EventRepository

class EventViewModel(private val repo: EventRepository = EventRepository()) : ViewModel() {
    private val _events = MutableLiveData<List<Event>>(emptyList())
    val events: LiveData<List<Event>> = _events
    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message
    private var registration: com.google.firebase.firestore.ListenerRegistration? = null

    fun startListening() {
        registration?.remove()
        registration = repo.observeEvents({ _events.postValue(it) }, { _message.postValue(it) })
    }

    fun add(event: Event, done: (Boolean) -> Unit) =
        repo.add(event) { ok, msg -> if (!ok) _message.postValue(msg); done(ok) }

    fun update(event: Event, done: (Boolean) -> Unit) =
        repo.update(event) { ok, msg -> if (!ok) _message.postValue(msg); done(ok) }

    fun delete(id: String, done: (Boolean) -> Unit) =
        repo.delete(id) { ok, msg -> if (!ok) _message.postValue(msg); done(ok) }

    override fun onCleared() {
        registration?.remove(); super.onCleared()
    }
}
