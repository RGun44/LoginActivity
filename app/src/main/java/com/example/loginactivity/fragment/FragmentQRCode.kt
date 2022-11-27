package com.example.loginactivity.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.loginactivity.R
import com.example.loginactivity.databinding.FragmentQrCodeBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage


class FragmentQRCode : Fragment(), View.OnClickListener {

    private var _binding: FragmentQrCodeBinding? = null
    private val binding get() = _binding!!

    companion object{
        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 101

        private const val TAG = "MAIN_TAG"
    }

    private  lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    private var imageUri: Uri? = null
    private var barcodeScannerOption : BarcodeScannerOptions? = null
    private var barcodeScanner: BarcodeScanner? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrCodeBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cameraBtn.setOnClickListener(this)
        binding.galleryBtn.setOnClickListener(this)
        binding.scanBtn.setOnClickListener(this)

        cameraPermissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        barcodeScannerOption = BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOption!!)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cameraBtn -> {
                if (checkCameraPermissions()){
                    pickImageCamera()
                }else{
                    requestCameraPermission()
                }
            }
            R.id.galleryBtn -> {
                if (checkStoragePermission()){
                    pickImageGallery()
                }else{
                    requestStoragePermission()
                }
            }
            R.id.scanBtn -> {
                if (imageUri == null){
                    showToast("Pick image first")
                } else {
                    detectResultFromImage()
                }
            }
        }
    }

    private fun checkCameraPermissions(): Boolean {
        val resultcamera = (activity?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA) }
                == PackageManager.PERMISSION_GRANTED)
        val resultstorage = (activity?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) }
                == PackageManager.PERMISSION_GRANTED)

        return resultcamera && resultstorage
    }

    private fun requestCameraPermission(){
        activity?.let {
            ActivityCompat.requestPermissions(
                it, cameraPermissions,
                FragmentQRCode.CAMERA_REQUEST_CODE
            )
        }
    }

    private fun checkStoragePermission(): Boolean{
        val result = (activity?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) }
                == PackageManager.PERMISSION_GRANTED)
        return result
    }

    private fun requestStoragePermission(){
        activity?.let {
            ActivityCompat.requestPermissions(
                it, storagePermissions,
                FragmentQRCode.STORAGE_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            FragmentQRCode.CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted){
                        pickImageCamera()
                    }else{
                        showToast("Camera dan Storage Permissions are required")
                    }
                }
            }

            FragmentQRCode.STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()){
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if (storageAccepted){
                        pickImageGallery()
                    }else{
                        showToast("Storage are required...")
                    }
                }
            }
        }
    }

    private fun showToast(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun pickImageCamera(){
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Foto Sample")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Deskripsi Foto Sample")

        imageUri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data
            Log.d(FragmentQRCode.TAG, "cameraActivityResultLauncher: imageUri: $imageUri")

            binding.imageTv.setImageURI(imageUri)
        }
    }

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data

            imageUri = data!!.data
            Log.d(FragmentQRCode.TAG, "galleryActivityResultLauncher: imageUri: $imageUri")

            binding.imageTv.setImageURI(imageUri)
        }else{
            showToast("Dibatalkan ............")
        }

    }

    private fun detectResultFromImage(){
        try {
            val inputImage = activity?.let { InputImage.fromFilePath(it, imageUri!!) }
            val barcodeResult = inputImage?.let {
                barcodeScanner?.process(it)
                    ?.addOnSuccessListener { barcodes ->
                        extractbarcodeQrCodeInfo(barcodes)
                    }
                    ?.addOnFailureListener { e ->
                        Log.d(FragmentQRCode.TAG, "detectResultFromImage: ", e)
                        showToast("Failed Scanning due to ${e.message}")
                    }
            }
        }catch (e: Exception){
            Log.d(FragmentQRCode.TAG, "detectResultFromImage: ", e)
            showToast("Failed Due to ${e.message}")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun extractbarcodeQrCodeInfo(barcodes: List<Barcode>){
        for(barcode in barcodes){
            val bound = barcode.boundingBox
            val corners = barcode.cornerPoints

            val rawValue = barcode.rawValue
            Log.d(FragmentQRCode.TAG, "extractbarcodeQrCodeInfo: rawValue: $rawValue")

            val valueType = barcode.valueType
            when(valueType){
                Barcode.TYPE_WIFI -> {
                    val typeWifi = barcode.wifi
                    val ssid = "${typeWifi?.ssid}"
                    val password = "${typeWifi?.password}"
                    var encryptionType = "${typeWifi?.encryptionType}"

                    if(encryptionType == "1"){
                        encryptionType = "OPEN"
                    }else if (encryptionType == "2"){
                        encryptionType = "WPA"
                    }else if (encryptionType == "3"){
                        encryptionType = "WEP"
                    }

                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: TYPE_WIFI")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: ssid: $ssid")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: password: $password")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: encryptionType: $encryptionType")

                    binding.resultView.text =
                        "TYPE_WIFI \n ssid: $ssid \npassword: $password \nencryptionType: $encryptionType"+
                                "\n\nrawValue: $rawValue"
                }
                Barcode.TYPE_URL->{
                    val typeUrl = barcode.url
                    val title = "${typeUrl?.title}"
                    val url = "${typeUrl?.url}"

                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: TYPE_URL")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: title: $title")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: url: $url")

                    binding.resultView.text = "TYPE_URL \ntitle: $title \nurl: $url \n\nrawValue: $rawValue"
                }
                Barcode.TYPE_EMAIL -> {
                    val typeEmail = barcode.email
                    val address = "${typeEmail?.address}"
                    val body = "${typeEmail?.body}"
                    val subject = "${typeEmail?.subject}"

                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: TYPE_EMAIL")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: address: $address")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: body: $body")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: subject: $subject")

                    binding.resultView.text = "TYPE_EMAIL \ntitle: $address \nurl: $body \nsubject: $subject \n\nrawValue : $rawValue"
                }
                Barcode.TYPE_CONTACT_INFO -> {
                    val typeContact = barcode.contactInfo
                    val title = "${typeContact?.title}"
                    val organisasi = "${typeContact?.organization}"
                    val name = "${typeContact?.name}"
                    val phone = "${typeContact?.phones}"

                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: TYPE_CONTACT_INFO")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: Title: $title")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: Organization: $organisasi")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: nama: $name")
                    Log.d(FragmentQRCode.TAG,"extractbarcodeQrCodeInfo: Phone: $phone")

                    binding.resultView.text = "TYPE_CONTACT_INFO " + "\ntitle: $title " +
                            "\nnama: $name " +
                            "\norganization: $organisasi " +
                            "\nPhone: $phone " + "\n\nrawValue : $rawValue"

                }
                else -> {
                    binding.resultView.text = "rawValue: $rawValue"
                }
            }
        }
    }

}