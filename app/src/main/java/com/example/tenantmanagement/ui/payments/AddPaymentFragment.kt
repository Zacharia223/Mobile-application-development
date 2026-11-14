package com.example.tenantmanagement.ui.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tenantmanagement.data.model.Payment
import com.example.tenantmanagement.databinding.FragmentAddPaymentBinding

class AddPaymentFragment : Fragment() {
    private var _binding: FragmentAddPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PaymentViewModel
    private val args: AddPaymentFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _binding = FragmentAddPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity(), PaymentViewModelFactory(requireActivity().application))[PaymentViewModel::class.java]
        val existing = args.payment
        binding.titleTextView.text = if (existing == null) "Add Rent Payment" else "Edit Rent Payment"
        existing?.let {
            binding.tenantEditText.setText(it.tenantName)
            binding.amountEditText.setText(it.amount.toString())
            binding.dateEditText.setText(it.paymentDate)
            binding.statusEditText.setText(it.status)
            binding.notesEditText.setText(it.notes)
        }
        binding.savePaymentButton.setOnClickListener { save(existing) }
    }

    private fun save(existing: Payment?) {
        val tenant = binding.tenantEditText.text.toString().trim()
        val amountText = binding.amountEditText.text.toString().trim()
        val date = binding.dateEditText.text.toString().trim()
        val status = binding.statusEditText.text.toString().trim().ifEmpty { "Paid" }
        val notes = binding.notesEditText.text.toString().trim()
        val amount = amountText.toDoubleOrNull()
        if (tenant.isEmpty() || amount == null || amount <= 0 || date.isEmpty()) {
            binding.validationTextView.text = "Enter a tenant, a valid amount, and a payment date."
            binding.validationTextView.visibility = View.VISIBLE
            return
        }
        binding.validationTextView.visibility = View.GONE
        val payment = Payment(existing?.id ?: 0, tenant, amount, date, status, notes)
        val complete: (Boolean) -> Unit = { success ->
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), if (success) "Payment saved" else "Unable to save payment", Toast.LENGTH_SHORT).show()
                if (success) findNavController().popBackStack()
            }
        }
        if (existing == null) viewModel.addPayment(payment, complete) else viewModel.updatePayment(payment, complete)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
