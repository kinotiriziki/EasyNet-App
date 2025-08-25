package com.example.easynetapp.models

data class Booking(
    val bookingId: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val locationType: String = "",
    val isp: String = "",
    val providerId: String = "",
    val providerName: String = "",
    val status: String = BookingStatus.PENDING,
    val estimatedTime: String = "",
    val cost: String = ""
)



