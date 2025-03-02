package timeswap.application.ui.screens.features.setting

import timeswap.application.ui.shared.components.BottomNavigationBar
import android.content.Context

import androidx.navigation.NavController

import kotlinx.coroutines.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import timeswap.application.network.services.UserRepository
import timeswap.application.ui.screens.features.setting.sections.SettingFormSection

@Composable
fun SettingsScreen(context: Context, navController: NavController) {
    val sharedPreferences = remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
    var fullName by remember { mutableStateOf("Unknown User") }
    var balance by remember { mutableStateOf("₫0") }
    val accessToken = sharedPreferences.getString("accessToken", null)
    val userRepository = remember { UserRepository() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(accessToken) {
        accessToken?.let { token ->
            coroutineScope.launch {
                val user = userRepository.getUserProfile(token)
                fullName = user?.fullName ?: "Unknown User"
                balance = (user?.balance ?: "0").toString()
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SettingFormSection(fullName, balance, userRepository, navController, sharedPreferences)
        }
    }
}