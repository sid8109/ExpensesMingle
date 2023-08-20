package com.example.expensesmingle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.expensesmingle.Data.User
import com.example.expensesmingle.databinding.FragmentInfoBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Info : Fragment() {
    private lateinit var binding : FragmentInfoBinding
    private lateinit var database: DatabaseReference
    private val viewModel: ExpensesMingleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        super.onViewCreated(view, savedInstanceState)

        binding.done.setOnClickListener {
            if(!binding.nameEditText.text.isNullOrEmpty() && !binding.usernameEditText.text.isNullOrEmpty()) {
                val username = binding.usernameEditText.text.toString()
                if(isValid(username)) {
                    val name = binding.nameEditText.text.toString()
                    writeNewUser(name, username)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Username already taken!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Enter all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValid(username: String): Boolean {
        return !viewModel.usernames.containsValue(username)
    }

    private fun writeNewUser(name: String, username: String) {
        val user = User(name, username, FirebaseAuth.getInstance().currentUser!!.uid, null)
        database.child(user.uid).setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    requireActivity(),
                    "Welcome, ${user.name}",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_info2_to_netTransactions)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Try Again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}