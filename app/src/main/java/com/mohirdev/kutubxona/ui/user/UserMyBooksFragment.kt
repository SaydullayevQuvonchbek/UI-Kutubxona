package com.mohirdev.kutubxona.ui.user

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohirdev.kutubxona.data.model.BookLoan
import com.mohirdev.kutubxona.data.repository.LibraryRepository
import com.mohirdev.kutubxona.databinding.FragmentUserMyBooksBinding
import com.mohirdev.kutubxona.ui.adapter.LoansAdapter

class UserMyBooksFragment : Fragment() {

    private var _binding: FragmentUserMyBooksBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LibraryRepository
    private lateinit var adapter: LoansAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserMyBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = LibraryRepository(requireContext())

        setupRecyclerView()
        loadMyLoans()
    }

    override fun onResume() {
        super.onResume()
        loadMyLoans()
    }

    private fun setupRecyclerView() {
        adapter = LoansAdapter(
            emptyList(),
            showUser = false,
            showReturnButton = true,
            onReturnClick = { loan ->
                confirmReturnBook(loan)
            }
        )
        binding.rvMyBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMyBooks.adapter = adapter
    }

    private fun confirmReturnBook(loan: BookLoan) {
        AlertDialog.Builder(requireContext())
            .setTitle("Kitobni topshirish")
            .setMessage("'${loan.bookTitle}' kitobini kutubxonaga topshirasizmi?")
            .setPositiveButton("Topshirish") { _, _ ->
                val (success, message) = repository.returnBook(loan.id)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                if (success) {
                    loadMyLoans()
                }
            }
            .setNegativeButton("Bekor qilish", null)
            .show()
    }

    private fun loadMyLoans() {
        val currentUser = repository.getCurrentUser() ?: return
        val loans = repository.getLoansForUser(currentUser.id)
        adapter.updateData(loans)
        binding.tvEmptyMyBooks.visibility = if (loans.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
