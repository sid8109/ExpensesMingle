package com.example.expensesmingle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensesmingle.databinding.FragmentNetTransactionsBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel


class NetTransactions : Fragment() {
    private lateinit var binding: FragmentNetTransactionsBinding
    private val viewModel: ExpensesMingleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFriends()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNetTransactionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTransactions()
        binding.summary.layoutManager = LinearLayoutManager(context)
        binding.summary.adapter = viewModel.summary.value?.let {
            SummaryAdapter(it.toList()) { name ->
                findNavController().navigate(
                    NetTransactionsDirections.actionNetTransactionsToHistory(
                        name
                    )
                )
            }
        }
        viewModel.summary.observe(viewLifecycleOwner) {
            binding.summary.adapter = viewModel.summary.value?.let {
                SummaryAdapter(it.toList()) { name ->
                    findNavController().navigate(
                        NetTransactionsDirections.actionNetTransactionsToHistory(
                            name
                        )
                    )
                }
            }
        }

        binding.addExpense.setOnClickListener {
            findNavController().navigate(
                NetTransactionsDirections.actionNetTransactionsToAddExpense2(
                    ""
                )
            )
        }
        binding.friends.setOnClickListener {
            findNavController().navigate(R.id.action_netTransactions_to_friends2)
        }
        binding.splitExpenses.setOnClickListener {
            if (viewModel.friends.value!!.isEmpty()) {
                Toast.makeText(requireContext(), "Add friends first!", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_netTransactions_to_splitExpenses)
            }
        }
    }
}