package com.example.eventmanager.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eventmanager.databinding.ActivityRegisterBinding
import com.example.eventmanager.ui.dashboard.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var b: ActivityRegisterBinding
    private val vm: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b =
            ActivityRegisterBinding.inflate(layoutInflater); setContentView(b.root)
        b.btnRegister.setOnClickListener {
            val email = b.etEmail.text.toString().trim();
            val p = b.etPassword.text.toString();
            val c = b.etConfirmPassword.text.toString(); when {
            email.isBlank() -> toast("Email is required"); p.length < 6 -> toast("Password must be at least 6 characters"); p != c -> toast(
                "Passwords do not match"
            ); else -> vm.register(email, p)
        }
        }
        vm.result.observe(this) { (ok, msg) ->
            if (ok) {
                startActivity(Intent(this, MainActivity::class.java)); finishAffinity()
            } else toast(msg ?: "Registration failed")
        }
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}
