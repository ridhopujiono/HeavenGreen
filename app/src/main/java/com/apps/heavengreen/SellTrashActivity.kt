package com.apps.heavengreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.apps.heavengreen.application.HeavenGreenDatabase
import com.apps.heavengreen.dao.TrashTransactionDao
import com.apps.heavengreen.databinding.ActivitySellTrashBinding
import com.apps.heavengreen.models.TrashTransactionModel
import com.apps.heavengreen.repositories.TrashTransactionRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class SellTrashActivity : AppCompatActivity(), OnMapReadyCallback {
    private var coordinateLat = ""
    private var coordinateLong = ""
    private lateinit var binding: ActivitySellTrashBinding;
    private lateinit var trashTransactionRepository: TrashTransactionRepository
    private lateinit var trashTransactionDao: TrashTransactionDao
    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var binding: ActivitySellTrashBinding = ActivitySellTrashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        var heavenGreenDatabase = HeavenGreenDatabase.getDatabase(this)
        trashTransactionDao = heavenGreenDatabase.trashTransactionDao()

        trashTransactionRepository = TrashTransactionRepository(trashTransactionDao)



        // Total Text
        val totalValue: TextView = binding.totalValue

        // Inisialisasi spinner jenis sampah
        val trashTypeSpinner: Spinner = binding.trashType
        val trashTypeValues = resources.getStringArray(R.array.trashTypeValues)
        val trashTypeEnum = resources.getStringArray(R.array.trashTypeEnum)
        var priceNow = ""
        var trashTypeEnumVal = ""

         //Hitung total saat jenis sampah berubah
        trashTypeSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                priceNow = trashTypeValues[position]
                trashTypeEnumVal = trashTypeEnum[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak ada tindakan yang dilakukan saat tidak ada item yang dipilih
            }
        })

        val trashWeight: EditText = binding.weight
        var weightNow = 0.0
        trashWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak ada tindakan yang dilakukan sebelum teks berubah
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Tindakan yang dilakukan saat teks berubah
                val weight = s.toString().toDoubleOrNull()

                if (weight != null) {
                    val selectedValue = trashTypeValues[trashTypeSpinner.selectedItemPosition]
                    val total = selectedValue.toDouble() * weight
                    weightNow = total
                    totalValue.text = "Rp. " +  total.toString()
                } else {
                    // Jika berat tidak valid, tampilkan total dengan nilai 0
                    totalValue.text = "Rp. " +  "0"
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak ada tindakan yang dilakukan setelah teks berubah
            }
        })

        // notes pengguna
        var notes: EditText = binding.notes

        // nama sampah
        var trashName: EditText= binding.trashName


        binding.trashSellButton.setOnClickListener {
            val newTrashTransaction = TrashTransactionModel(trash_type = trashTypeEnumVal,
                user_id = 1,
                notes = notes.text.toString(),
                weight = weightNow,
                price = priceNow.toDouble(),
                trash_name = trashName.text.toString(),
                status = "PENDING",
                lat = coordinateLat,
                lang = coordinateLong
                )
            insertTrashTransaction(newTrashTransaction)
            Toast.makeText(this, "Penjualan ditambahkan", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun insertTrashTransaction(trashTransaction: TrashTransactionModel) {
        // Jalankan operasi insert di dalam coroutine
        lifecycleScope.launch {
            trashTransactionRepository.insertTrashTransaction(trashTransaction)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            getLastKnownLocationAndAddMarker()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        mMap.setOnMapClickListener { latLng ->
            moveMarker(latLng)
        }
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}

            override fun onMarkerDrag(marker: Marker) {}

            override fun onMarkerDragEnd(marker: Marker) {
                moveMarker(marker.position)
            }
        })

        mMap.uiSettings.isScrollGesturesEnabled = true
    }

    private fun getLastKnownLocationAndAddMarker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(location.latitude, location.longitude)
                    moveCamera(latLng)
                }
            }
        }
    }

    private fun moveCamera(latLng: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
        addMarker(latLng)
    }

    private fun moveMarker(latLng: LatLng) {
        marker?.position = latLng

        val latitude = latLng.latitude
        val longitude = latLng.longitude

        coordinateLat = latitude.toString()
        coordinateLong = longitude.toString()
    }

    private fun addMarker(latLng: LatLng) {
        marker?.remove()
        marker = mMap.addMarker(MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM = 15f
    }

}
