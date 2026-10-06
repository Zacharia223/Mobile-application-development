package com.example.tenantmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.tenantmanagement.databinding.ActivityLoginactivityBinding

class Loginactivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve pre-filled email from RegisterActivity if available
        val emailExtra = intent.getStringExtra("EMAIL")
        if (!emailExtra.isNullOrEmpty()) {
            binding.emailEditText.setText(emailExtra)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Successfully logged in -> Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.helpTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://www.google.com".toUri())
            startActivity(intent)
        }
    }
}
