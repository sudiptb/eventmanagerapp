package com.example.eventmanager.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) =
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            onResult(task.isSuccessful, task.exception?.message)
        }

    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) =
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            onResult(task.isSuccessful, task.exception?.message)
        }

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) =
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            onResult(task.isSuccessful, task.exception?.message)
        }

    fun currentUserExists() = auth.currentUser != null
    fun logout() = auth.signOut()
}
