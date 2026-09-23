package com.example.eventmanager.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eventmanager.databinding.ActivityLoginBinding
import com.example.eventmanager.ui.dashboard.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding
    private val vm: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); b = ActivityLoginBinding.inflate(layoutInflater); setContentView(b.root)
        if (com.google.firebase.auth.FirebaseAuth.getInstance().currentUser != null) { openMain(); return }
        b.btnLogin.setOnClickListener { val email=b.etEmail.text.toString().trim(); val pass=b.etPassword.text.toString(); if(email.isBlank()||pass.isBlank()){toast("Enter email and password");return@setOnClickListener}; vm.login(email,pass) }
        b.tvRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        b.tvForgot.setOnClickListener { startActivity(Intent(this, ForgotPasswordActivity::class.java)) }
        vm.result.observe(this) { (ok,msg) -> if(ok) openMain() else toast(firebaseMessage(msg)) }
    }
    private fun openMain(){ startActivity(Intent(this, MainActivity::class.java)); finish() }
    private fun firebaseMessage(m:String?) = m ?: "Authentication failed"
    private fun toast(s:String){ Toast.makeText(this,s,Toast.LENGTH_LONG).show() }
}
