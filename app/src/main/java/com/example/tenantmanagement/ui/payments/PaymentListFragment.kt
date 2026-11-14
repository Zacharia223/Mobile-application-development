package com.example.tenantmanagement.ui.payments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tenantmanagement.R
import com.example.tenantmanagement.data.model.Payment
import com.example.tenantmanagement.databinding.FragmentPaymentListBinding
import com.example.tenantmanagement.worker.RentReminderWorker
import java.util.concurrent.TimeUnit

class PaymentListFragment : Fragment() {
    private var _binding: FragmentPaymentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PaymentViewModel
    private var payments: List<Payment> = emptyList()
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _binding = FragmentPaymentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, PaymentViewModelFactory(requireActivity().application))[PaymentViewModel::class.java]
        requestNotificationPermission()
        scheduleReminderWorker()
        binding.addPaymentButton.setOnClickListener {
            findNavController().navigate(R.id.action_paymentListFragment_to_addPaymentFragment)
        }
        viewModel.payments.observe(viewLifecycleOwner) { items ->
            payments = items
            binding.emptyTextView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            binding.paymentListView.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
            binding.paymentListView.adapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_2, android.R.id.text1,
                items.map { "${it.tenantName} - KSh ${"%.2f".format(it.amount)}" }
            )
        }
        binding.paymentListView.setOnItemClickListener { _, _, position, _ ->
            val payment = payments[position]
            findNavController().navigate(PaymentListFragmentDirections.actionPaymentListFragmentToAddPaymentFragment(payment))
        }
        binding.paymentListView.setOnItemLongClickListener { _, _, position, _ ->
            confirmDelete(payments[position])
            true
        }
        viewModel.loadPayments()
    }

    private fun scheduleReminderWorker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                RentReminderWorker.CHANNEL_ID,
                "Rent payment reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            requireContext().getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
        val request = PeriodicWorkRequestBuilder<RentReminderWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "rent_payment_reminders",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun confirmDelete(payment: Payment) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete payment?")
            .setMessage("Remove the payment for ${payment.tenantName}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deletePayment(payment.id) { success ->
                    activity?.runOnUiThread {
                        if (!isAdded || _binding == null) return@runOnUiThread
                        Toast.makeText(requireContext(), if (success) "Payment deleted" else "Delete failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
