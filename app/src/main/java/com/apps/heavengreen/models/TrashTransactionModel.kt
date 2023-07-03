package com.apps.heavengreen.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "trash_transactions")
@Parcelize
data class TrashTransactionModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val trash_type: String,
    val user_id: Int = 0,
    val notes: String,
    val trash_name: String,
    val weight: Double,
    val price: Double,
    val status: String
) : Parcelable {
    override fun toString(): String {

        return "Nama Sampah: $trash_name\n"+
                "Status: $status"
    }
}