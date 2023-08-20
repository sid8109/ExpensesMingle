package com.example.expensesmingle

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.expensesmingle.databinding.FragmentLoginPageBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking


class LoginPage : Fragment() {
    private lateinit var binding : FragmentLoginPageBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: ExpensesMingleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            viewModel.getUsernames()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
        binding = FragmentLoginPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpButton.setOnClickListener {
            createAccount()
        }
        binding.signIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginPage2_to_signInPage2)
        }
    }

    private fun createAccount() {
        if(!binding.emailEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty() && !binding.confirmPasswordEditText.text.isNullOrEmpty()) {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            if(password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(requireContext(), "Authentication Successfull.", Toast.LENGTH_SHORT).show()
                            load()
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Password does not match", Toast.LENGTH_SHORT).show()
                binding.confirmPasswordEditText.text?.clear()
                binding.passwordEditText.text?.clear()
            }
        } else {
            Toast.makeText(requireContext(), "Enter all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reload() {
        findNavController().navigate(R.id.netTransactions)
    }

    private fun load() {
        findNavController().navigate(R.id.action_loginPage2_to_info2)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}