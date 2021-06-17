package com.example.tarclearn.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.adapter.QuizEditRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentManageQuizBinding
import com.example.tarclearn.factory.QuizViewModelFactory
import com.example.tarclearn.model.Quiz
import com.example.tarclearn.repository.QuizRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.quiz.ManageQuizViewModel

class ManageQuizFragment : Fragment() {
    private lateinit var binding: FragmentManageQuizBinding
    private lateinit var viewModel: ManageQuizViewModel
    private val args: ManageQuizFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuizEditRecyclerViewAdapter
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
        adapter = QuizEditRecyclerViewAdapter(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter
        binding.btnAddQuestion.setOnClickListener { adapter.addNewQuestion() }
    }

    private fun setupButtons() {
        binding.btnCreate.setOnClickListener {
            when (args.mode) {
                Constants.MODE_CREATE -> {
                    //viewModel.createQuiz()
                    val s = adapter.getQuizQuestions()
                    Log.d("choice", s.toString())
                }
            }
        }
        binding.btnCancel.setOnClickListener { requireActivity().onBackPressed() }
    }

}