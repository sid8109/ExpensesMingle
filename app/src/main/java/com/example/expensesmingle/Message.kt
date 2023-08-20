package com.example.expensesmingle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensesmingle.databinding.FragmentMessageBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel


class Message : Fragment() {
    private val viewModel: ExpensesMingleViewModel by activityViewModels()
    private lateinit var binding: FragmentMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getChats()
        binding.chats.layoutManager = LinearLayoutManager(context)
        if(viewModel.chats.value!!.isNotEmpty()) {
            binding.chats.smoothScrollToPosition(viewModel.chats.value!!.size - 1)
        }
        binding.chats.adapter = viewModel.chats.value?.let {
            MessageAdapter(requireContext(), it.toList())
        }
        viewModel.chats.observe(viewLifecycleOwner) {
            if(viewModel.chats.value!!.isNotEmpty()) {
                binding.chats.smoothScrollToPosition(viewModel.chats.value!!.size - 1)
            }
            binding.chats.adapter = viewModel.chats.value?.let {
                MessageAdapter(requireContext(), it.toList())
            }
        }

        binding.send.setOnClickListener {
            if(binding.chat.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Enter a message!", Toast.LENGTH_SHORT).show()
            } else {
                val chat = binding.chat.text.toString()
                viewModel.addMessage(chat)
                binding.chat.text.clear()

            }
        }
    }
}