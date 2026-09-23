package com.example.eventmanager.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eventmanager.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var b: ActivityForgotPasswordBinding
    private val vm: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b =
            ActivityForgotPasswordBinding.inflate(layoutInflater); setContentView(b.root)
        b.btnReset.setOnClickListener {
            val e = b.etEmail.text.toString()
                .trim(); if (e.isBlank()) toast("Enter your email") else vm.reset(e)
        }
        vm.result.observe(this) { (ok, msg) ->
            if (ok) {
                toast("Password reset email sent"); finish()
            } else toast(msg ?: "Unable to send reset email")
        }
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}
