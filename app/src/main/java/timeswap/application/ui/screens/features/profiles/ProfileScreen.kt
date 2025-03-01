package timeswap.application.ui.screens.features.profiles

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import timeswap.application.R
import timeswap.application.data.entity.User
import timeswap.application.network.services.UserRepository
import timeswap.application.ui.screens.core.navigation.EditAboutMeDestination
import timeswap.application.ui.screens.core.navigation.EditEducationDestination
import timeswap.application.ui.screens.core.navigation.EditMajorDestination
import timeswap.application.ui.screens.core.navigation.SettingsDestination
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
        ProfileHeader(userProfile, navController)
        ProfileSection(
            title = "About me",
            iconRes = R.drawable.ic_about_me,
            details = userProfile?.description ?: "No information available",
            navController = navController,
            destinationRoute = EditAboutMeDestination.route
        )

        ProfileSection(
            title = "Major",
            iconRes = R.drawable.ic_major,
            details = userProfile?.majorIndustry ?: "No major specified",
            navController = navController,
            destinationRoute = EditMajorDestination.route
        )

        ProfileSection(
            title = "Education",
            iconRes = R.drawable.ic_education,
            details = userProfile?.educationHistory ?: emptyList<String>(),
            navController = navController,
            destinationRoute = EditEducationDestination.route
        )

    }
}

@Composable
fun ProfileHeader(userProfile : User?, navController: NavController? = null) {
    val subscriptionText = when (userProfile?.subscriptionPlan) {
        1 -> "Standard"
        2 -> "Premium"
        else -> "Basic"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(295.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_job),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                AsyncImage(
                    model = userProfile?.avatarUrl ?: "drawable/default_avatar.png",
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(90.dp)
                        .background(Color.White, shape = CircleShape)
                )

                IconButton(
                    onClick = {  navController?.navigate(SettingsDestination.route) },
                    modifier = Modifier.size(40.dp).padding(top = 0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_setting),
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = userProfile?.fullName ?: "Unknown",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = userProfile?.fullLocation ?: "Unknown",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 99.dp)
                .background(Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(subscriptionText, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Edit",
                tint = Color.White
            )
        }
    }
}




@Composable
fun ProfileSection(title: String, iconRes: Int, details: Any, navController: NavController?,  destinationRoute: String? = null) {
    var expanded by remember { mutableStateOf(false) }

    val detailsList: List<String> = when (details) {
        is String -> listOf(details)
        is List<*> -> details.filterIsInstance<String>()
        else -> emptyList()
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(25.dp),
                    tint = Color(0xFFFFA726)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E1E1E)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        painter = painterResource(id = if (expanded) R.drawable.ic_eye_off else R.drawable.ic_eye),
                        contentDescription = "Toggle Details",
                        modifier = Modifier.size(25.dp),
                        tint = Color(0xFFFFA726)
                    )
                }

                if (destinationRoute != null) {
                    IconButton(onClick = { navController?.navigate(destinationRoute) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_next_page),
                            contentDescription = "Edit",
                            modifier = Modifier.size(25.dp),
                            tint = Color(0xFFFFA726)
                        )
                    }
                }
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                        .fillMaxWidth()
                ) {
                    if (detailsList.isNotEmpty()) {
                        detailsList.forEach { item ->
                            Text(
                                text = item,
                                fontSize = 16.sp,
                                color = Color(0xFF424242),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "No details available",
                            fontSize = 16.sp,
                            color = Color(0xFF424242)
                        )
                    }
                }
            }
        }
    }
}
