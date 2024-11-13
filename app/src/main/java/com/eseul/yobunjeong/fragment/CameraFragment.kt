package com.eseul.yobunjeong.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eseul.yobunjeong.Factory.CameraViewModelFactory
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.repository.ImageRepository
import com.eseul.yobunjeong.viewmodel.CameraViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class CameraFragment : Fragment() {

    companion object {
        private const val CAMERA_REQUEST_CODE = 1001
        private const val CAMERA_PERMISSION_CODE = 1002
    }

    private lateinit var cameraViewModel: CameraViewModel
    private var tempResult: String? = null  // 임시로 촬영 결과를 저장할 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        // ViewModel 초기화 (Activity 범위에서 공유)
        val repository = ImageRepository()
        val factory = CameraViewModelFactory(repository)
        cameraViewModel = ViewModelProvider(requireActivity(), factory).get(CameraViewModel::class.java)

        // 카메라 퍼미션 체크 및 요청
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            openCamera()
        }

        // 서버 응답 결과를 관찰하고 임시 변수에 저장
        cameraViewModel.uploadResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                tempResult = translateTrashType(it)  // 결과를 임시 변수에 저장
                showResultFragment(tempResult ?: "알 수 없음")
            } ?: Log.e("CameraFragment", "uploadResult is null")
        }

        return view
    }

    fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }
    }

    private fun showResultFragment(result: String) {
        val resultFragment = ResultFragment()
        val bundle = Bundle().apply {
            putString("recognition_result", result)
        }
        resultFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, resultFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                // 권한이 거부된 경우 처리
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                val imagePart = convertBitmapToMultipart(it)
                cameraViewModel.uploadImage(imagePart)
            }
        }
    }

    private fun convertBitmapToMultipart(bitmap: Bitmap): MultipartBody.Part {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), bos.toByteArray())
        return MultipartBody.Part.createFormData("image", "captured_image.jpg", requestBody)
    }

    // trash_type을 한국어로 변환하는 함수
    private fun translateTrashType(trashType: String): String {
        return when {
            trashType.contains("Plastic", ignoreCase = true) -> "플라스틱"
            trashType.contains("Can", ignoreCase = true) -> "캔"
            trashType.contains("Paper", ignoreCase = true) -> "종이"
            else -> "알 수 없음"
        }
    }
}
