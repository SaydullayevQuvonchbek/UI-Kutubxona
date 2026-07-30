package com.mohirdev.kutubxona.ui.user

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohirdev.kutubxona.R
import com.mohirdev.kutubxona.data.model.Book
import com.mohirdev.kutubxona.data.repository.LibraryRepository
import com.mohirdev.kutubxona.databinding.FragmentUserLibraryBinding
import com.mohirdev.kutubxona.ui.adapter.UserBooksAdapter

class UserLibraryFragment : Fragment() {

    private var _binding: FragmentUserLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LibraryRepository
    private lateinit var adapter: UserBooksAdapter
    private var allBooks = listOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = LibraryRepository(requireContext())

        setupRecyclerView()
        setupSearch()
        loadBooks()
    }

    override fun onResume() {
        super.onResume()
        loadBooks()
    }

    private fun setupRecyclerView() {
        adapter = UserBooksAdapter(emptyList()) { book ->
            showBorrowDialog(book)
        }
        binding.rvUserLibrary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUserLibrary.adapter = adapter
    }

    private fun loadBooks() {
        allBooks = repository.getBooks()
        filterBooks(binding.etSearchUserBooks.text.toString())
    }

    private fun filterBooks(query: String) {
        val filtered = if (query.isEmpty()) {
            allBooks
        } else {
            allBooks.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.author.contains(query, ignoreCase = true) ||
                        it.genre.contains(query, ignoreCase = true)
            }
        }
        adapter.updateData(filtered)
        binding.tvEmptyUserLibrary.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupSearch() {
        binding.etSearchUserBooks.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterBooks(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showBorrowDialog(book: Book) {
        val currentUser = repository.getCurrentUser() ?: return

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_borrow_book, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.tvBorrowBookName).text = "${book.title} - ${book.author}"

        dialogView.findViewById<View>(R.id.btnCancelBorrow).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.btnConfirmBorrow).setOnClickListener {
            val rg = dialogView.findViewById<RadioGroup>(R.id.rgBorrowDays)
            val selectedId = rg.checkedRadioButtonId

            val days = when (selectedId) {
                R.id.rb7Days -> 7
                R.id.rb30Days -> 30
                else -> 14
            }

            val (success, message) = repository.borrowBook(book.id, currentUser.id, days)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (success) {
                dialog.dismiss()
                loadBooks()
            }
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
