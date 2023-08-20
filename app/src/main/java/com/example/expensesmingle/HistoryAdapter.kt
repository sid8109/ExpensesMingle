package com.example.expensesmingle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesmingle.databinding.DetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class HistoryAdapter(private val list: ArrayList<Triple<String, Double, Long>>): ListAdapter<String, HistoryAdapter.HistoryViewHolder>(DiffCallback())  {
    private val dateFormat = SimpleDateFormat("d MMMM, h:mma", Locale.getDefault())
    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    class HistoryViewHolder(binding: DetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        val item = binding.item
        val itemAmt = binding.itemAmount
        val time = binding.time
    }

     override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.HistoryViewHolder {
        return HistoryAdapter.HistoryViewHolder(DetailsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        val item = list[position]
        holder.item.text = item.first
        val solution = (item.second * 100.0).roundToInt() / 100.0
        holder.itemAmt.text = "₹ $solution"
        holder.time.text = dateFormat.format(item.third)
    }

    override fun getItemCount(): Int = list.size
}