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
import androidx.navigation.NavController
import com.example.easynetapp.data.ClientViewModel
import com.example.easynetapp.models.Client


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderHomeScreen(navController: NavController, viewModel: ClientViewModel) {
    val context = LocalContext.current
    val bookings = remember { mutableStateListOf<Client>() }


    LaunchedEffect(Unit) {
        viewModel.listenForProviderBookings { list ->
            bookings.clear()
            bookings.addAll(list)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Bookings") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(bookings) { booking ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Client: ${booking.clientName}")
                        Text("Status: ${booking.status}")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = {
                                viewModel.updateBookingStatus(booking.bookingId, "approved") {
                                    Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show()
                                }
                            }) { Text("Accept") }

                            Button(onClick = {
                                viewModel.updateBookingStatus(booking.bookingId, "rejected") {
                                    Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show()
                                }
                            }) { Text("Reject") }
                        }
                    }
                }
            }
        }
    }
}


