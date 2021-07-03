package com.example.tarclearn.ui.discussion

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.SharedPreferences
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.adapter.MessageRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentDiscussionBinding
import com.example.tarclearn.factory.DiscussionViewModelFactory
import com.example.tarclearn.repository.DiscussionRepository
import com.example.tarclearn.repository.MessageRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.discussion.DiscussionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.properties.Delegates
class DiscussionFragment : Fragment() {
    private lateinit var binding: FragmentDiscussionBinding
    private lateinit var viewModel: DiscussionViewModel
    private val args: DiscussionFragmentArgs by navArgs()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userId: String
    private var isLect: Boolean by Delegates.notNull()
    private lateinit var recyclerView: RecyclerView
    private var threadUserId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiscussionBinding.inflate(inflater, container, false)
        val vmFactory = DiscussionViewModelFactory(DiscussionRepository(), MessageRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(DiscussionViewModel::class.java)
        sharedPref = requireContext().getSharedPreferences(
            getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )
        userId = sharedPref.getString(getString(R.string.key_user_id), null) ?: ""
        isLect = sharedPref.getBoolean(getString(R.string.key_is_lecturer), false)
        recyclerView = binding.messageRecyclerView
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchDiscussionThreadDetail(args.threadId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.tvThreadDescription.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
        setupRecyclerView()
        setupSendButton()

        viewModel.discussionDetail.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvThreadTitle.text = it.threadTitle
                binding.tvThreadDescription.text = it.threadDescription
                binding.tvUserName.text = it.userName
                threadUserId = it.userId
                if (threadUserId == userId || isLect) {
                    setHasOptionsMenu(true)
                }
            }
        }
        viewModel.successFlag.observe(viewLifecycleOwner) {
            it?.let {
                requireActivity().onBackPressed()
            }
        }

    }

    private fun setupSendButton() {

        binding.btnSend.setOnClickListener {
            val message = binding.tvMessage.text.toString().trim()
            if (message != "") {
                viewModel.sendMessage(message, userId, args.threadId)
                binding.tvMessage.setText("")
                val imm =
                    requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
                binding.scrollView.smoothScrollTo(0, binding.scrollView.getChildAt(0).height)
            } else {
                binding.tvMessageLayout.isErrorEnabled = true
                binding.tvMessageLayout.error = "Message cannot be empty"
            }

        }
        binding.tvMessage.doAfterTextChanged {
            binding.tvMessageLayout.isErrorEnabled = false
        }

    }

    private fun setupRecyclerView() {
        val adapter = MessageRecyclerViewAdapter(requireContext())
        recyclerView.adapter = adapter
        viewModel.messages.observe(viewLifecycleOwner) {
            it?.let {
                adapter.messageList = it

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (threadUserId == userId) {
            inflater.inflate(R.menu.edit_delete_menu, menu)
        } else {
            inflater.inflate(R.menu.delete_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit -> {
                val action = DiscussionFragmentDirections
                    .actionDiscussionFragmentToManageDiscussionFragment(
                        Constants.MODE_EDIT,
                        args.threadId
                    )
                findNavController().navigate(action)
                true
            }
            R.id.delete -> {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Delete Discussion")
                    .setMessage(
                        "Are you sure you want to delete this discussion thread permanently?" +
                                "\n\nThis action cannot be undone."
                    )
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteDiscussionThread(args.threadId)
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}