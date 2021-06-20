package com.example.tarclearn.ui.material

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tarclearn.R
import com.example.tarclearn.databinding.FragmentManageMaterialBinding
import com.example.tarclearn.factory.MaterialViewModelFactory
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.repository.MaterialRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.material.ManageMaterialViewModel
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import java.util.*
import kotlin.properties.Delegates

class ManageMaterialFragment : Fragment() {
    private lateinit var binding: FragmentManageMaterialBinding
    private lateinit var viewModel: ManageMaterialViewModel
    private val args: ManageMaterialFragmentArgs by navArgs()
    private var chapterId: Int by Delegates.notNull()
    private var uri: Uri by Delegates.notNull()
    private var newMaterial: MaterialDetailDto by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageMaterialBinding.inflate(inflater, container, false)
        chapterId = requireActivity().intent.getIntExtra("chapterId", -1)
        val vmFactory = MaterialViewModelFactory(MaterialRepository())
        viewModel = ViewModelProvider(this, vmFactory).get(ManageMaterialViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupModeMenu()
        setupButtons()
        setupTextViews()
        viewModel.success.observe(viewLifecycleOwner) {
            val text = when (args.mode) {
                Constants.MODE_CREATE -> "Material uploaded successfully"
                Constants.MODE_EDIT -> "Material detail updated successfully"
                else -> "Action succeeded"
            }
            it?.let {

                Toast.makeText(requireContext(), text, Toast.LENGTH_LONG)
                    .show()
                viewModel.resetSuccessFlag()
                requireActivity().onBackPressed()

            }

        }
    }

    private fun setupModeMenu() {
        Log.d("menui", "set")
        val items = listOf("Lecture", "Tutorial", "Practical")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown, items)
        (binding.menuMode as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setupTextViews() {
        binding.tvMaterialName.doAfterTextChanged {
            binding.tvMaterialNameLayout.isErrorEnabled = false
        }
        binding.tvMaterialNo.doAfterTextChanged {
            binding.tvMaterialNoLayout.isErrorEnabled = false
        }
        binding.menuMode.doAfterTextChanged { binding.menuModeLayout.isErrorEnabled = false }
        binding.tvMaterialTitle.doAfterTextChanged {
            binding.tvMaterialTitleLayout.isErrorEnabled = false
        }
        when (args.mode) {
            Constants.MODE_EDIT -> {
                viewModel.fetchMaterialDetail(args.materialId)
                binding.tvMaterialName.isEnabled = false

                viewModel.material.observe(viewLifecycleOwner) {

                    it?.let {

                        when (it.mode) {
                            "LECTURE" -> binding.menuMode.setText(
                                binding.menuMode.adapter.getItem(0).toString(), false
                            )
                            "TUTORIAL" -> {
                                binding.menuMode.setText(
                                    binding.menuMode.adapter.getItem(1).toString(), false
                                )
                            }
                            "PRACTICAL" -> {
                                binding.menuMode.setText(
                                    binding.menuMode.adapter.getItem(2).toString(), false
                                )
                            }
                        }
                        binding.tvMaterialNo.setText(it.index.toString())
                        binding.tvMaterialTitle.setText(it.materialTitle)
                        binding.tvMaterialDesc.setText(it.materialDescription)
                        binding.tvMaterialName.setText(it.materialName)
                    }
                }
            }
        }
    }

    private fun setupButtons() {
        setupFilePicker()
        val btnOk = binding.btnOk
        when (args.mode) {
            Constants.MODE_CREATE -> {
                btnOk.text = getString(R.string.label_create)
                btnOk.setOnClickListener {
                    val matNo = getMaterialNo()
                    val matTitle = getMaterialTitle()
                    val matMode = getMaterialMode()
                    val matName = getMaterialName()
                    val matDesc = binding.tvMaterialDesc.text.toString()
                    if (matNo != -1 && matTitle != "" && matMode != "" && matName != "") {
                        Toast.makeText(requireContext(), "Upload started", Toast.LENGTH_SHORT)
                            .show()
                        newMaterial = MaterialDetailDto(
                            0, matNo, matTitle, matDesc, matName, matMode, false
                        )

                        val serverUrl = "http://192.168.0.72:50000/api/" +
                                "upload?chapterId=$chapterId&type=${Constants.OTHER_MATERIAL}"
                        MultipartUploadRequest(
                            requireContext(), serverUrl
                        ).setMethod("POST")
                            .addFileToUpload(uri.toString(), "file")
                            .addParameter("material", newMaterial.toString())
                            .startUpload()

                    }
                }
            }
            Constants.MODE_EDIT -> {
                binding.btnChooseMaterial.isEnabled = false
                btnOk.text = getString(R.string.label_save)
                btnOk.setOnClickListener {
                    val matNo = getMaterialNo()
                    val matTitle = getMaterialTitle()
                    val matMode = getMaterialMode()
                    val matName = getMaterialName()
                    val matDesc = binding.tvMaterialDesc.text.toString()
                    if (matNo != -1 && matTitle != "" && matMode != "") {
                        val newmat =
                            MaterialDetailDto(0, matNo, matTitle, matDesc, matName, matMode, false)
                        viewModel.updateMaterial(args.materialId, newmat)
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_PICK_MATERIAL) {
            data?.data?.also {
                uri = it

                val cursor =
                    requireContext().contentResolver.query(
                        it, null, null, null, null
                    )

                cursor?.let {
                    val nameIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    it.moveToFirst()
                    val name = it.getString(nameIdx)
                    binding.tvMaterialName.setText(name)
                }
                cursor?.close()
            }
        }
    }

    private fun setupFilePicker() {
        val btnPick = binding.btnChooseMaterial
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("text/*", "application/*", "audio/*", "image/*")
            )
            //addCategory(Intent.CATEGORY_OPENABLE)
        }
        btnPick.setOnClickListener {
            Log.d("buttons", "clicked")
            startActivityForResult(
                Intent.createChooser(intent, "Select Material"),
                Constants.REQUEST_PICK_MATERIAL
            )
        }
    }

    private fun getMaterialNo(): Int {
        val materialNo = binding.tvMaterialNo.text.toString()
        if (materialNo == "") {
            binding.tvMaterialNoLayout.isErrorEnabled = true
            binding.tvMaterialNoLayout.error = "Material No. cannot be empty"
            return -1
        }
        return Integer.parseInt(materialNo)
    }

    private fun getMaterialTitle(): String {
        val materialTitle = binding.tvMaterialTitle.text.toString()
        if (materialTitle == "") {
            binding.tvMaterialTitleLayout.isErrorEnabled = true
            binding.tvMaterialTitleLayout.error = "Material Title cannot be empty"
            return ""
        }
        return materialTitle
    }

    private fun getMaterialMode(): String {
        val raw = binding.menuMode.text.toString()
        val matMode = raw.toUpperCase(Locale.ROOT)
        if (matMode == "") {
            binding.menuModeLayout.isErrorEnabled = true
            binding.menuModeLayout.error = "Mode cannot be empty"
            return ""
        }
        return matMode
    }

    private fun getMaterialName(): String {
        val matName = binding.tvMaterialName.text.toString()
        if (matName == "") {
            binding.tvMaterialNameLayout.isErrorEnabled = true
            binding.tvMaterialNameLayout.error = "Material File cannot be empty"
            return ""
        }
        return matName
    }


}