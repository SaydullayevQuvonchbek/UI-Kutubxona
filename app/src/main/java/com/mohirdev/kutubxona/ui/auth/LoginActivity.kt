package com.mohirdev.kutubxona.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mohirdev.kutubxona.data.model.Role
import com.mohirdev.kutubxona.data.repository.LibraryRepository
import com.mohirdev.kutubxona.databinding.ActivityLoginBinding
import com.mohirdev.kutubxona.ui.admin.AdminMainActivity
import com.mohirdev.kutubxona.ui.user.UserMainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var repository: LibraryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = LibraryRepository(this)

        val currentUser = repository.getCurrentUser()
        if (currentUser != null) {
            navigateToMain(currentUser.role)
            return
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Iltimos, barcha maydonlarni to'ldiring!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = repository.login(username, password)
            if (user != null) {
                Toast.makeText(this, "Xush kelibsiz, ${user.fullName}!", Toast.LENGTH_SHORT).show()
                navigateToMain(user.role)
            } else {
                Toast.makeText(this, "Foydalanuvchi nomi yoki parol xato!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnDemoAdmin.setOnClickListener {
            binding.etUsername.setText("admin")
            binding.etPassword.setText("admin")
            binding.btnLogin.performClick()
        }

        binding.btnDemoUser.setOnClickListener {
            binding.etUsername.setText("user")
            binding.etPassword.setText("user")
            binding.btnLogin.performClick()
        }
    }

    private fun navigateToMain(role: Role) {
        val intent = if (role == Role.ADMIN) {
            Intent(this, AdminMainActivity::class.java)
        } else {
            Intent(this, UserMainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
