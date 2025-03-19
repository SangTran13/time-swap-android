package timeswap.application.ui.screens.features.profiles

import android.content.Context
import android.content.SharedPreferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.navigation.NavController

import timeswap.application.R
import timeswap.application.network.services.UserRepository

import timeswap.application.ui.screens.core.navigation.EditAboutMeDestination
import timeswap.application.ui.screens.core.navigation.EditEducationDestination
import timeswap.application.ui.screens.core.navigation.EditMajorDestination
import timeswap.application.ui.screens.features.profiles.sections.ProfileHeaderSection
import timeswap.application.ui.screens.features.profiles.sections.ProfileSection

import timeswap.application.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController?) {

    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = remember {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    val accessToken = sharedPreferences.getString("accessToken", "") ?: ""

    val userRepository = remember { UserRepository() }
    val profileViewModel = remember { ProfileViewModel(userRepository) }

    val userProfile by profileViewModel.userProfile.collectAsState()

    LaunchedEffect(Unit) {
        if (accessToken.isNotEmpty()) {
            profileViewModel.loadUserProfile(accessToken)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        ProfileHeaderSection(userProfile, navController)

        ProfileSection(
            title = "Thông tin cá nhân",
            iconRes = R.drawable.ic_about_me,
            details = userProfile?.description ?: "Không có thông tin chi tiết.",
            navController = navController,
            destinationRoute = EditAboutMeDestination.route
        )

        ProfileSection(
            title = "Ngành nghề",
            iconRes = R.drawable.ic_major,
            details = userProfile?.majorIndustry?.industryName ?: "Không có ngành nghề chi tiết.",
            navController = navController,
            destinationRoute = EditMajorDestination.route
        )

        ProfileSection(
            title = "Học vấn",
            iconRes = R.drawable.ic_education,
            details = userProfile?.educationHistory ?: emptyList<String>(),
            navController = navController,
            destinationRoute = EditEducationDestination.route
        )

    }
}