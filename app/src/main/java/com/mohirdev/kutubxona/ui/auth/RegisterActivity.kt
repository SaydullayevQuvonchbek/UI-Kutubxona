package com.mohirdev.kutubxona.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mohirdev.kutubxona.data.model.Role
import com.mohirdev.kutubxona.data.repository.LibraryRepository
import com.mohirdev.kutubxona.databinding.ActivityRegisterBinding
import com.mohirdev.kutubxona.ui.admin.AdminMainActivity
import com.mohirdev.kutubxona.ui.user.UserMainActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var repository: LibraryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = LibraryRepository(this)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegisterSubmit.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Iltimos, barcha maydonlarni to'ldiring!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val role = if (binding.rbAdmin.isChecked) Role.ADMIN else Role.USER

            val (success, message) = repository.register(fullName, username, password, role)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (success) {
                navigateToMain(role)
            }
        }

        binding.btnLoginLink.setOnClickListener {
            finish()
        }
    }

    private fun navigateToMain(role: Role) {
        val intent = if (role == Role.ADMIN) {
            Intent(this, AdminMainActivity::class.java)
        } else {
            Intent(this, UserMainActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
