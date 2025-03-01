package timeswap.application.ui.screens.features.setting

import timeswap.application.ui.shared.components.BottomNavigationBar
import android.content.Context

import androidx.navigation.NavController

import kotlinx.coroutines.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import timeswap.application.R
import timeswap.application.network.services.UserRepository
import timeswap.application.ui.screens.core.navigation.ChangePasswordDestination
import timeswap.application.ui.screens.core.navigation.LoginDestination
import timeswap.application.ui.screens.core.navigation.PaymentDestination
import timeswap.application.ui.screens.core.navigation.ProfileDestination

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

            SettingItemWithValue(
                icon = R.drawable.ic_profile,
                title = "Profile",
                value = fullName,
                onClick = { navController.navigate(ProfileDestination.route)}
            )

            SettingItemWithValue(
                icon = R.drawable.ic_budget,
                title = "Balance",
                value = balance,
                onClick = { navController.navigate(PaymentDestination.route) }
            )

            SettingItem(
                icon = R.drawable.ic_change_p,
                title = "Change Password",
                onClick = { navController.navigate(ChangePasswordDestination.route) }
            )

            SettingItem(
                icon = R.drawable.ic_logout,
                title = "Logout",
                onClick = {
                    coroutineScope.launch {
                        userRepository.logout(sharedPreferences)
                        navController.navigate(LoginDestination.route) {
                            popUpTo(LoginDestination.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SettingItem(icon: Int, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(id = icon), contentDescription = title, tint = Color.Black, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))
        Icon(painterResource(id = R.drawable.ic_next_page), contentDescription = "Next", tint = Color.Gray, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun SettingItemWithValue(icon: Int, title: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(id = icon), contentDescription = title, tint = Color.Black, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(painterResource(id = R.drawable.ic_next_page), contentDescription = "Next", tint = Color.Gray, modifier = Modifier.size(20.dp))
    }
}