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
import com.example.tarclearn.ui.material.MaterialListFragmentDirections
import com.example.tarclearn.util.Constants
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MaterialRecyclerViewAdapter : RecyclerView.Adapter<MaterialRecyclerViewAdapter.ViewHolder>() {
    var materialList = mutableListOf<MaterialDetailDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.chapter_card)
        val title: TextView = itemView.findViewById(R.id.chapter_title)
        val editBtn: Button = itemView.findViewById(R.id.btn_edit_chapter)
        val deleteBtn: Button = itemView.findViewById(R.id.btn_delete_chapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_course_chapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return materialList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = materialList[position]
        holder.title.text = item.materialTitle
        holder.card.setOnClickListener {
            val action =
                MaterialListFragmentDirections.actionMaterialListFragmentToMaterialFragment(item.materialId)
            holder.card.findNavController().navigate(action)
        }
        holder.editBtn.setOnClickListener {
            val action =
                MaterialListFragmentDirections.actionMaterialListFragmentToManageMaterialFragment(
                    Constants.MODE_EDIT,
                    item.materialId
                )
            holder.card.findNavController().navigate(action)
        }
        holder.deleteBtn.setOnClickListener {
            onDelete(holder, position)
        }
    }

    private fun onDelete(holder: ViewHolder, position: Int) {
        val item = materialList[position]
        MaterialAlertDialogBuilder(holder.deleteBtn.context)
            .setTitle("Remove Material")
            .setMessage("Are you sure you want to remove this material?")
            .setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    val repository = MaterialRepository()
                    val response = repository.deleteMaterial(item.materialId)
                    if (response.code() == 200) {
                        materialList.remove(item)
                        notifyDataSetChanged()
                    }
                }
            }
            .setNegativeButton("Cancel", null).show()
    }
}