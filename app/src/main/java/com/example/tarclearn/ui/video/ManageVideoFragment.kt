package com.example.tarclearn.ui.video

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.tarclearn.databinding.FragmentManageVideoBinding
import com.example.tarclearn.factory.MaterialViewModelFactory
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.repository.MaterialRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.viewmodel.video.ManageVideoViewModel
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import java.util.*
import kotlin.properties.Delegates


class ManageVideoFragment : Fragment() {
    private lateinit var binding: FragmentManageVideoBinding
    private lateinit var viewModel: ManageVideoViewModel
    private val args: ManageVideoFragmentArgs by navArgs()
    private var chapterId: Int by Delegates.notNull()
    private var uri: Uri by Delegates.notNull()
    private var newVid: MaterialDetailDto by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageVideoBinding.inflate(inflater, container, false)
        chapterId = requireActivity().intent.getIntExtra("chapterId", -1)
        val vmFactory = MaterialViewModelFactory(MaterialRepository())
        viewModel = ViewModelProvider(this, vmFactory).get(ManageVideoViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupModeMenu()
        setupButtons()
        setupTextViews()
        viewModel.success.observe(viewLifecycleOwner) {
            val text = when (args.mode) {
                Constants.MODE_CREATE -> "Video uploaded successfully"
                Constants.MODE_EDIT -> "Video detail updated successfully"
                else -> "Action succeeded"
            }
            it?.let {

                Toast.makeText(requireContext(), text, Toast.LENGTH_LONG)
                    .show()
                viewModel.resetSuccessFlag()
                requireActivity().onBackPressed()

            }

        }
        viewModel.noExistFlag.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    binding.tvVideoNoLayout.isErrorEnabled = true
                    binding.tvVideoNoLayout.error = "Video No. already exist."
                } else {
                    binding.tvVideoNoLayout.isErrorEnabled = false
                }
                viewModel.resetNoExistFlag()
            }
        }
    }

    private fun setupModeMenu() {
        val items = listOf("Lecture", "Tutorial", "Practical")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown, items)
        (binding.menuMode as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.menuMode.setText(binding.menuMode.adapter.getItem(0).toString(), false)
    }

    private fun setupTextViews() {
        binding.tvVideoName.doAfterTextChanged { binding.tvVideoNameLayout.isErrorEnabled = false }
        binding.tvVideoNo.doAfterTextChanged {
            val index = viewModel.video.value?.index ?: -2
            val no = it.toString().toIntOrNull() ?: -1
            if (no == 0) {
                binding.tvVideoNoLayout.isErrorEnabled = true
                binding.tvVideoNoLayout.error = "Video No. cannot be 0"
            } else if ((args.mode == Constants.MODE_EDIT && index != no) || args.mode == Constants.MODE_CREATE) {
                viewModel.checkIsVideoIndexExist(
                    chapterId,
                    no,
                    binding.menuMode.text.toString().toUpperCase(Locale.ROOT),
                    true
                )

            }
        }
        binding.menuMode.doAfterTextChanged {
            val no = binding.tvVideoNo.text.toString().toIntOrNull() ?: -1
            if (no == 0) {
                binding.tvVideoNoLayout.isErrorEnabled = true
                binding.tvVideoNoLayout.error = "Video No. cannot be 0"
            } else {
                viewModel.checkIsVideoIndexExist(
                    chapterId,
                    no,
                    binding.menuMode.text.toString().toUpperCase(Locale.ROOT),
                    true
                )
            }
        }
        binding.tvVideoTitle.doAfterTextChanged {
            binding.tvVideoTitleLayout.isErrorEnabled = false
        }
        when (args.mode) {
            Constants.MODE_EDIT -> {
                viewModel.fetchVideoDetail(args.materialId)
                binding.tvVideoName.isEnabled = false

                viewModel.video.observe(viewLifecycleOwner) {

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
                        binding.tvVideoNo.setText(it.index.toString())
                        binding.tvVideoTitle.setText(it.materialTitle)
                        binding.tvVideoDesc.setText(it.materialDescription)
                        binding.tvVideoName.setText(it.materialName)
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
                    val vidNo = getVideoNo()
                    val vidTitle = getVideoTitle()
                    val vidMode = getVideoMode()
                    val vidName = getVideoName()
                    val vidDesc = binding.tvVideoDesc.text.toString()
                    if (vidNo != -1 && vidTitle != "" && vidMode != "" && vidName != "") {
                        Toast.makeText(requireContext(), "Upload started", Toast.LENGTH_SHORT)
                            .show()
                        newVid =
                            MaterialDetailDto(0, vidNo, vidTitle, vidDesc, vidName, vidMode, true)

                        val serverUrl =
                            "http://192.168.0.72:50000/api/upload?chapterId=$chapterId&type=${Constants.VIDEO_MATERIAL}"
                        MultipartUploadRequest(
                            requireContext(), serverUrl
                        ).setMethod("POST")
                            .addFileToUpload(uri.toString(), "file")
                            .addParameter("material", newVid.toString())
                            .startUpload()

                    }
                }
            }
            Constants.MODE_EDIT -> {
                binding.btnChooseVideo.isEnabled = false
                btnOk.text = getString(R.string.label_save)
                btnOk.setOnClickListener {
                    val vidNo = getVideoNo()
                    val vidTitle = getVideoTitle()
                    val vidMode = getVideoMode()
                    val vidName = getVideoName()
                    val vidDesc = binding.tvVideoDesc.text.toString()
                    if (vidNo != -1 && vidTitle != "" && vidMode != "") {
                        val newVid =
                            MaterialDetailDto(0, vidNo, vidTitle, vidDesc, vidName, vidMode, true)
                        viewModel.updateVideo(args.materialId, newVid)
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupFilePicker() {
        val btnPick = binding.btnChooseVideo
        var intent: Intent? = null
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            intent =
                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "video/*"
                    flags =
                        (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                }

        } else {
            intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
                type = "video/*"
                flags =
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
        }
        btnPick.setOnClickListener {
            startActivityForResult(
                Intent.createChooser(intent, "Select Video"),
                Constants.REQUEST_PICK_MATERIAL
            )
        }
    }

    private fun getVideoNo(): Int {
        val videoNo = binding.tvVideoNo.text.toString()
        if (binding.tvVideoNoLayout.isErrorEnabled) {
            return -1
        }
        if (videoNo == "") {
            binding.tvVideoNoLayout.isErrorEnabled = true
            binding.tvVideoNoLayout.error = "Video No. cannot be empty"
            return -1
        }
        return Integer.parseInt(videoNo)
    }

    private fun getVideoTitle(): String {
        val videoTitle = binding.tvVideoTitle.text.toString()
        if (videoTitle == "") {
            binding.tvVideoTitleLayout.isErrorEnabled = true
            binding.tvVideoTitleLayout.error = "Video Title cannot be empty"
            return ""
        }
        return videoTitle
    }

    private fun getVideoMode(): String {
        val raw = binding.menuMode.text.toString()
        val vidMode = raw.toUpperCase(Locale.ROOT)
        if (vidMode == "") {
            binding.menuModeLayout.isErrorEnabled = true
            binding.menuModeLayout.error = "Mode cannot be empty"
            return ""
        }
        return vidMode
    }

    private fun getVideoName(): String {
        val vidName = binding.tvVideoName.text.toString()
        if (vidName == "") {
            binding.tvVideoNameLayout.isErrorEnabled = true
            binding.tvVideoNameLayout.error = "Video File cannot be empty"
            return ""
        }
        return vidName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_PICK_MATERIAL) {
            data?.data?.also {
                uri = it
                val cursor =
                    requireActivity().applicationContext.contentResolver.query(
                        it, null, null, null, null
                    )

                cursor?.let { c ->
                    val nameIdx = c.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                    c.moveToFirst()
                    val name = c.getString(nameIdx)
                    binding.tvVideoName.setText(name)
                }
                cursor?.close()
            }
        }
    }


}