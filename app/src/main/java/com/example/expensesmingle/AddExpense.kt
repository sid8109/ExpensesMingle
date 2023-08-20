package com.example.expensesmingle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.expensesmingle.databinding.FragmentAddExpenseBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel

class AddExpense : Fragment() {
    private lateinit var binding : FragmentAddExpenseBinding
    private val viewModel: ExpensesMingleViewModel by activityViewModels()
    private var friend: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            friend = it.getString("friend").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExpenseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(friend != null) {
            binding.autoCompleteTextView.setText(friend.toString())
        }

        if(viewModel.friends.value!!.isEmpty()) {
            viewModel.getFriends()
        }
        viewModel.friends.observe(viewLifecycleOwner){
            val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.name,  viewModel.friends.value!!)
            binding.autoCompleteTextView.setAdapter(arrayAdapter)
        }

        binding.iPaid.setOnClickListener {
            if(!binding.autoCompleteTextView.text.isNullOrEmpty() && !binding.valueEditText.text.isNullOrEmpty() && !binding.reasonEditText.text.isNullOrEmpty()) {
                val whoPaid = binding.autoCompleteTextView.text.toString()
                val amt = binding.valueEditText.text.toString().toDouble()
                val reason = binding.reasonEditText.text.toString()
                val time = System.currentTimeMillis()
                viewModel.addTrans(whoPaid, amt, reason, true, time)
                Toast.makeText(requireContext(), "Transaction added successfully!", Toast.LENGTH_SHORT).show()
                activity?.onBackPressedDispatcher?.onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Enter all fields!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.friendPaid.setOnClickListener {
            if(!binding.autoCompleteTextView.text.isNullOrEmpty() && !binding.valueEditText.text.isNullOrEmpty() && !binding.reasonEditText.text.isNullOrEmpty()) {
                val whoPaid = binding.autoCompleteTextView.text.toString()
                val amt = binding.valueEditText.text.toString().toDouble()
                val reason = binding.reasonEditText.text.toString()
                val time = System.currentTimeMillis()
                viewModel.addTrans(whoPaid, amt, reason, false, time)
                Toast.makeText(requireContext(), "Transaction added successfully!", Toast.LENGTH_SHORT).show()
                activity?.onBackPressedDispatcher?.onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Enter all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}