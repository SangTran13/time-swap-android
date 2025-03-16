package timeswap.application.ui.screens.features.home

import androidx.navigation.NavController

import android.content.Context
import android.content.SharedPreferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import timeswap.application.ui.shared.components.BottomNavigationBar

import timeswap.application.network.services.UserRepository
import timeswap.application.viewmodel.ProfileViewModel

import timeswap.application.ui.screens.features.home.sections.UserInfoSection
import timeswap.application.ui.screens.features.home.sections.BannerSection
import timeswap.application.ui.screens.features.home.sections.JobCategorySection
import timeswap.application.ui.screens.features.home.sections.LocationMap
import timeswap.application.ui.utils.ApiUtils

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = remember {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    val accessToken = sharedPreferences.getString("accessToken", "") ?: ""

    val userRepository = remember { UserRepository() }
    val profileViewModel = remember { ProfileViewModel(userRepository) }

    val userProfile by profileViewModel.userProfile.collectAsState()

    LaunchedEffect(Unit) {
        if (ApiUtils.checkExpiredToken(context)) {
            ApiUtils.clearAccessToken(context)
            navController.navigate("login")
            return@LaunchedEffect
        }
        if (accessToken.isNotEmpty()) {
            profileViewModel.loadUserProfile(accessToken)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            userProfile?.let { UserInfoSection(it.fullName, it.avatarUrl) }
            Spacer(modifier = Modifier.height(16.dp))
            BannerSection()
            Spacer(modifier = Modifier.height(16.dp))
            JobCategorySection()
            Spacer(modifier = Modifier.height(16.dp))
            LocationMap(context)
            Spacer(modifier = Modifier.weight(1f))
        }
    }

}