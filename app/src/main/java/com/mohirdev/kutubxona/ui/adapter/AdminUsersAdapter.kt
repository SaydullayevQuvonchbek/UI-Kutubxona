package com.mohirdev.kutubxona.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mohirdev.kutubxona.R
import com.mohirdev.kutubxona.data.model.Role
import com.mohirdev.kutubxona.data.model.User
import com.mohirdev.kutubxona.databinding.ItemUserAdminBinding

class AdminUsersAdapter(
    private var users: List<User>,
    private val getActiveLoansCount: (String) -> Int,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<AdminUsersAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvUserFullName.text = user.fullName
            binding.tvUserUsernameBadge.text = "@${user.username}"

            if (user.role == Role.ADMIN) {
                binding.tvUserRoleBadge.text = "Admin"
                binding.tvUserRoleBadge.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.secondary)
                )
            } else {
                binding.tvUserRoleBadge.text = "Foydalanuvchi"
                binding.tvUserRoleBadge.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.primary)
                )
            }

            val activeCount = getActiveLoansCount(user.id)
            if (activeCount > 0) {
                binding.tvLoansCountBadge.text = "$activeCount ta kitob ijarada"
                binding.tvLoansCountBadge.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.status_green)
                )
                binding.tvLoansCountBadge.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.status_green_bg)
                )
            } else {
                binding.tvLoansCountBadge.text = "Ijara kitob yo'q"
                binding.tvLoansCountBadge.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.text_secondary)
                )
                binding.tvLoansCountBadge.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.divider)
                )
            }

            binding.cardUserAdmin.setOnClickListener {
                onUserClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserAdminBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateData(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
