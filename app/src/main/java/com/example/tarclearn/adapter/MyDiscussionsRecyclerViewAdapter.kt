package com.example.tarclearn.adapter.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.DiscussionThreadDto
import com.example.tarclearn.ui.discussion.MyDiscussionsFragmentDirections
import com.google.android.material.card.MaterialCardView

class MyDiscussionsRecyclerViewAdapter() :
    RecyclerView.Adapter<MyDiscussionsRecyclerViewAdapter.ViewHolder>() {
    var threadList = mutableListOf<DiscussionThreadDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardHeader: TextView = itemView.findViewById(R.id.tv_card_header)
        val cardSubHeader: TextView = itemView.findViewById(R.id.tv_card_subheader)
        val card: MaterialCardView = itemView.findViewById(R.id.item_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item_view_discussion_card, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = threadList[position]
        holder.cardHeader.text = item.threadTitle
        holder.cardSubHeader.text = item.userName
        holder.card.setOnClickListener {
            val action =
                MyDiscussionsFragmentDirections.actionMyDiscussionsFragmentToDiscussionFragment(
                    item.threadId
                )
            holder.card.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return threadList.size
    }
}