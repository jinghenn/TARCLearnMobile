package com.example.tarclearn.ui.course

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
import com.example.tarclearn.databinding.FragmentManageChapterBinding
import com.example.tarclearn.factory.ChapterViewModelFactory
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.course.ManageChapterViewModel

class ManageChapterFragment : Fragment() {
    private lateinit var binding: FragmentManageChapterBinding
    private lateinit var viewModel: ManageChapterViewModel
    private val args: ManageChapterFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageChapterBinding.inflate(inflater, container, false)
        //create view model
        val vmFactory = ChapterViewModelFactory(ChapterRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(ManageChapterViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextFieldWithChapterDetail()
        setupButtonWithMode()

        binding.tvChapterName.doAfterTextChanged {
            if (it.toString() != "") {
                binding.tvChapterNameLayout.isErrorEnabled = false
            } else {
                binding.tvChapterNameLayout.isErrorEnabled = true
                binding.tvChapterNameLayout.error = "Chapter Name cannot be empty"
            }
        }
        binding.tvChapterNo.doAfterTextChanged {
            if (it.toString() != "") {
                binding.tvChapterNoLayout.isErrorEnabled = false
            } else {
                binding.tvChapterNoLayout.isErrorEnabled = true
                binding.tvChapterNoLayout.error = "Chapter No. cannot be empty"
            }

        }
        viewModel.success.observe(viewLifecycleOwner) {
            val text = when (args.mode) {
                Constants.MODE_CREATE -> "Chapter ${viewModel.chapter.value?.chapterNo} created successfully"
                Constants.MODE_EDIT -> "Chapter: ${viewModel.chapter.value?.chapterNo} updated successfully"
                else -> "Success"
            }
            if (it == true) {
                Toast.makeText(
                    requireContext(),
                    text,
                    Toast.LENGTH_LONG
                ).show()

                viewModel.resetSuccessFlag()
                requireActivity().onBackPressed()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvChapterNoLayout.isErrorEnabled = true
                binding.tvChapterNoLayout.error = it
                viewModel.resetErrorFlag()
            }
        }

    }

    private fun setupTextFieldWithChapterDetail() {
        if (args.mode == Constants.MODE_EDIT) {
            viewModel.fetchChapter(args.chapterId)
            viewModel.chapter.observe(viewLifecycleOwner) {
                val chapNo = viewModel.chapter.value!!.chapterNo.substringBefore(".")
                val chapSubNo = viewModel.chapter.value!!.chapterNo.substringAfter(".", "")
                it?.let {
                    binding.tvChapterNo.setText(chapNo)
                    binding.tvChapterSubNo.setText(chapSubNo)
                    binding.tvChapterName.setText(viewModel.chapter.value?.chapterTitle)
                }
            }
        }
    }

    private fun setupButtonWithMode() {
        val btnOk = binding.btnOk
        when (args.mode) {
            Constants.MODE_CREATE -> {
                btnOk.text = getString(R.string.label_create)
                btnOk.setOnClickListener {
                    val chapterNo = getChapterNo()
                    val chapterName = getChapterName()
                    if (chapterNo != "" && chapterName != "") {
                        viewModel.createChapter(args.courseId, chapterNo, chapterName)
                    }
                }
            }
            Constants.MODE_EDIT -> {
                btnOk.text = getString(R.string.label_save)
                btnOk.setOnClickListener {
                    val chapterNo = getChapterNo()
                    val chapterName = getChapterName()
                    if (chapterNo != "" && chapterName != "") {
                        viewModel.updateChapter(
                            args.courseId,
                            args.chapterId,
                            chapterNo,
                            chapterName
                        )
                    }
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getChapterNo(): String {
        val chapterNo = binding.tvChapterNo.text.toString()
        val mergedChapNo = when (val subChapterNo = binding.tvChapterSubNo.text.toString()) {
            "" -> chapterNo
            else -> "$chapterNo.$subChapterNo"
        }

        if (chapterNo == "") {
            binding.tvChapterNoLayout.isErrorEnabled = true
            binding.tvChapterNoLayout.error = "Chapter No. cannot be empty"
            return ""
        }

        return mergedChapNo
    }

    private fun getChapterName(): String {
        val chapterName = binding.tvChapterName.text.toString()
        if (chapterName == "") {
            binding.tvChapterNameLayout.isErrorEnabled = true
            binding.tvChapterNameLayout.error = "Chapter Name cannot be empty"
            return ""
        }
        return chapterName
    }

}