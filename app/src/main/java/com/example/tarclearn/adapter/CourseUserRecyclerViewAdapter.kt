package com.example.tarclearn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.UserDto
import com.example.tarclearn.repository.CourseRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CourseUserRecyclerViewAdapter(
    val courseId: Int,
    val currentUserId: String
) :
    RecyclerView.Adapter<CourseUserRecyclerViewAdapter.ViewHolder>() {
    var userList = mutableListOf<UserDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnDelete: MaterialButton = itemView.findViewById(R.id.btn_delete)
        val cardHeader: TextView = itemView.findViewById(R.id.tv_card_header)
        val cardSubHeader: TextView = itemView.findViewById(R.id.tv_card_subheader)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = userList[position]
        holder.cardHeader.text = item.userId
        holder.cardSubHeader.text = item.username
        if (item.userId == currentUserId) {
            holder.btnDelete.isEnabled = false
            holder.btnDelete.icon = null
            var headerText = holder.cardHeader.text.toString()
            headerText += " (you)"
            holder.cardHeader.text = headerText
        }
        holder.btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(holder.btnDelete.context)
                .setTitle("Remove User")
                .setMessage("Are you sure you want to remove \nUser: ${item.userId} from this course?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val repository = CourseRepository()
                        val response = repository.unenrol(courseId, item.userId)
                        if (response.code() == 200) {
                            userList.remove(item)
                            notifyDataSetChanged()
                        }
                    }
                }
                .setNegativeButton("Cancel", null).show()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

}