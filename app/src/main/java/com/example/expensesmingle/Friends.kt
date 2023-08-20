package com.example.expensesmingle

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensesmingle.databinding.FragmentFriendsBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Friends : Fragment() {
    private lateinit var binding: FragmentFriendsBinding
    private val viewModel: ExpensesMingleViewModel by activityViewModels()
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFriendsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(viewModel.friends.value!!.size == 0) {
            viewModel.getFriends()
        }
        binding.friendName.layoutManager = LinearLayoutManager(context)
        binding.friendName.adapter = FriendsAdapter(viewModel.friends.value!!)

        binding.addFriend.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Add Friends Username")
            val viewInflated = layoutInflater.inflate(R.layout.edit_text_layout, view as ViewGroup, false)
            val input = viewInflated.findViewById<EditText>(R.id.username)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(viewInflated)
            builder.setPositiveButton("Add") { dialog, _ ->
                val name = input.text.toString()
                if (!addFriend(name)){
                    return@setPositiveButton
                }
                Toast.makeText(requireContext(), "Friend added successfully!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }

        viewModel.friends.observe(viewLifecycleOwner) {
            binding.friendName.adapter = FriendsAdapter(it)
        }
    }

    private fun addFriend(name: String): Boolean {
        return if(name !in viewModel.usernames.values) {
            Toast.makeText(requireContext(), "Invalid username!", Toast.LENGTH_SHORT).show()
            false
        } else if(name == viewModel.usernames[FirebaseAuth.getInstance().currentUser!!.uid]) {
            Toast.makeText(requireContext(), "You cannot add yourself!", Toast.LENGTH_SHORT).show()
            false
        } else if(viewModel.friends.value!!.contains(name)) {
            Toast.makeText(requireContext(), "Friend already added!", Toast.LENGTH_SHORT).show()
            false
        } else {
            database.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("friends").push().setValue(name)
            if(viewModel.usernames.isEmpty()) {
                viewModel.getUsernames()
            }
            for ((k, v) in viewModel.usernames) {
                if(v == name) {
                    var currUser = viewModel.usernames[FirebaseAuth.getInstance().currentUser!!.uid]
                    database.child("Users").child(k).child("friends").push().setValue(currUser)
                    break
                }
            }
            true
        }
    }
}