package com.example.easynetapp.ui.theme.Screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.easynetapp.data.ProviderViewModel

import com.google.firebase.auth.FirebaseAuth
import com.example.easynetapp.models.Booking



@Composable
fun ProviderHomeScreen(viewModel: ProviderViewModel , navController: NavController) {
    val context = LocalContext.current
    var bookings by remember { mutableStateOf(listOf<Booking>()) }


    LaunchedEffect(Unit) {
        viewModel.listenForProviderBookings { bookings = it }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("My Client Bookings", style = MaterialTheme.typography.titleLarge)

        LazyColumn {
            items(bookings) { booking ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Client: ${booking.clientName}", style = MaterialTheme.typography.titleMedium)
                        Text("Location: ${booking.locationType}")
                        Text("ISP: ${booking.isp}")
                        Text("Status: ${booking.status}")
                        if (booking.estimatedTime.isNotEmpty()) {
                            Text("Estimated Time: ${booking.estimatedTime}")
                        }
                        if (booking.cost.isNotEmpty()) {
                            Text("Cost: ${booking.cost}")
                        }

                        Spacer(Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            Button(onClick = {
                                viewModel.updateBookingFeedback(
                                    booking.bookingId,
                                    "APPROVED",
                                    booking.estimatedTime,
                                    booking.cost
                                )
                                Toast.makeText(context, "Booking accepted", Toast.LENGTH_SHORT).show()
                            }) {
                                Text("Accept")
                            }


                            Button(onClick = {
                                viewModel.updateBookingFeedback(
                                    booking.bookingId,
                                    "REJECTED",
                                    booking.estimatedTime,
                                    booking.cost
                                )
                                Toast.makeText(context, "Booking rejected", Toast.LENGTH_SHORT).show()
                            }) {
                                Text("Reject")
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            Button(onClick = {
                                viewModel.updateBookingFeedback(
                                    booking.bookingId,
                                    booking.status,
                                    estimatedTime = "2 hours",
                                    cost = "Ksh 1500"
                                )
                                Toast.makeText(context, "Updated client details", Toast.LENGTH_SHORT).show()
                            }) {
                                Text("Update")
                            }


                            Button(onClick = {
                                viewModel.updateBookingFeedback(
                                    booking.bookingId,
                                    "COMPLETED",
                                    booking.estimatedTime,
                                    booking.cost
                                )
                                Toast.makeText(context, "Marked as attended", Toast.LENGTH_SHORT).show()
                            }) {
                                Text("Attended")
                            }


                            Button(onClick = {
                                viewModel.deleteClient(booking.bookingId)
                                Toast.makeText(context, "Booking deleted", Toast.LENGTH_SHORT).show()
                            }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))


        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("providerHome") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
