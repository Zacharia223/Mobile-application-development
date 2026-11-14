package com.example.tenantmanagement.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tenantmanagement.R
import com.example.tenantmanagement.data.local.PaymentDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RentReminderWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val overduePayments = PaymentDatabaseHelper(applicationContext)
            .getAll()
            .count {
                (it.status.equals("Pending", ignoreCase = true) || it.status.equals("Overdue", ignoreCase = true)) &&
                    it.paymentDate <= today
            }

        if (overduePayments > 0) {
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Rent payment reminder")
                .setContentText("You have $overduePayments pending or overdue rent payment(s).")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
            try {
                NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
            } catch (_: SecurityException) {
                return Result.success()
            }
        }
        return Result.success()
    }

    companion object {
        const val CHANNEL_ID = "rent_payment_reminders"
        private const val NOTIFICATION_ID = 1001
    }
}
