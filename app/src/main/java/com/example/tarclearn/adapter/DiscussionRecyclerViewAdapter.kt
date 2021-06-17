package com.example.tarclearn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.DiscussionThreadDto
import com.example.tarclearn.repository.DiscussionRepository
import com.example.tarclearn.ui.discussion.DiscussionListFragmentDirections
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscussionRecyclerViewAdapter() :
    RecyclerView.Adapter<DiscussionRecyclerViewAdapter.ViewHolder>() {
    var threadList = mutableListOf<DiscussionThreadDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnDelete: MaterialButton = itemView.findViewById(R.id.btn_delete)
        val cardHeader: TextView = itemView.findViewById(R.id.tv_card_header)
        val cardSubHeader: TextView = itemView.findViewById(R.id.tv_card_subheader)
        val card: MaterialCardView = itemView.findViewById(R.id.item_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_card, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = threadList[position]
        holder.cardHeader.text = item.threadTitle
        holder.cardSubHeader.text = item.userName
        holder.btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(holder.btnDelete.context)
                .setTitle("Remove Discussion Thread")
                .setMessage("Are you sure you want to remove this discussion thread?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val repository = DiscussionRepository()
                        val response = repository.deleteDiscussionThread(item.threadId)
                        if (response.code() == 200) {
                            threadList.remove(item)
                            notifyDataSetChanged()
                        }
                    }
                }
                .setNegativeButton("Cancel", null).show()
        }
        holder.card.setOnClickListener {
            val action =
                DiscussionListFragmentDirections.actionDiscussionListFragmentToDiscussionFragment(
                    item.threadId
                )
            holder.card.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return threadList.size
    }
}