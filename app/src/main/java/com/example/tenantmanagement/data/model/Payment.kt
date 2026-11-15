package com.example.tenantmanagement.data.model

import java.io.Serializable

data class Payment(
    val id: Long = 0,
    val tenantName: String,
    val amount: Double,
    val paymentDate: String,
    val status: String,
    val notes: String = ""
) : Serializable
