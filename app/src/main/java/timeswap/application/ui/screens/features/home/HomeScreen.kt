package timeswap.application.ui.screens.features.home

import android.content.Context

import kotlinx.coroutines.launch

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import timeswap.application.network.services.UserRepository

@Composable
fun HomeScreen(context: Context, onLogout: () -> Unit, onNavigateToPayment: () -> Unit) {
    val sharedPreferences = remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
    var fullName by remember { mutableStateOf("Unknown User") }
    var balance by remember { mutableDoubleStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }

    val accessToken = sharedPreferences.getString("accessToken", null)
    val userRepository = remember { UserRepository() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(accessToken) {
        accessToken?.let { token ->
            coroutineScope.launch {
                val user = userRepository.getUserProfile(token)
                fullName = user?.fullName ?: "Unknown User"
                balance = user?.balance ?: 0.0
                isLoading = false
            }
        } ?: run { isLoading = false }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (isLoading) "Loading..." else "Welcome to TimeSwap, hello $fullName!",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Balance: $balance",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onNavigateToPayment) {
                Text(text = "Go to Payment")
            }

            Spacer(modifier = Modifier.height(10.dp))


            Button(onClick = {
                userRepository.logout(sharedPreferences)
                onLogout()
            }) {
                Text(text = "LOGOUT")
            }
        }
    }
}
