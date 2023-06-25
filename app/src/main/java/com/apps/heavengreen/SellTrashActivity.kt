package com.apps.heavengreen

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
import com.apps.heavengreen.databinding.ActivitySellTrashBinding

class SellTrashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellTrashBinding;
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var binding: ActivitySellTrashBinding = ActivitySellTrashBinding.inflate(layoutInflater)

        setContentView(binding.root)


        // Total Text
        val totalValue: TextView = binding.totalValue

        // Inisialisasi spinner jenis sampah
        val trashTypeSpinner: Spinner = binding.trashType
        val trashTypeValues = resources.getStringArray(R.array.trashTypeValues)
        var priceNow = ""

         //Hitung total saat jenis sampah berubah
        trashTypeSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                priceNow = trashTypeValues[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak ada tindakan yang dilakukan saat tidak ada item yang dipilih
            }
        })

        val trashWeight: EditText = binding.weight
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

    }
}