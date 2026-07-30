package com.mohirdev.kutubxona.ui.admin

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohirdev.kutubxona.data.repository.LibraryRepository
import com.mohirdev.kutubxona.databinding.ActivityUserDetailsBinding
import com.mohirdev.kutubxona.ui.adapter.LoansAdapter

class UserDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }

    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var repository: LibraryRepository
    private lateinit var adapter: LoansAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = LibraryRepository(this)

        setSupportActionBar(binding.toolbarUserDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userId = intent.getStringExtra(EXTRA_USER_ID) ?: return finish()
        val user = repository.getUserById(userId) ?: return finish()

        binding.tvUserName.text = user.fullName
        binding.tvUserUsername.text = "@${user.username}"
        binding.tvUserRegDate.text = "Ro'yxatdan o'tgan: ${user.registeredDate}"

        setupRecyclerView(userId)
    }

    private fun setupRecyclerView(userId: String) {
        val loans = repository.getLoansForUser(userId)
        adapter = LoansAdapter(loans, showUser = false, showReturnButton = false)
        binding.rvUserLoans.layoutManager = LinearLayoutManager(this)
        binding.rvUserLoans.adapter = adapter

        binding.tvEmptyLoans.visibility = if (loans.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
