package com.mohirdev.kutubxona.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mohirdev.kutubxona.R
import com.mohirdev.kutubxona.data.model.Book
import com.mohirdev.kutubxona.databinding.ItemBookAdminBinding

class AdminBooksAdapter(
    private var books: List<Book>,
    private val onDeleteClick: (Book) -> Unit
) : RecyclerView.Adapter<AdminBooksAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemBookAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.tvBookTitle.text = book.title
            binding.tvBookAuthor.text = book.author
            binding.tvBookGenre.text = book.genre

            val context = binding.root.context
            binding.tvBookCopies.text = context.getString(
                R.string.available_copies_format,
                book.availableCopies,
                book.totalCopies
            )

            if (book.availableCopies == 0) {
                binding.tvBookCopies.setTextColor(
                    ContextCompat.getColor(context, R.color.status_red)
                )
                binding.tvBookCopies.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.status_red_bg)
                )
            } else {
                binding.tvBookCopies.setTextColor(
                    ContextCompat.getColor(context, R.color.status_green)
                )
                binding.tvBookCopies.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.status_green_bg)
                )
            }

            binding.btnDeleteBook.setOnClickListener {
                onDeleteClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookAdminBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun updateData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
