package com.example.tarclearn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.repository.MaterialRepository
import com.example.tarclearn.ui.video.VideoListFragmentDirections
import com.example.tarclearn.util.Constants
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VideoRecyclerViewAdapter() : RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder>() {
    var videoList = mutableListOf<MaterialDetailDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.card_view)
        val title: TextView = itemView.findViewById(R.id.card_text)
        val editBtn: Button = itemView.findViewById(R.id.btn_edit)
        val deleteBtn: Button = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_editable_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = videoList[position]
        holder.title.text = holder.title.context.getString(R.string.material_title, item.index, item.materialTitle)
        holder.card.setOnClickListener {
            val action =
                VideoListFragmentDirections.actionVideoListFragmentToVideoFragment(item.materialId)
            holder.card.findNavController().navigate(action)
        }
        holder.editBtn.setOnClickListener {
            val action = VideoListFragmentDirections.actionVideoListFragmentToManageVideoFragment(
                Constants.MODE_EDIT,
                item.materialId
            )
            holder.card.findNavController().navigate(action)
        }
        holder.deleteBtn.setOnClickListener {
            MaterialAlertDialogBuilder(holder.deleteBtn.context)
                .setTitle("Remove Video")
                .setMessage("Are you sure you want to remove this video?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val repository = MaterialRepository()
                        val response = repository.deleteMaterial(item.materialId)
                        if (response.code() == 200) {
                            videoList.remove(item)
                            notifyDataSetChanged()
                        }
                    }
                }
                .setNegativeButton("Cancel", null).show()
        }
    }
}