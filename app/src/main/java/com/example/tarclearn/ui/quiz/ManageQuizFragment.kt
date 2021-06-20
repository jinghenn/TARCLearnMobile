package com.example.tarclearn.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.adapter.QuizCreateRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentManageQuizBinding
import com.example.tarclearn.factory.QuizViewModelFactory
import com.example.tarclearn.model.Quiz
import com.example.tarclearn.repository.QuizRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.quiz.ManageQuizViewModel
import kotlin.properties.Delegates

class ManageQuizFragment : Fragment() {
    private lateinit var binding: FragmentManageQuizBinding
    private lateinit var viewModel: ManageQuizViewModel
    private val args: ManageQuizFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private var adapter: QuizCreateRecyclerViewAdapter? = null
    private var chapterId: Int by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageQuizBinding.inflate(inflater, container, false)
        val vmFactory = QuizViewModelFactory(QuizRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(ManageQuizViewModel::class.java)
        recyclerView = binding.questionRecyclerView

        chapterId = requireActivity().intent.getIntExtra("chapterId", -1)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupButtons()

        viewModel.successFlag.observe(viewLifecycleOwner) {
            it?.let {
                val successMsg = when (args.mode) {
                    Constants.MODE_CREATE -> "Quiz created successfully"
                    Constants.MODE_EDIT -> "Quiz updated successfully"
                    else -> "Success"
                }
                Toast.makeText(requireContext(), successMsg, Toast.LENGTH_LONG).show()
                viewModel.resetSuccessFlag()
                requireActivity().onBackPressed()
            }
        }
    }


    private fun setupRecyclerView() {

        if (args.mode == Constants.MODE_EDIT) {
            viewModel.fetchQuizQuestions(args.quizId)
            viewModel.quizQuestions.observe(viewLifecycleOwner) {
                it?.let {
                    binding.etQuizTitle.setText(it.quizTitle)
                    adapter = QuizCreateRecyclerViewAdapter(requireContext())
                    adapter?.let { a ->
                        recyclerView.adapter = a
                        a.questionList = it.getQuestionList()
                    }
                }
            }
        }
        if (args.mode == Constants.MODE_CREATE) {
            adapter = QuizCreateRecyclerViewAdapter(requireContext())
            adapter?.let { a ->
                recyclerView.adapter = a

            }
        }


    }

    private fun setupButtons() {
        binding.btnCreate.text = when (args.mode) {
            Constants.MODE_CREATE -> "Create"
            else -> "Save"
        }
        binding.btnAddQuestion.setOnClickListener { adapter?.addQuestion() }
        binding.btnCreate.setOnClickListener {
            val quizQuestions = adapter?.getQuizQuestions()
            Log.d("quiz", quizQuestions.toString())
            val quizTitle = binding.etQuizTitle.text.toString()
            if (quizTitle != "") {
                when (args.mode) {
                    Constants.MODE_CREATE -> {
                        if (quizQuestions != null) {
                            viewModel.createQuiz(Quiz(quizTitle, quizQuestions, chapterId))
                        } else {
                            showRequiredFieldToast()
                        }
                    }
                    Constants.MODE_EDIT -> {
                        if (quizQuestions != null) {
                            viewModel.updateQuiz(
                                args.quizId,
                                Quiz(quizTitle, quizQuestions, chapterId)
                            )
                        } else {
                            showRequiredFieldToast()
                        }
                    }
                }
            } else {
                binding.etQuizTitleLayout.isErrorEnabled = true
                binding.etQuizTitleLayout.error = "Title cannot be empty"
            }

        }
        binding.btnCancel.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun showRequiredFieldToast() {
        Toast.makeText(
            requireContext(),
            "Please fill in required field",
            Toast.LENGTH_LONG
        ).show()
    }
}