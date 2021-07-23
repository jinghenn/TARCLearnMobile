package com.example.tarclearn.ui.material

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tarclearn.R
import com.example.tarclearn.adapter.MaterialRecyclerViewAdapter
import com.example.tarclearn.adapter.student.StudentMaterialRecyclerViewAdapter
import com.example.tarclearn.databinding.FragmentMaterialListBinding
import com.example.tarclearn.factory.ChapterViewModelFactory
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.material.MaterialListViewModel
import java.util.*
import kotlin.properties.Delegates

class MaterialListFragment : Fragment() {
    private lateinit var binding: FragmentMaterialListBinding
    private lateinit var viewModel: MaterialListViewModel
    private var chapterId by Delegates.notNull<Int>()
    private lateinit var sharedPref: SharedPreferences
    private var isLect = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMaterialListBinding.inflate(inflater, container, false)
        val vmFactory = ChapterViewModelFactory(ChapterRepository())
        viewModel = ViewModelProvider(requireActivity().viewModelStore, vmFactory)
            .get(MaterialListViewModel::class.java)
        chapterId = requireActivity().intent.getIntExtra("chapterId", -1)
        sharedPref = requireContext()
            .getSharedPreferences(getString(R.string.pref_user), Context.MODE_PRIVATE)
        isLect = sharedPref.getBoolean(getString(R.string.key_is_lecturer), false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initializeMenu()
        viewModel.fetchMaterialList(
            chapterId,
            binding.menuMode.text.toString()
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isLect) {
            binding.rootLayout.removeView(binding.coordLayout)
            val params = binding.materialRecyclerView.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            binding.materialRecyclerView.layoutParams = params
        }
        val materialRecyclerView = binding.materialRecyclerView

        viewModel.materialList.observe(viewLifecycleOwner) {
            it?.let {
                if (isLect) {
                    val adapter = MaterialRecyclerViewAdapter()
                    adapter.materialList = it as MutableList
                    materialRecyclerView.adapter = adapter
                } else {
                    val adapter = StudentMaterialRecyclerViewAdapter()
                    adapter.materialList = it as MutableList
                    materialRecyclerView.adapter = adapter
                }

            }
        }
        binding.fabAddMaterial.setOnClickListener {
            val action =
                MaterialListFragmentDirections.actionMaterialListFragmentToManageMaterialFragment(
                    Constants.MODE_CREATE,
                    -1
                )
            findNavController().navigate(action)
        }
    }

    private fun initializeMenu() {
        val items = Constants.MODE_LIST
        val menuMode = binding.menuMode
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown, items)
        (menuMode as? AutoCompleteTextView)?.setAdapter(adapter)

        val currentMode = viewModel.mode
        menuMode.setText(adapter.getItem(currentMode).toString(), false)

        menuMode.setOnItemClickListener { _, _, position, _ ->
            val selectedMode = adapter.getItem(position).toString()
            viewModel.fetchMaterialList(chapterId, selectedMode)
            viewModel.changeMode(position)
        }

    }

}