package com.example.expensesmingle

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesmingle.databinding.FriendnameBinding

class FriendsAdapter (private val list: ArrayList<String>) : ListAdapter<String, FriendsAdapter.FriendViewHolder>(DiffCallback()) {
    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    class FriendViewHolder(binding: FriendnameBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.friendName
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendsAdapter.FriendViewHolder {
        return FriendViewHolder(FriendnameBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = (position + 1).toString() + ") " + item
    }

    override fun getItemCount(): Int = list.size
}