package com.apps.heavengreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.apps.heavengreen.application.HeavenGreenDatabase
import com.apps.heavengreen.databinding.ActivityTrashTransactionHistoriesBinding
import com.apps.heavengreen.models.TrashTransactionModel
import com.apps.heavengreen.repositories.TrashTransactionRepository
import kotlinx.coroutines.launch

class TrashTransactionHistories : AppCompatActivity() {
    private lateinit var binding: ActivityTrashTransactionHistoriesBinding
    private lateinit var transactionListAdapter: ArrayAdapter<TrashTransactionModel>
    private lateinit var trashTransactionRepository: TrashTransactionRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashTransactionHistoriesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Inisialisasi repository dan model database
        val heavenGreenDatabase = HeavenGreenDatabase.getDatabase(this)
        val transactionDao = heavenGreenDatabase.trashTransactionDao()
        trashTransactionRepository = TrashTransactionRepository(transactionDao)

        // Inisialisasi Adapter dan ListView
        transactionListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = transactionListAdapter

        // Panggil metode untuk mengambil dan menampilkan data transaksi sampah
        fetchTransactionData()

        // Set listener untuk klik item pada ListView
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            val transaction = transactionListAdapter.getItem(position)

            // Tampilkan dialog konfirmasi delete atau edit
            showOptionsDialog(transaction)
        }

    }
    private fun fetchTransactionData() {
        // Menggunakan repository untuk mengambil data transaksi sampah dari Room Database
        getAllTrashTransaction()
    }

    private fun getAllTrashTransaction() {
        lifecycleScope.launch {
            val transactions = trashTransactionRepository.getAllTrashTransaction()

            // Hapus data lama dari adapter
            transactionListAdapter.clear()

            // Tambahkan data baru dengan format yang sesuai
            for (transaction in transactions) {
                transactionListAdapter.add(transaction)
            }

            // Perbarui tampilan ListView
            transactionListAdapter.notifyDataSetChanged()
        }
    }

    private fun showOptionsDialog(transaction: TrashTransactionModel?) {
        val options = arrayOf("Delete", "Edit")

        AlertDialog.Builder(this)
            .setTitle("Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> deleteTransaction(transaction)
                    1 -> editTransaction(transaction)
                }
            }
            .show()
    }


    private fun deleteTransaction(transaction: TrashTransactionModel?) {
        if (transaction != null) {
            lifecycleScope.launch {
                trashTransactionRepository.deleteTrashTransaction(transaction)

                // Refresh data setelah delete
                getAllTrashTransaction()
            }
        }
    }

    private fun editTransaction(transaction: TrashTransactionModel?) {
        if (transaction != null) {
            // Implementasi aksi edit sesuai kebutuhan Anda
            // Misalnya, buka activity atau dialog untuk mengedit data transaksi
            // Setelah proses edit selesai, Anda dapat memanggil getAllTrashTransaction() untuk me-refresh data
        }
    }
}