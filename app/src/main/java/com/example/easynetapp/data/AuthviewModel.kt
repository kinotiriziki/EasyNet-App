package com.example.easynetapp.data

import androidx.lifecycle.ViewModel
import com.example.easynetapp.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?, String?) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onResult(false, "Email and password required", null)
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener


                    val dbRef = FirebaseDatabase.getInstance().getReference("users")

                    dbRef.child("clients").child(uid).get()
                        .addOnSuccessListener { clientSnap ->
                            if (clientSnap.exists()) {
                                onResult(true, null, "client")
                            } else {
                                dbRef.child("providers").child(uid).get()
                                    .addOnSuccessListener { providerSnap ->
                                        if (providerSnap.exists()) {
                                            onResult(true, null, "provider")
                                        } else {
                                            onResult(false, "User role not found", null)
                                        }
                                    }
                            }
                        }
                } else {
                    onResult(false, task.exception?.message ?: "Login failed", null)
                }
            }
    }


    fun register(
        fullname: String,
        email: String,
        password: String,
        confirmpassword: String,
        role: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (fullname.isBlank() || email.isBlank() || password.isBlank() || confirmpassword.isBlank() || role.isBlank()) {
            onResult(false, "All fields are required")
            return
        }
        if (password != confirmpassword) {
            onResult(false, "Passwords do not match")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = UserModel(
                        userId= userId,
                        fullname= fullname,
                        email = email,
                        role = role)

                    saveUserToDatabase(user,onResult)

                } else {
                    onResult(false, task.exception?.message ?: "Registration failed")
                }
            }
    }


    private fun saveUserToDatabase(
        user: UserModel,
        onResult: (Boolean, String?) -> Unit
    ) {
        val rolePath = if (user.role == "client") "clients" else "providers"

        val dbRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(rolePath)
            .child(user.userId)

        dbRef.setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message ?: "Failed to save user")
                }
            }
    }


    fun getUserRole(onResult: (String?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onResult(null)
            return
        }

        FirebaseDatabase.getInstance().getReference("users")
            .child(userId)
            .child("role")
            .get()
            .addOnSuccessListener { snapshot ->
                val role = snapshot.getValue(String::class.java)
                onResult(role)
            }
            .addOnFailureListener { e ->
                onResult(null)
            }
    }


    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }


}

