package com.example.tenantmanagement.data.model

import java.io.Serializable

data class Tenant(
    val name: String,
    val phone: String,
    val rent: String,
) : Serializable {

    fun summary(): String {
        return "Tenant: $name\nPhone: $phone\nRent: Ksh $rent"
    }
}