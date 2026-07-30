package com.mohirdev.kutubxona.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mohirdev.kutubxona.R
import com.mohirdev.kutubxona.data.model.BookLoan
import com.mohirdev.kutubxona.databinding.ItemBookLoanBinding

class LoansAdapter(
    private var loans: List<BookLoan>,
    private val showUser: Boolean = false,
    private val showReturnButton: Boolean = false,
    private val onReturnClick: ((BookLoan) -> Unit)? = null
) : RecyclerView.Adapter<LoansAdapter.LoanViewHolder>() {

    inner class LoanViewHolder(val binding: ItemBookLoanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loan: BookLoan) {
            binding.tvLoanBookTitle.text = loan.bookTitle

            if (showUser) {
                binding.tvLoanBookAuthor.text = "${loan.userFullName} • ${loan.bookAuthor}"
            } else {
                binding.tvLoanBookAuthor.text = loan.bookAuthor
            }

            val context = binding.root.context
            binding.tvLoanBorrowDate.text = "${context.getString(R.string.borrowed_date_label)} ${loan.borrowDate}"
            binding.tvLoanDueDate.text = "${context.getString(R.string.due_date_label)} ${loan.dueDate}"

            if (loan.isReturned) {
                binding.tvLoanStatusBadge.text = context.getString(R.string.status_returned)
                binding.tvLoanStatusBadge.setTextColor(
                    ContextCompat.getColor(context, R.color.text_secondary)
                )
                binding.tvLoanStatusBadge.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.divider)
                )
                binding.btnReturnBook.visibility = View.GONE
            } else if (loan.isOverdue()) {
                binding.tvLoanStatusBadge.text = context.getString(R.string.status_overdue)
                binding.tvLoanStatusBadge.setTextColor(
                    ContextCompat.getColor(context, R.color.status_red)
                )
                binding.tvLoanStatusBadge.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.status_red_bg)
                )
                binding.btnReturnBook.visibility = if (showReturnButton) View.VISIBLE else View.GONE
            } else {
                binding.tvLoanStatusBadge.text = context.getString(R.string.status_on_time)
                binding.tvLoanStatusBadge.setTextColor(
                    ContextCompat.getColor(context, R.color.status_green)
                )
                binding.tvLoanStatusBadge.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.status_green_bg)
                )
                binding.btnReturnBook.visibility = if (showReturnButton) View.VISIBLE else View.GONE
            }

            binding.btnReturnBook.setOnClickListener {
                onReturnClick?.invoke(loan)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val binding = ItemBookLoanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LoanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        holder.bind(loans[position])
    }

    override fun getItemCount(): Int = loans.size

    fun updateData(newLoans: List<BookLoan>) {
        loans = newLoans
        notifyDataSetChanged()
    }
}
