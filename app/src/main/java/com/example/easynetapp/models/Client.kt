package com.example.easynetapp.models

data class Client(
    val bookingId: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val providerId: String = "",
    val status: String = "pending"
)

