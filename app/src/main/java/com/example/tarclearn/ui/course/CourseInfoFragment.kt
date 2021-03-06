package com.example.tarclearn.ui.course

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tarclearn.R
import com.example.tarclearn.adapter.ChapterRecyclerViewAdapter
import com.example.tarclearn.adapter.student.StudentChapterRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentCourseInfoBinding
import com.example.tarclearn.factory.CourseViewModelFactory
import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.repository.CourseRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.course.CourseInfoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CourseInfoFragment : Fragment() {
    private lateinit var binding: FragmentCourseInfoBinding
    private lateinit var viewModel: CourseInfoViewModel
    private val args: CourseInfoFragmentArgs by navArgs()
    private lateinit var sharedPref: SharedPreferences
    private var isLect = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentCourseInfoBinding.inflate(inflater, container, false)

        //initialize viewmodel
        val vmFactory = CourseViewModelFactory(CourseRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(CourseInfoViewModel::class.java)
        sharedPref = requireContext()
            .getSharedPreferences(getString(R.string.pref_user), Context.MODE_PRIVATE)
        isLect = sharedPref.getBoolean(getString(R.string.key_is_lecturer), false)
        if (isLect) {
            setHasOptionsMenu(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isLect) {
            binding.parentView.removeView(binding.coordLayout)
            val params = binding.nestedScrollView.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            binding.nestedScrollView.layoutParams = params
        }
        //bind data to textviews
        viewModel.fetchCourseInfo(args.courseId)
        viewModel.course.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvCourseCode.text = it.courseCode
                binding.tvCourseDescription.text = it.courseDescription
                binding.tvCourseName.text = it.courseTitle
            }
        }
        //bind course chapters to recyclerview
        viewModel.fetchChapterList(args.courseId)
        viewModel.chapterList.observe(viewLifecycleOwner) {
            if (isLect) {
                val adapter = ChapterRecyclerViewAdapter(args.courseId)
                if (it.isNotEmpty()) {
                    adapter.chapterList = it as MutableList<ChapterDetailDto>
                    binding.chapterListRecyclerView.adapter = adapter
                }
            } else {
                val adapter = StudentChapterRecyclerViewAdapter(args.courseId)
                it?.let {
                    adapter.chapterList = it as MutableList<ChapterDetailDto>
                    binding.chapterListRecyclerView.adapter = adapter
                }
            }
        }

        //fab
        binding.fabAddChapter.setOnClickListener {
            val action =
                CourseInfoFragmentDirections.actionCourseInfoFragmentToManageChapterFragment(
                    0,
                    Constants.MODE_CREATE,
                    args.courseId
                )
            findNavController().navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.edit_delete_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Delete Course")
                    .setMessage(
                        "Are you sure you want to delete this course permanently?" +
                                "\n\nThis action cannot be undone."
                    )
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteCourse(args.courseId)
                        requireActivity().onBackPressed()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            R.id.edit -> {
                val action =
                    CourseInfoFragmentDirections.actionCourseInfoFragmentToManageCourseFragment(
                        2,
                        args.courseId
                    )
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}