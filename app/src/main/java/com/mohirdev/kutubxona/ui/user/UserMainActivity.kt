package com.mohirdev.kutubxona.ui.user

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
import com.mohirdev.kutubxona.databinding.ActivityUserMainBinding
import com.mohirdev.kutubxona.ui.auth.LoginActivity

class UserMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserMainBinding
    private lateinit var repository: LibraryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = LibraryRepository(this)

        setSupportActionBar(binding.toolbarUser)
        val currentUser = repository.getCurrentUser()
        if (currentUser != null) {
            supportActionBar?.subtitle = currentUser.fullName
        }

        setupViewPagerAndNav()
    }

    private fun setupViewPagerAndNav() {
        val adapter = UserPagerAdapter(this)
        binding.viewPagerUser.adapter = adapter
        binding.viewPagerUser.offscreenPageLimit = 2

        binding.bottomNavUser.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_library -> binding.viewPagerUser.currentItem = 0
                R.id.nav_my_books -> binding.viewPagerUser.currentItem = 1
            }
            true
        }

        binding.viewPagerUser.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.bottomNavUser.selectedItemId = R.id.nav_library
                    1 -> binding.bottomNavUser.selectedItemId = R.id.nav_my_books
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

    private inner class UserPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> UserLibraryFragment()
                1 -> UserMyBooksFragment()
                else -> UserLibraryFragment()
            }
        }
    }
}
