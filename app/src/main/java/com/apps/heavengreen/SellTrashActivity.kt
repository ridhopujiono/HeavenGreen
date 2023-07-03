package com.apps.heavengreen

import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import com.apps.heavengreen.application.HeavenGreenDatabase
import com.apps.heavengreen.dao.TrashTransactionDao
import com.apps.heavengreen.databinding.ActivitySellTrashBinding
import com.apps.heavengreen.models.TrashTransactionModel
import com.apps.heavengreen.repositories.TrashTransactionRepository
import kotlinx.coroutines.launch

class SellTrashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellTrashBinding;
    private lateinit var trashTransactionRepository: TrashTransactionRepository
    private lateinit var trashTransactionDao: TrashTransactionDao


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var binding: ActivitySellTrashBinding = ActivitySellTrashBinding.inflate(layoutInflater)

        setContentView(binding.root)


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
                status = "PENDING")
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
}
