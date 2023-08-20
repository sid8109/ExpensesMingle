package com.example.expensesmingle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesmingle.databinding.CheckboxRowBinding
import com.example.expensesmingle.viewmodel.ExpensesMingleViewModel

class CheckBoxAdapter(private val list: ArrayList<String>, private val viewModel: ExpensesMingleViewModel): ListAdapter<String, CheckBoxAdapter.CheckBoxViewHolder>(DiffCallback()) {
    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    class CheckBoxViewHolder(binding: CheckboxRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var checkbox = binding.checkbox
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckBoxViewHolder {
        return CheckBoxViewHolder(CheckboxRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CheckBoxViewHolder, position: Int) {
        val item = list[position]
        holder.checkbox.text = item
        holder.checkbox.setOnClickListener {
            if (!holder.checkbox.isChecked) {
                viewModel.spentOn.value!!.remove(holder.checkbox.text.toString())
            } else {
                viewModel.spentOn.value!!.add(holder.checkbox.text.toString())
            }
        }
    }

    override fun getItemCount() = list.size
}