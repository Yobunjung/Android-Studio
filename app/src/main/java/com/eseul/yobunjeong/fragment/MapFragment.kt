package com.eseul.yobunjeong.fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 바텀 시트 초기화
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        // 지도 초기화01ssa
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val bitmapDraw = ContextCompat.getDrawable(requireContext(), R.drawable.marker) as BitmapDrawable
        val b = bitmapDraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, 100, 150, false)

        // USW 마커 추가
        val usw = LatLng(37.21, 126.975)
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
                // 바텀 시트 열기
                showBottomSheet(clickedMarker)
            }
            true
        }
    }

    private fun showBottomSheet(marker: Marker) {
        // 바텀 시트 표시
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
