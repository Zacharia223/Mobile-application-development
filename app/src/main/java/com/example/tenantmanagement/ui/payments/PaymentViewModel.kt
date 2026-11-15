package com.example.tenantmanagement.ui.payments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenantmanagement.data.model.Payment
import com.example.tenantmanagement.data.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PaymentRepository.getInstance(application)
    private val _payments = MutableLiveData<List<Payment>>(emptyList())
    val payments: LiveData<List<Payment>> = _payments

    fun loadPayments() = viewModelScope.launch(Dispatchers.IO) {
        _payments.postValue(repository.getAll())
    }

    fun addPayment(payment: Payment, onComplete: (Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onComplete(repository.insert(payment) != -1L)
        loadPayments()
    }

    fun updatePayment(payment: Payment, onComplete: (Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onComplete(repository.update(payment) > 0)
        loadPayments()
    }

    fun deletePayment(id: Long, onComplete: (Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        onComplete(repository.delete(id) > 0)
        loadPayments()
    }
}
