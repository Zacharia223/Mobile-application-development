package com.example.tenantmanagement.data.repository

import android.content.Context
import com.example.tenantmanagement.data.local.PaymentDatabaseHelper
import com.example.tenantmanagement.data.model.Payment

class PaymentRepository private constructor(context: Context) {
    private val database = PaymentDatabaseHelper(context.applicationContext)

    fun getAll(): List<Payment> = database.getAll()
    fun insert(payment: Payment): Long = database.insert(payment)
    fun update(payment: Payment): Int = database.update(payment)
    fun delete(id: Long): Int = database.delete(id)

    companion object {
        @Volatile private var instance: PaymentRepository? = null

        fun getInstance(context: Context): PaymentRepository =
            instance ?: synchronized(this) {
                instance ?: PaymentRepository(context).also { instance = it }
            }
    }
}
