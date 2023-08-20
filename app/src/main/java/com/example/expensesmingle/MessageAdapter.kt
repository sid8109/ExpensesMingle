package com.example.expensesmingle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private val context: Context, private val list: List<Pair<String, Boolean>>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class SentMessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val sent: TextView = view.findViewById(R.id.sent_message)
    }

    class RecieveMessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val receive: TextView = view.findViewById(R.id.recieve_message)
    }

    private val ITEM_RECIEVE = 1
    private val ITEM_SENT = 2

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if(item.second) {
            ITEM_SENT
        } else {
            ITEM_RECIEVE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if(viewType == 2) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.msg_get, parent, false)
            RecieveMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if(holder.javaClass == SentMessageViewHolder::class.java) {
            val viewHolder = holder as SentMessageViewHolder
            holder.sent.text = item.first
        } else {
            val viewHolder = holder as RecieveMessageViewHolder
            holder.receive.text = item.first
        }
    }

    override fun getItemCount(): Int = list.size
}