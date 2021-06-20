package com.example.tarclearn.ui.quiz

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tarclearn.R
import com.example.tarclearn.adapter.QuizRecyclerViewAdapter
import com.example.tarclearn.adapter.student.StudentQuizRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentQuizListBinding
import com.example.tarclearn.factory.ChapterViewModelFactory
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.quiz.QuizListViewModel
import kotlin.properties.Delegates

class QuizListFragment : Fragment() {
    private lateinit var binding: FragmentQuizListBinding
    private lateinit var viewModel: QuizListViewModel
    private var chapterId: Int by Delegates.notNull()
    private lateinit var sharedPref: SharedPreferences
    private var isLect = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuizListBinding.inflate(inflater, container, false)
        val vmFactory = ChapterViewModelFactory(ChapterRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(QuizListViewModel::class.java)
        chapterId = requireActivity().intent.getIntExtra("chapterId", -1)
        sharedPref = requireContext()
            .getSharedPreferences(getString(R.string.pref_user), Context.MODE_PRIVATE)
        isLect = sharedPref.getBoolean(getString(R.string.key_is_lecturer), false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchQuizList(chapterId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isLect) {
            binding.rootLayout.removeView(binding.coordLayout)
            val params = binding.quizRecyclerView.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            binding.quizRecyclerView.layoutParams = params
        }
        viewModel.quizList.observe(viewLifecycleOwner) {
            it?.let {
                if (isLect) {
                    val adapter = QuizRecyclerViewAdapter()
                    adapter.quizList = it as MutableList
                    binding.quizRecyclerView.adapter = adapter
                } else {
                    val adapter = StudentQuizRecyclerViewAdapter()
                    adapter.quizList = it as MutableList
                    binding.quizRecyclerView.adapter = adapter
                }
            }
        }
        binding.fabAddQuiz.setOnClickListener {
            val action = QuizListFragmentDirections
                .actionQuizListFragmentToManageQuizFragment(Constants.MODE_CREATE, -1)
            findNavController().navigate(action)
        }
    }
}