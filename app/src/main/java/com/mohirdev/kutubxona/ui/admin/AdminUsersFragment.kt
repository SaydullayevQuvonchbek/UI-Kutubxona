package com.mohirdev.kutubxona.ui.admin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohirdev.kutubxona.data.model.User
import com.mohirdev.kutubxona.data.repository.LibraryRepository
import com.mohirdev.kutubxona.databinding.FragmentAdminUsersBinding
import com.mohirdev.kutubxona.ui.adapter.AdminUsersAdapter

class AdminUsersFragment : Fragment() {

    private var _binding: FragmentAdminUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LibraryRepository
    private lateinit var adapter: AdminUsersAdapter
    private var allUsers = listOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = LibraryRepository(requireContext())

        setupRecyclerView()
        setupSearch()
        loadUsers()
    }

    override fun onResume() {
        super.onResume()
        loadUsers()
    }

    private fun setupRecyclerView() {
        adapter = AdminUsersAdapter(
            emptyList(),
            getActiveLoansCount = { userId ->
                repository.getLoansForUser(userId).count { !it.isReturned }
            },
            onUserClick = { user ->
                val intent = Intent(requireContext(), UserDetailsActivity::class.java)
                intent.putExtra(UserDetailsActivity.EXTRA_USER_ID, user.id)
                startActivity(intent)
            }
        )
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter
    }

    private fun loadUsers() {
        allUsers = repository.getUsers()
        filterUsers(binding.etSearchUsers.text.toString())
    }

    private fun filterUsers(query: String) {
        val filtered = if (query.isEmpty()) {
            allUsers
        } else {
            allUsers.filter {
                it.fullName.contains(query, ignoreCase = true) ||
                        it.username.contains(query, ignoreCase = true)
            }
        }
        adapter.updateData(filtered)
        binding.tvEmptyUsers.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupSearch() {
        binding.etSearchUsers.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterUsers(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
