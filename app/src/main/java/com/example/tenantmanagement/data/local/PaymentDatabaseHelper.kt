package com.example.tenantmanagement.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tenantmanagement.data.model.Payment

class PaymentDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_PAYMENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TENANT TEXT NOT NULL,
                $COLUMN_AMOUNT REAL NOT NULL,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_STATUS TEXT NOT NULL,
                $COLUMN_NOTES TEXT NOT NULL DEFAULT ''
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PAYMENTS")
        onCreate(db)
    }

    fun insert(payment: Payment): Long {
        return writableDatabase.insert(TABLE_PAYMENTS, null, payment.toValues())
    }

    fun getAll(): List<Payment> {
        val result = mutableListOf<Payment>()
        readableDatabase.query(
            TABLE_PAYMENTS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_DATE DESC, $COLUMN_ID DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) result.add(cursor.toPayment())
        }
        return result
    }

    fun update(payment: Payment): Int {
        return writableDatabase.update(
            TABLE_PAYMENTS,
            payment.toValues(),
            "$COLUMN_ID = ?",
            arrayOf(payment.id.toString())
        )
    }

    fun delete(id: Long): Int {
        return writableDatabase.delete(
            TABLE_PAYMENTS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    private fun Payment.toValues() = ContentValues().apply {
        put(COLUMN_TENANT, tenantName)
        put(COLUMN_AMOUNT, amount)
        put(COLUMN_DATE, paymentDate)
        put(COLUMN_STATUS, status)
        put(COLUMN_NOTES, notes)
    }

    private fun android.database.Cursor.toPayment() = Payment(
        id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
        tenantName = getString(getColumnIndexOrThrow(COLUMN_TENANT)),
        amount = getDouble(getColumnIndexOrThrow(COLUMN_AMOUNT)),
        paymentDate = getString(getColumnIndexOrThrow(COLUMN_DATE)),
        status = getString(getColumnIndexOrThrow(COLUMN_STATUS)),
        notes = getString(getColumnIndexOrThrow(COLUMN_NOTES))
    )

    companion object {
        private const val DATABASE_NAME = "rentease.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PAYMENTS = "payments"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TENANT = "tenant_name"
        private const val COLUMN_AMOUNT = "amount"
        private const val COLUMN_DATE = "payment_date"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_NOTES = "notes"
    }
}
