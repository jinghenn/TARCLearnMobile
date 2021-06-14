package com.example.tarclearn.ui.discussion

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tarclearn.R
import com.example.tarclearn.databinding.FragmentManageDiscussionBinding
import com.example.tarclearn.factory.DiscussionViewModelFactory
import com.example.tarclearn.repository.DiscussionRepository
import com.example.tarclearn.repository.MessageRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.discussion.ManageDiscussionViewModel
import kotlin.properties.Delegates

class ManageDiscussionFragment : Fragment() {
    private lateinit var binding: FragmentManageDiscussionBinding
    private lateinit var viewModel: ManageDiscussionViewModel
    private val args: ManageDiscussionFragmentArgs by navArgs()
    private lateinit var userId: String
    private var chapterId: Int by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageDiscussionBinding.inflate(inflater, container, false)
        val vmFactory = DiscussionViewModelFactory(DiscussionRepository(), MessageRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(ManageDiscussionViewModel::class.java)
        val sharedPref = requireContext().getSharedPreferences(
            getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )
        userId = sharedPref.getString(getString(R.string.key_user_id), null) ?: ""
        chapterId = requireActivity().intent.getIntExtra("chapterId", -1)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (args.mode == Constants.MODE_EDIT) {
            viewModel.fetchDiscussionDetail(args.threadId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        viewModel.successFlag.observe(viewLifecycleOwner) {
            it?.let {
                val successMsg = when (args.mode) {
                    Constants.MODE_CREATE -> "Discussion posted"
                    Constants.MODE_EDIT -> "Discussion detail updated"
                    else -> "Success"
                }
                Toast.makeText(requireContext(), successMsg, Toast.LENGTH_LONG).show()
                viewModel.resetSuccessFlag()
                requireActivity().onBackPressed()
            }
        }
        viewModel.errorFlag.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_LONG).show()
                viewModel.resetErrorFlag()
            }
        }
        viewModel.discussionDetail.observe(viewLifecycleOwner) {
            if (args.mode == Constants.MODE_EDIT) {
                it?.let {
                    binding.tvDiscussionTitle.setText(it.threadTitle)
                    binding.tvDiscussionDescription.setText(it.threadDescription)
                }
            }
        }
        binding.tvDiscussionTitle.doAfterTextChanged {
            if (it.toString() == "") {
                binding.tvDiscussionTitleLayout.isErrorEnabled = true
                binding.tvDiscussionTitleLayout.error = "Title cannot be empty"
            } else {
                binding.tvDiscussionTitleLayout.isErrorEnabled = false
            }
        }
        binding.tvDiscussionDescription.doAfterTextChanged {
            if (it.toString() == "") {
                binding.tvDiscussionDescriptionLayout.isErrorEnabled = true
                binding.tvDiscussionDescriptionLayout.error = "Description cannot be empty"
            } else {
                binding.tvDiscussionDescriptionLayout.isErrorEnabled = false
            }
        }
    }

    private fun setupButton() {
        binding.btnOk.text = when (args.mode) {
            Constants.MODE_CREATE -> "Create"
            Constants.MODE_EDIT -> "Save"
            else -> "Ok"
        }
        binding.btnOk.setOnClickListener {
            when (args.mode) {
                Constants.MODE_CREATE -> {
                    val title = getThreadTitle()
                    val desc = getThreadDescription()
                    if (title != "" && desc != "") {
                        viewModel.createDiscussionThread(title, desc, chapterId, userId)
                    }
                }
                Constants.MODE_EDIT -> {
                    val title = getThreadTitle()
                    val desc = getThreadDescription()
                    if (title != "" && desc != "") {
                        viewModel.updateDiscussionThread(args.threadId, title, desc)
                    }
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getThreadTitle(): String {
        val title = binding.tvDiscussionTitle.text.toString()
        if (title == "") {
            binding.tvDiscussionTitleLayout.isErrorEnabled = true
            binding.tvDiscussionTitleLayout.error = "Title cannot be empty"
            return ""
        }

        return title.trim()
    }

    private fun getThreadDescription(): String {
        val desc = binding.tvDiscussionDescription.text.toString()
        if (desc == "") {
            binding.tvDiscussionDescriptionLayout.isErrorEnabled = true
            binding.tvDiscussionDescriptionLayout.error = "Description cannot be empty"
            return ""
        }

        return desc.trim()
    }

}