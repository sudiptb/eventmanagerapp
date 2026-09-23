package com.example.eventmanager.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventmanager.data.repository.AuthRepository

class AuthViewModel(private val repo: AuthRepository = AuthRepository()) : ViewModel() {
    private val _result = MutableLiveData<Pair<Boolean, String?>>()
    val result: LiveData<Pair<Boolean, String?>> = _result
    fun login(email: String, password: String) =
        repo.login(email, password) { ok, msg -> _result.value = ok to msg }

    fun register(email: String, password: String) =
        repo.register(email, password) { ok, msg -> _result.value = ok to msg }

    fun reset(email: String) = repo.resetPassword(email) { ok, msg -> _result.value = ok to msg }
}
