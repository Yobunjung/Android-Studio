package com.eseul.yobunjeong.fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.eseul.yobunjeong.Factory.BinViewModelFactory
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.databinding.FragmentMapBinding
import com.eseul.yobunjeong.model.BinStatusModel
import com.eseul.yobunjeong.viewmodel.BinViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    // ViewModel 초기화
    private val viewModel: BinViewModel by viewModels { BinViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapBinding.bind(view)

        // 바텀 시트 초기화
        val bottomSheet = binding.bottomSheet.root
        if (bottomSheet is FrameLayout) {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                isHideable = true
            }
        } else {
            throw IllegalStateException("BottomSheet's root is not a FrameLayout")
        }

        // 지도 초기화
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // ViewModel 데이터 관찰
        lifecycleScope.launchWhenStarted {
            viewModel.binStatus.collectLatest { binStatus ->
                binStatus?.let { updateBottomSheet(it) }
            }
        }
    }



    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // 마커 아이콘 설정
        val bitmapDraw = ContextCompat.getDrawable(requireContext(), R.drawable.marker) as BitmapDrawable
        val b = bitmapDraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, 100, 150, false)

        // Custom 마커 추가 (USW)
        val usw = LatLng(37.21124694874648, 126.9799920737918)
        val markerUSW = googleMap.addMarker(
            MarkerOptions()
                .position(usw)
                .title("Marker in USW")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        )

        // 카메라 이동
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usw, 14f))

        // 마커 클릭 리스너 설정
        googleMap.setOnMarkerClickListener { clickedMarker ->
            if (clickedMarker == markerUSW) {
                // ViewModel로부터 데이터 로드
                viewModel.fetchBinStatus()
                // BottomSheet의 peek_height만큼 보여주기
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            true
        }

        // 지도 클릭 리스너 설정
        googleMap.setOnMapClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED ||
                bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED
            ) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        // BottomSheetBehavior 콜백 설정
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 바텀 시트 상태 변경 시 동작 추가 가능
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    // 상태가 숨겨졌을 때 동작 정의 가능
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 스크롤 시 애니메이션 관련 동작
            }
        })
    }



    private fun updateBottomSheet(binStatus: BinStatusModel) {
        // 주소 업데이트
        binding.bottomSheet.address1.text = binStatus.address

        // 쓰레기통 상태 업데이트
        binStatus.bins.forEach { bin ->
            when (bin.trash_type) {
                "Paper" -> {
                    binding.bottomSheet.statusPaper.text = bin.status
                    // 상태에 따라 색상 변경
                    val color = if (bin.status == "on") "#2795EF" else "#FB0000"
                    binding.bottomSheet.statusPaper.setTextColor(android.graphics.Color.parseColor(color))
                }
                "Plastic" -> {
                    binding.bottomSheet.statusPlastic.text = bin.status
                    // 상태에 따라 색상 변경
                    val color = if (bin.status == "on") "#2795EF" else "#FB0000"
                    binding.bottomSheet.statusPlastic.setTextColor(android.graphics.Color.parseColor(color))
                }
                "Can" -> {
                    binding.bottomSheet.statusCan.text = bin.status
                    // 상태에 따라 색상 변경
                    val color = if (bin.status == "on") "#2795EF" else "#FB0000"
                    binding.bottomSheet.statusCan.setTextColor(android.graphics.Color.parseColor(color))
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
