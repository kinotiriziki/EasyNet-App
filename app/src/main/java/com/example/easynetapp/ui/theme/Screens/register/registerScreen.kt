package com.example.easynetapp.ui.theme.Screens.register


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.easynetapp.R
import com.example.easynetapp.data.AuthViewModel
import com.example.easynetapp.navigation.Route_LOGIN


@Composable
fun RegisterScreen(navController: NavController,authViewModel: AuthViewModel) {
    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    var role by remember { mutableStateOf("client") }


    Box(){
        Image(painter = painterResource(id = R.drawable.design),
            contentDescription = "Image background",
            contentScale = ContentScale.FillBounds)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(15.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("EasyNet Registration",
            style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Card(shape = CircleShape,
            modifier = Modifier
                .padding(10.dp)
                .size(75.dp))
        {Image(painter = painterResource(id = R.drawable.logo),
            contentDescription = "Image logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentScale = ContentScale.Fit)  }


        OutlinedTextField(
            value = fullname,
            onValueChange = { fullname = it },
            label = { Text("Fullname") },
            placeholder = { Text("Enter Fullname") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
            modifier = Modifier.fillMaxWidth(0.8f)

        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Enter Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            modifier = Modifier.fillMaxWidth(0.8f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Enter Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon") },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            placeholder = { Text("Enter Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon") },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation()
        )


        Row {
            RadioButton(
                selected = role == "client",
                onClick = { role = "client" }
            )
            Text("Client")

            Spacer(Modifier.width(16.dp))

            RadioButton(
                selected = role == "provider",
                onClick = { role = "provider" }
            )
            Text("Provider")
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.register(fullname,email, password,confirmPassword,role) { success, errorMsg ->
                if (success) {
                   Toast.makeText(context,"Registration Successful", Toast.LENGTH_LONG).show()
                    if (role == "client") {
                        navController.navigate("client_home"){popUpTo(0)}
                    }else{
                        navController.navigate("provider_home"){popUpTo(0)}
                    }

                } else {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        }) {
            Text("Register")
        }


        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Already have an account? Login",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.clickable {navController.navigate(Route_LOGIN)  }
        )

        if (loading) {
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator()
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}

