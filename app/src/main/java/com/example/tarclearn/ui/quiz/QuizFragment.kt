package com.example.tarclearn.ui.quiz

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        val vmFactory = QuizViewModelFactory(QuizRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(QuizViewModel::class.java)

        recyclerView = binding.questionRecyclerView

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = QuestionRecyclerViewAdapter(requireContext())
        if (savedInstanceState == null) {
            viewModel.fetchQuizQuestions(args.quizId)

        } else {
            val pa = savedInstanceState.getParcelable<Parcelable>("quiz_recycler_view")
            recyclerView.layoutManager?.onRestoreInstanceState(pa)
        }

        recyclerView.adapter = adapter
        viewModel.quizQuestions.observe(viewLifecycleOwner) {
            it?.let {

                binding.tvQuizTitle.text = it.quizTitle
                adapter.questionList = it.getQuestionList()
            }
        }
        binding.btnCheckAnswer.setOnClickListener {
            val score = adapter.checkAnswer()
            binding.tvScore.text =
                getString(R.string.score_string, score, adapter.questionList.size)

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mListState = recyclerView.layoutManager?.onSaveInstanceState()

        outState.putParcelable("quiz_recycler_view", mListState)
    }

}