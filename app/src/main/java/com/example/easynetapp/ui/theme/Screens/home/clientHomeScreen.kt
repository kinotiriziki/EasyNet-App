package com.example.easynetapp.ui.theme.Screens.home


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.easynetapp.models.Provider
import com.example.easynetapp.data.ClientViewModel
import com.google.firebase.auth.FirebaseAuth



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(
    viewModel: ClientViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var providers by remember { mutableStateOf(listOf<Provider>()) }
    var clientName by remember { mutableStateOf("") }
    var locationType by remember { mutableStateOf("Home") }
    var selectedIsp by remember { mutableStateOf("") }
    val ispList = listOf("Safaricom", "Airtel", "Faiba", "Telkom")


    LaunchedEffect(Unit) {
        viewModel.listenForProviders { providers = it }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Available Providers", style = MaterialTheme.typography.titleLarge)
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            ) {
                Text("Logout")
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = clientName,
            onValueChange = { clientName = it },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = locationType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Location Type") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Home") }, onClick = {
                    locationType = "Home"
                    expanded = false
                })
                DropdownMenuItem(text = { Text("Office") }, onClick = {
                    locationType = "Office"
                    expanded = false
                })
            }
        }

        Spacer(Modifier.height(8.dp))

        var ispExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = ispExpanded,
            onExpandedChange = { ispExpanded = !ispExpanded }) {
            OutlinedTextField(
                value = selectedIsp,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select ISP") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ispExpanded) }
            )
            ExposedDropdownMenu(
                expanded = ispExpanded,
                onDismissRequest = { ispExpanded = false }) {
                ispList.forEach { isp ->
                    DropdownMenuItem(
                        text = { Text(isp) },
                        onClick = {
                            selectedIsp = isp
                            ispExpanded = false
                        }
                    )
                }
            }

        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(providers.filter { selectedIsp.isBlank() || it.isp == selectedIsp }) { provider ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            if (clientName.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Enter your name first!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@clickable
                            }
                            if (selectedIsp.isBlank()) {
                                Toast.makeText(context, "Select an ISP!", Toast.LENGTH_SHORT).show()
                                return@clickable
                            }

                            viewModel.bookProvider(
                                provider = provider,
                                clientName = clientName,
                                locationType = locationType,
                                isp = selectedIsp
                            ) { success ->
                                Toast.makeText(
                                    context,
                                    if (success) "Booking Successful!" else "Booking Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(provider.name, style = MaterialTheme.typography.titleMedium)
                        Text("ISP: ${provider.isp}")
                        Text("Experience: ${provider.experience} yrs")
                    }
                }
            }
        }
    }
}

