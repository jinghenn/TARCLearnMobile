package com.example.tarclearn.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tarclearn.R
import com.example.tarclearn.adapter.QuizRecyclerViewAdapter
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
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchQuizList(chapterId)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = QuizRecyclerViewAdapter()
        binding.quizRecyclerView.adapter = adapter
        viewModel.quizList.observe(viewLifecycleOwner){
            it?.let{
                adapter.quizList = it as MutableList
            }
        }
        binding.fabAddQuiz.setOnClickListener {
            val action = QuizListFragmentDirections
                .actionQuizListFragmentToManageQuizFragment(Constants.MODE_CREATE, -1)
            findNavController().navigate(action)
        }
    }
}