package com.example.tenantmanagement.ui.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tenantmanagement.R
import com.example.tenantmanagement.data.model.Payment
import com.example.tenantmanagement.databinding.FragmentPaymentListBinding

class PaymentListFragment : Fragment() {
    private var _binding: FragmentPaymentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PaymentViewModel
    private var payments: List<Payment> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _binding = FragmentPaymentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, PaymentViewModelFactory(requireActivity().application))[PaymentViewModel::class.java]
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

    private fun confirmDelete(payment: Payment) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete payment?")
            .setMessage("Remove the payment for ${payment.tenantName}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deletePayment(payment.id) { success ->
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), if (success) "Payment deleted" else "Delete failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
