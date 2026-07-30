package com.mohirdev.kutubxona.ui.admin

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.mohirdev.kutubxona.R
import com.mohirdev.kutubxona.data.model.Book
import com.mohirdev.kutubxona.data.repository.LibraryRepository
import com.mohirdev.kutubxona.databinding.FragmentAdminBooksBinding
import com.mohirdev.kutubxona.ui.adapter.AdminBooksAdapter

class AdminBooksFragment : Fragment() {

    private var _binding: FragmentAdminBooksBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LibraryRepository
    private lateinit var adapter: AdminBooksAdapter
    private var allBooks = listOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = LibraryRepository(requireContext())

        setupRecyclerView()
        setupSearch()
        setupAddFab()
        loadBooks()
    }

    override fun onResume() {
        super.onResume()
        loadBooks()
    }

    private fun setupRecyclerView() {
        adapter = AdminBooksAdapter(emptyList()) { book ->
            confirmDeleteBook(book)
        }
        binding.rvBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBooks.adapter = adapter
    }

    private fun loadBooks() {
        allBooks = repository.getBooks()
        filterBooks(binding.etSearchBooks.text.toString())
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
        binding.tvEmptyBooks.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupSearch() {
        binding.etSearchBooks.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterBooks(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun confirmDeleteBook(book: Book) {
        AlertDialog.Builder(requireContext())
            .setTitle("Kitobni o'chirish")
            .setMessage("'${book.title}' kitobini ro'yxatdan o'chirmoqchimisiz?")
            .setPositiveButton("O'chirish") { _, _ ->
                repository.deleteBook(book.id)
                Toast.makeText(requireContext(), "Kitob o'chirildi", Toast.LENGTH_SHORT).show()
                loadBooks()
            }
            .setNegativeButton("Bekor qilish", null)
            .show()
    }

    private fun setupAddFab() {
        binding.fabAddBook.setOnClickListener {
            showAddBookDialog()
        }
    }

    private fun showAddBookDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_book, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val etTitle = dialogView.findViewById<TextInputEditText>(R.id.etBookTitle)
        val etAuthor = dialogView.findViewById<TextInputEditText>(R.id.etBookAuthor)
        val etGenre = dialogView.findViewById<TextInputEditText>(R.id.etBookGenre)
        val etCopies = dialogView.findViewById<TextInputEditText>(R.id.etBookCopies)
        val etDesc = dialogView.findViewById<TextInputEditText>(R.id.etBookDesc)

        dialogView.findViewById<View>(R.id.btnCancelBook).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.btnSaveBook).setOnClickListener {
            val title = etTitle.text.toString().trim()
            val author = etAuthor.text.toString().trim()
            val genre = etGenre.text.toString().trim()
            val copiesStr = etCopies.text.toString().trim()
            val desc = etDesc.text.toString().trim()

            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || copiesStr.isEmpty()) {
                Toast.makeText(requireContext(), "Iltimos, asosiy maydonlarni to'ldiring!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val copies = copiesStr.toIntOrNull() ?: 1
            repository.addBook(title, author, genre, copies, desc)
            Toast.makeText(requireContext(), "Yangi kitob qo'shildi!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            loadBooks()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
