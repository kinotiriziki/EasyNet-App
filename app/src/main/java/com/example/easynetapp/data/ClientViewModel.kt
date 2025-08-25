package com.example.easynetapp.data


import androidx.lifecycle.ViewModel
import com.example.easynetapp.models.Client
import com.example.easynetapp.models.Provider
import com.example.easynetapp.models.BookingStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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


    fun bookProvider(
        provider: Provider,
        clientName: String,
        locationType: String,
        isp: String,
        onResult: (Boolean) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return onResult(false)

        val bookingId = db.child("bookings").push().key ?: return onResult(false)

        val booking = mapOf(
            "bookingId" to bookingId,
            "clientId" to uid,
            "clientName" to clientName,
            "locationType" to locationType,
            "isp" to isp,
            "providerId" to provider.providerId,
            "providerName" to provider.name,
            "status" to BookingStatus.PENDING
        )

        db.child("bookings").child(bookingId).setValue(booking)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }
}
