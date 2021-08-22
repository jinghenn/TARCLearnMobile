package com.example.tarclearn.ui.material

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tarclearn.R
import com.example.tarclearn.databinding.FragmentMaterialBinding
import com.example.tarclearn.factory.MaterialViewModelFactory
import com.example.tarclearn.repository.MaterialRepository
import com.example.tarclearn.util.Constants
import com.example.tarclearn.util.Constants.Companion.BASE_URL
import com.example.tarclearn.viewmodel.material.MaterialViewModel
import com.tonyodev.fetch2.*
import kotlin.properties.Delegates

class MaterialFragment : Fragment() {
    private lateinit var binding: FragmentMaterialBinding
    private lateinit var viewModel: MaterialViewModel
    private val args: MaterialFragmentArgs by navArgs()
    private var fetch: Fetch by Delegates.notNull()
    private var contentType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMaterialBinding.inflate(inflater, container, false)
        val vmFactory = MaterialViewModelFactory(MaterialRepository())
        viewModel = ViewModelProvider(this, vmFactory)
            .get(MaterialViewModel::class.java)

        val fetchConfig = FetchConfiguration.Builder(requireActivity().applicationContext)
            .setDownloadConcurrentLimit(3)
            .enableLogging(true)
            .build()
        fetch = Fetch.Impl.getInstance(fetchConfig)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchMaterialDetail(args.materialId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var materialName: String? = null
        viewModel.material.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.checkFileAvailability(it.materialId)
                binding.tvMaterialTitle.text = it.materialTitle
                binding.tvMaterialDesc.text = it.materialDescription
                binding.btnViewMaterial.text = getString(R.string.download)
                materialName = it.materialName

            }
        }
        viewModel.fileAvailable.observe(viewLifecycleOwner) {
            it?.let { isFileExist ->
                if (isFileExist) {
                    materialName?.let { name ->
                        setupViewMaterialButton(name)
                    }
                } else {
                    binding.btnViewMaterial.isEnabled = false
                    binding.btnViewMaterial.text = getString(R.string.corrupted_file)
                }
            }
        }

        viewModel.uri.observe(viewLifecycleOwner){
            it?.let{
                val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        type = contentType
                        this.data = it
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    if (intent.resolveActivity(requireContext().packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            requireContext(), "No application can open this file.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
    }

    private fun setupViewMaterialButton(fileName: String) {
        val btn = binding.btnViewMaterial
        val ext = fileName.substringAfterLast(".")
        contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: "*/*"

        btn.setOnClickListener {

            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = contentType
                putExtra(Intent.EXTRA_TITLE, fileName)
            }
            startActivityForResult(intent, Constants.REQUEST_CREATE_FILE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CREATE_FILE && resultCode == RESULT_OK) {


            data?.data?.let {
                val url = "${BASE_URL}api/materials/download?materialId=${args.materialId}"
                val req = Request(url, it).apply {
                    priority = Priority.HIGH
                    networkType = NetworkType.ALL
                }
                fetch.enqueue(req, {}, { error -> Log.d("download", error.toString()) })
                val mListener = object : AbstractFetchListener() {
                    override fun onCompleted(download: Download) {
                        Log.d("urionresult", it.toString())
                        viewModel.setDownloadCompleted(it)
                    }
                }
                fetch.addListener(mListener)

            }
        }
    }

}