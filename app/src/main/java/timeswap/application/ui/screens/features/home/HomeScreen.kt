package timeswap.application.ui.screens.features.home

import android.content.Context
import android.content.SharedPreferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import timeswap.application.network.RetrofitClient

@Composable
fun HomeScreen(context: Context, onLogout: () -> Unit) {
    val sharedPreferences =
        remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
    var fullName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val accessToken = sharedPreferences.getString("accessToken", null)

    LaunchedEffect(accessToken) {
        accessToken?.let {
            fetchUserProfile(it) { name ->
                fullName = name ?: "Unknown User"
                isLoading = false
            }
        } ?: run { isLoading = false }
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (isLoading) "Loading..." else "Welcome to TimeSwap, hello $fullName!",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                logout(sharedPreferences)
                onLogout()
            }) {
                Text(text = "LOGOUT")
            }
        }
    }
}


fun fetchUserProfile(accessToken: String, onResult: (String?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitClient.userService.getUserProfile("Bearer $accessToken")
            if (response.isSuccessful && response.body()?.statusCode == 1000) {
                val fullName = response.body()?.data?.fullName
                withContext(Dispatchers.Main) { onResult(fullName) }
            } else {
                withContext(Dispatchers.Main) { onResult(null) }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { onResult(null) }
        }
    }
}

fun logout(sharedPreferences: SharedPreferences) {
    with(sharedPreferences.edit()) {
        remove("accessToken")
        remove("refreshToken")
        remove("expiresAt")
        apply()
    }
}

