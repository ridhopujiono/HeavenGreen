package com.apps.heavengreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.apps.heavengreen.application.HeavenGreenDatabase
import com.apps.heavengreen.dao.TrashTransactionDao
import com.apps.heavengreen.databinding.ActivityEditTransactionBinding
import com.apps.heavengreen.models.TrashTransactionModel
import com.apps.heavengreen.repositories.TrashTransactionRepository
import kotlinx.coroutines.launch

class EditTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTransactionBinding
    private var transaction: TrashTransactionModel? = null
    private lateinit var trashTransactionRepository: TrashTransactionRepository
    private lateinit var trashTransactionDao: TrashTransactionDao

    private var trashType = ""
    private var idUser = 0
    private var weight = 0.0
    private var price = 0.0
    private var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        var heavenGreenDatabase = HeavenGreenDatabase.getDatabase(this)
        trashTransactionDao = heavenGreenDatabase.trashTransactionDao()

        trashTransactionRepository = TrashTransactionRepository(trashTransactionDao)


        // Mendapatkan data transaksi dari intent
        transaction = intent.getParcelableExtra("transaction")

        // Mengisi data transaksi ke dalam input fields

        var notesEdit:EditText = binding.notesEdit
        var trashNameEdit:EditText = binding.trashNameEdit

        transaction?.let {
            notesEdit.setText(it.notes)
            trashNameEdit.setText(it.trash_name)
            trashType = it.trash_type
            idUser = it.user_id
            weight = it.weight
            price = it.price
            status = it.status
        }

        // Set listener untuk tombol simpan
        binding.btnSave.setOnClickListener {
            saveTransactionChanges()
        }
    }

    private fun saveTransactionChanges() {
        // notes pengguna
        var notes: EditText = binding.notesEdit

        // nama sampah
        var trashName: EditText = binding.trashNameEdit

        // Validasi data transaksi (opsional)

        // Membuat objek transaksi baru dengan data yang diperbarui

        val updatedTransaction = TrashTransactionModel(
            id = transaction?.id ?: 0,
            notes = notes.text.toString(),
            trash_name = trashName.text.toString(),
            trash_type = trashType,
            user_id = idUser.toInt(),
            weight = weight.toDouble(),
            price = price,
            status = status
        )

        // Menyimpan perubahan transaksi ke dalam database (atau proses penyimpanan sesuai kebutuhan aplikasi)
        updateTrashTransaction(updatedTransaction)
        Toast.makeText(this, "Penjualan dirubah", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    private fun updateTrashTransaction(trashTransaction: TrashTransactionModel) {
        // Jalankan operasi update di dalam coroutine
        lifecycleScope.launch {
            trashTransactionRepository.updateTrashTransaction(trashTransaction)
        }
    }
}
