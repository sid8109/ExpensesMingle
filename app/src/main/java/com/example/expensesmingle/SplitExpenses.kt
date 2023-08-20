package com.example.expensesmingle

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensesmingle.databinding.FragmentSplitExpensesBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel

class SplitExpenses : Fragment() {
    private lateinit var binding: FragmentSplitExpensesBinding
    private val viewModel: ExpensesMingleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplitExpensesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.friends.layoutManager = LinearLayoutManager(context)
        binding.friends.adapter = CheckBoxAdapter(viewModel.friends.value!!, viewModel)
        binding.addTransaction.setOnClickListener {
            if(check()) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Add Amount You Paid")
                val viewInflated = layoutInflater.inflate(R.layout.amount, view as ViewGroup, false)
                val input = viewInflated.findViewById<EditText>(R.id.amount_paid)
                builder.setView(viewInflated)
                builder.setPositiveButton("Add") { dialog, _ ->
                    if (input.text.isNullOrEmpty()) {
                        return@setPositiveButton
                    }
                    viewModel.calculate(input.text.toString().toDouble(), binding.reasonEditText.text.toString())
                    Toast.makeText(
                        requireContext(),
                        "Transaction added successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                builder.show()
            }
        }
    }

    private fun check(): Boolean {
        if (viewModel.spentOn.value!!.isEmpty()) {
            Toast.makeText(requireContext(), "Select friends!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.reasonEditText.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Enter a reason!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}