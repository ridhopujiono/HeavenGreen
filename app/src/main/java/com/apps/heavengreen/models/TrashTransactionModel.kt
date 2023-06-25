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
    val trash_type: TrashType,
    val user_id: Int=0,
    val buyer_id: Int=0,
    val notes: String,
    val weight: Double,
    val price: Double,
    val status: Status
    ): Parcelable

enum class Status {
    SUCCESS,
    CANCELED,
    PENDING
}

enum class TrashType{
    PLASCTIC,
    STEEL,
    PAPER,
    GLASS
}