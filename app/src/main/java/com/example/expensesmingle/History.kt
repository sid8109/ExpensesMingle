package com.example.expensesmingle

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensesmingle.databinding.FragmentHistoryBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel
import kotlin.math.roundToInt

class History : Fragment() {
    private var friend: String? = null
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: ExpensesMingleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            friend = it.getString("friend")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        friend?.let { viewModel.getHistory(it) }
        binding.friendName.text = friend
        val amt = viewModel.summary.value?.getValue(friend.toString())!!
        val solution = (amt * 100.0).roundToInt() / 100.0
        binding.amount.text = "₹ $solution"
        if(amt > 0) {
            binding.friendName.setTextColor(Color.parseColor("#0BF4AA"))
            binding.amount.setTextColor(Color.parseColor("#0BF4AA"))
        } else if(amt < 0) {
            binding.friendName.setTextColor(Color.parseColor("#F40B55"))
            binding.amount.setTextColor(Color.parseColor("#F40B55"))
        }

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = HistoryAdapter(viewModel.history.value!!)

        binding.addExpense.setOnClickListener {
            findNavController().navigate(HistoryDirections.actionHistoryToAddExpense2(friend.toString()))
        }

        binding.message.setOnClickListener {
            viewModel.chatFriend = friend.toString()
            findNavController().navigate(R.id.action_history_to_message2)
        }

        viewModel.summary.observe(viewLifecycleOwner) {
            val solution = (it[friend]!! * 100.0).roundToInt() / 100.0
            binding.amount.text = "₹ $solution"
            if(it[friend]!! > 0) {
                binding.friendName.setTextColor(Color.parseColor("#0BF4AA"))
                binding.amount.setTextColor(Color.parseColor("#0BF4AA"))
            } else if(it[friend]!! < 0) {
                binding.friendName.setTextColor(Color.parseColor("#F40B55"))
                binding.amount.setTextColor(Color.parseColor("#F40B55"))
            }
        }

        viewModel.history.observe(viewLifecycleOwner) {
            binding.list.adapter = HistoryAdapter(it)
        }
    }
}

