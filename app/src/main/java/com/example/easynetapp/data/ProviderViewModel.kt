package com.example.easynetapp.data

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.easynetapp.models.Booking


class ProviderViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference


    fun listenForProviderBookings(onResult: (List<Booking>) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        db.child("bookings")
            .orderByChild("providerId")
            .equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull { it.getValue(Booking::class.java) }
                    onResult(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(emptyList())
                }
            })
    }


    fun updateBookingFeedback(
        bookingId: String,
        status: String,
        estimatedTime: String,
        cost: String
    ) {
        val updates = mapOf(
            "status" to status,
            "estimatedTime" to estimatedTime,
            "cost" to cost
        )
        db.child("bookings").child(bookingId).updateChildren(updates)
    }

    fun deleteClient(bookingId: String) {
        db.child("bookings").child(bookingId).removeValue()
    }


}
