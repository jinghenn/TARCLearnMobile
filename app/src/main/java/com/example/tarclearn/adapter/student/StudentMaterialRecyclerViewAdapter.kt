package com.example.tarclearn.adapter.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.ui.material.MaterialListFragmentDirections
import com.google.android.material.card.MaterialCardView

class StudentMaterialRecyclerViewAdapter :
    RecyclerView.Adapter<StudentMaterialRecyclerViewAdapter.ViewHolder>() {
    var materialList = mutableListOf<MaterialDetailDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.card_view)
        val title: TextView = itemView.findViewById(R.id.card_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.student_item_view_card, parent, false)
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
    }

}