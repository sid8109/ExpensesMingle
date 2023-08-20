package com.example.expensesmingle

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesmingle.databinding.ListBinding
import kotlin.math.roundToInt

class SummaryAdapter(private val list: List<Pair<String, Double>>, private val onItemClicked: (String) -> Unit) : ListAdapter<String, SummaryAdapter.SummaryViewHolder>(DiffCallback()) {
    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    class SummaryViewHolder(binding: ListBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.friendName
        val amt: TextView = binding.friendAmount
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SummaryViewHolder {
        val viewHolder = SummaryViewHolder(ListBinding.inflate(LayoutInflater.from(parent.context)))
        viewHolder.itemView.setOnClickListener {
            onItemClicked(viewHolder.name.text.toString())
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.first
        val solution = (item.second * 100.0).roundToInt() / 100.0
        holder.amt.text = "₹ $solution"
        if(item.second > 0) {
            holder.name.setTextColor(Color.parseColor("#0BF4AA"))
            holder.amt.setTextColor(Color.parseColor("#0BF4AA"))
        } else if(item.second < 0) {
            holder.name.setTextColor(Color.parseColor("#F40B55"))
            holder.amt.setTextColor(Color.parseColor("#F40B55"))
        }
    }

    override fun getItemCount(): Int = list.size
}