package com.apps.heavengreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
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
        setContentView(R.layout.activity_trash_transaction_histories)

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
}