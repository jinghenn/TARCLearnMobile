package com.example.tarclearn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.MessageDetailDto
import com.example.tarclearn.model.MessageDto
import com.example.tarclearn.repository.MessageRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>() {
    var messageList = mutableListOf<MessageDetailDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var dialog: BottomSheetDialog? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.tv_message)
        val userName: TextView = itemView.findViewById(R.id.tv_user_name)
        val listItem: LinearLayout = itemView.findViewById(R.id.list_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.material_list_item_two_line, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val context = holder.listItem.context
        val item = messageList[position]
        holder.message.text = item.message
        holder.userName.text = item.userName
        holder.listItem.setOnLongClickListener {
            inflateBottomSheet(item.userId, position)
            true
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    private fun inflateBottomSheet(commentUserId: String, position: Int) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )
        val userId = sharedPref.getString(context.getString(R.string.key_user_id), null)
        val isLect = sharedPref.getBoolean(context.getString(R.string.key_is_lecturer), false)
        var layout: View? = null

        if (userId == commentUserId) {
            layout = View.inflate(context, R.layout.message_bottom_sheet_edit_delete, null)
        } else if (isLect) {
            layout = View.inflate(context, R.layout.message_bottom_sheet_delete, null)
        }
        layout?.let { l ->
            dialog = BottomSheetDialog(context)
            dialog?.setContentView(l)
            dialog?.show()
            val editBtn: Button? = l.findViewById(R.id.btn_edit_message)
            val deleteBtn: Button = l.findViewById(R.id.btn_delete_message)

            editBtn?.let { setupEditButton(editBtn, position) }
            setupDeleteButton(deleteBtn, position)
        }


    }

    private fun setupEditButton(editBtn: Button, position: Int) {
        val item = messageList[position]
        editBtn.setOnClickListener {
            val editView =
                View.inflate(context, R.layout.message_edit_bottom_sheet, null)
            val editDialog = BottomSheetDialog(context)
            editDialog.setContentView(editView)
            editDialog.show()
            val btnCancel: Button = editView.findViewById(R.id.btn_cancel)
            val btnOk: Button = editView.findViewById(R.id.btn_ok)
            val etNewMsg: EditText = editView.findViewById(R.id.tv_edit_message)
            val etLayout: TextInputLayout =
                editView.findViewById(R.id.tv_edit_message_layout)
            val oldMsg = etNewMsg.text.toString()
            etNewMsg.setText(item.message)
            etNewMsg.doAfterTextChanged {
                if (it.toString() == "") {
                    etLayout.isErrorEnabled = true
                    etLayout.error = "Message cannot be empty"
                } else {
                    etLayout.isErrorEnabled = false
                }
            }
            btnCancel.setOnClickListener {
                editDialog.cancel()
            }
            btnOk.setOnClickListener {

                val newMsg = etNewMsg.text.toString()
                if (newMsg != "" && newMsg != oldMsg) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val repository = MessageRepository()
                        val response = repository.updateMessage(
                            item.messageId, MessageDto(item.messageId, newMsg)
                        )
                        if (response.code() == 200) {
                            editDialog.cancel()
                            dialog?.cancel()
                            messageList[position] = response.body()!!
                            notifyDataSetChanged()

                        }
                    }
                }
            }
        }
    }

    private fun setupDeleteButton(deleteBtn: Button, position: Int) {
        val item = messageList[position]
        deleteBtn.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Delete Message")
                .setMessage("Delete this message?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val repository = MessageRepository()
                        val response = repository.deleteMessage(item.messageId)
                        if (response.code() == 200) {
                            dialog?.cancel()
                            messageList.remove(item)
                            notifyDataSetChanged()

                        }
                    }
                }
                .setNegativeButton("Cancel", null).show()
        }
    }
}