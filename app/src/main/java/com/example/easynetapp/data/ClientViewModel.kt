package com.example.easynetapp.data

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.easynetapp.models.Client
import com.example.easynetapp.models.Provider

class ClientViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference


    fun listenForProviders(onResult: (List<Provider>) -> Unit) {
        db.child("providers").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Provider::class.java) }
                onResult(list)
            }
            override fun onCancelled(error: DatabaseError) {
                onResult(emptyList())
            }
        })
    }


    fun bookProvider(provider: Provider, onResult: (Boolean) -> Unit) {
        val bookingId = db.child("clients").push().key ?: return
        val booking = Client(
            bookingId = bookingId,
            clientId = auth.currentUser?.uid ?: "",
            clientName = auth.currentUser?.email ?: "Unknown",
            providerId = provider.providerId,
            status = "pending"
        )
        db.child("clients").child(bookingId).setValue(booking)
            .addOnCompleteListener { task -> onResult(task.isSuccessful) }
    }


    fun listenForProviderBookings(onResult: (List<Client>) -> Unit) {
        val providerId = auth.currentUser?.uid ?: return
        db.child("clients").orderByChild("providerId").equalTo(providerId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull { it.getValue(Client::class.java) }
                    onResult(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    onResult(emptyList())
                }
            })
    }


    fun updateBookingStatus(bookingId: String, status: String, onResult: (Boolean) -> Unit) {
        db.child("clients").child(bookingId).child("status")
            .setValue(status)
            .addOnCompleteListener { task -> onResult(task.isSuccessful) }
    }
}

