package com.mohirdev.kutubxona.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohirdev.kutubxona.data.repository.LibraryRepository
import com.mohirdev.kutubxona.databinding.FragmentAdminLoansBinding
import com.mohirdev.kutubxona.ui.adapter.LoansAdapter

class AdminLoansFragment : Fragment() {

    private var _binding: FragmentAdminLoansBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LibraryRepository
    private lateinit var adapter: LoansAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminLoansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = LibraryRepository(requireContext())

        setupRecyclerView()
        loadLoans()
    }

    override fun onResume() {
        super.onResume()
        loadLoans()
    }

    private fun setupRecyclerView() {
        adapter = LoansAdapter(emptyList(), showUser = true, showReturnButton = false)
        binding.rvAllLoans.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllLoans.adapter = adapter
    }

    private fun loadLoans() {
        val loans = repository.getAllLoans()
        adapter.updateData(loans)
        binding.tvEmptyAllLoans.visibility = if (loans.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
