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
import com.example.easynetapp.data.ClientViewModel
import com.example.easynetapp.models.Provider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(navController: NavController, viewModel: ClientViewModel) {
    val context = LocalContext.current
    val providers = remember { mutableStateListOf<Provider>() }


    LaunchedEffect(Unit) {
        viewModel.listenForProviders { list ->
            providers.clear()
            providers.addAll(list)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Available Providers") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(providers) { provider ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            viewModel.bookProvider(provider) { success ->
                                Toast.makeText(
                                    context,
                                    if (success) "Booking Sent" else "Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(text = provider.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = provider.service, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

