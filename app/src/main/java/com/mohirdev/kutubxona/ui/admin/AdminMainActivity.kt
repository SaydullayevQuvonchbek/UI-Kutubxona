package com.mohirdev.kutubxona.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.mohirdev.kutubxona.R
import com.mohirdev.kutubxona.data.repository.LibraryRepository
import com.mohirdev.kutubxona.databinding.ActivityAdminMainBinding
import com.mohirdev.kutubxona.ui.auth.LoginActivity

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var repository: LibraryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = LibraryRepository(this)

        setSupportActionBar(binding.toolbarAdmin)

        setupViewPagerAndNav()
    }

    private fun setupViewPagerAndNav() {
        val adapter = AdminPagerAdapter(this)
        binding.viewPagerAdmin.adapter = adapter
        binding.viewPagerAdmin.offscreenPageLimit = 3

        binding.bottomNavAdmin.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_books -> binding.viewPagerAdmin.currentItem = 0
                R.id.nav_users -> binding.viewPagerAdmin.currentItem = 1
                R.id.nav_loans -> binding.viewPagerAdmin.currentItem = 2
            }
            true
        }

        binding.viewPagerAdmin.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.bottomNavAdmin.selectedItemId = R.id.nav_books
                    1 -> binding.bottomNavAdmin.selectedItemId = R.id.nav_users
                    2 -> binding.bottomNavAdmin.selectedItemId = R.id.nav_loans
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 1001, 0, getString(R.string.logout))?.apply {
            setIcon(R.drawable.ic_logout)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1001) {
            confirmLogout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Chiqish")
            .setMessage("Haqiqatan ham hisobdan chiqmoqchimisiz?")
            .setPositiveButton("Ha") { _, _ ->
                repository.setCurrentUser(null)
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Yo'q", null)
            .show()
    }

    private inner class AdminPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AdminBooksFragment()
                1 -> AdminUsersFragment()
                2 -> AdminLoansFragment()
                else -> AdminBooksFragment()
            }
        }
    }
}
