package com.mohirdev.kutubxona.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mohirdev.kutubxona.R
import com.mohirdev.kutubxona.data.model.Book
import com.mohirdev.kutubxona.databinding.ItemBookUserBinding

class UserBooksAdapter(
    private var books: List<Book>,
    private val onBorrowClick: (Book) -> Unit
) : RecyclerView.Adapter<UserBooksAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemBookUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.tvBookTitleUser.text = book.title
            binding.tvBookAuthorUser.text = book.author
            binding.tvBookGenreUser.text = book.genre

            val context = binding.root.context
            binding.tvBookCopiesUser.text = context.getString(
                R.string.available_copies_format,
                book.availableCopies,
                book.totalCopies
            )

            if (book.availableCopies > 0) {
                binding.btnBorrow.isEnabled = true
                binding.btnBorrow.text = context.getString(R.string.btn_borrow)
                binding.tvBookCopiesUser.setTextColor(
                    ContextCompat.getColor(context, R.color.status_green)
                )
                binding.tvBookCopiesUser.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.status_green_bg)
                )
            } else {
                binding.btnBorrow.isEnabled = false
                binding.btnBorrow.text = "Tugagan"
                binding.tvBookCopiesUser.setTextColor(
                    ContextCompat.getColor(context, R.color.status_red)
                )
                binding.tvBookCopiesUser.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.status_red_bg)
                )
            }

            binding.btnBorrow.setOnClickListener {
                onBorrowClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookUserBinding.inflate(
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
