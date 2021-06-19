package com.example.tarclearn.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tarclearn.R
import com.example.tarclearn.adapter.QuestionRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentQuizBinding
import com.example.tarclearn.factory.QuizViewModelFactory
import com.example.tarclearn.repository.QuizRepository
import com.example.tarclearn.viewmodel.quiz.QuizViewModel

class QuizFragment : Fragment() {
    private lateinit var viewModel: QuizViewModel
    private lateinit var binding: FragmentQuizBinding
    private val args: QuizFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        val vmFactory = QuizViewModelFactory(QuizRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(QuizViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchQuizQuestions(args.quizId)

        val adapter = QuestionRecyclerViewAdapter(requireContext())
        val recyclerView = binding.questionRecyclerView
        recyclerView.adapter = adapter

        viewModel.quizQuestions.observe(viewLifecycleOwner){
            it?.let{
                binding.tvQuizTitle.text = it.quizTitle
                adapter.questionList = it.getQuestionList()
            }
        }
        binding.btnCheckAnswer.setOnClickListener {
            val score = adapter.checkAnswer()
            binding.tvScore.text = getString(R.string.score_string, score, adapter.questionList.size)

        }

    }
}