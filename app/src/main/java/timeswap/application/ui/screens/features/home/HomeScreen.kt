package timeswap.application.ui.screens.features.home

import timeswap.application.ui.shared.components.BottomNavigationBar

import android.content.Context

import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter

import kotlinx.coroutines.launch

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import timeswap.application.R
import timeswap.application.network.services.UserRepository

@Composable
fun HomeScreen(context: Context, navController: NavController) {
    val sharedPreferences =
        remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
    var fullName by remember { mutableStateOf("Unknown User") }
    var avatarUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val accessToken = sharedPreferences.getString("accessToken", null)
    val userRepository = remember { UserRepository() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(accessToken) {
        accessToken?.let { token ->
            coroutineScope.launch {
                val user = userRepository.getUserProfile(token)
                fullName = user?.fullName ?: "Unknown User"
                avatarUrl = user?.avatarUrl ?: ""
                isLoading = false
            }
        } ?: run { isLoading = false }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Hello", fontSize = 20.sp, fontWeight = FontWeight.Light)
                    Text(text = "$fullName.", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
                Image(
                    painter = if (avatarUrl.isNotEmpty()) rememberAsyncImagePainter(avatarUrl) else painterResource(
                        R.drawable.default_avatar
                    ),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            BannerSection()
            Spacer(modifier = Modifier.height(16.dp))
            JobCategorySection()
            Spacer(modifier = Modifier.height(16.dp))
            RecentJobList()
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@Composable
fun BannerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(Color(0xFFFF6F61), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "50% off\nTake any courses",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Join Now", color = Color(0xFFFF6F61))
                }
            }

            Image(
                painter = painterResource(R.drawable.ic_girl_study),
                contentDescription = "Study Girl",
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun JobCategorySection() {
    Text(
        text = "Find Your Job",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        JobCategoryBox("44.5k", "Remote Job", Color(0xFF89CFF0), Modifier.weight(1f).height(172.dp), R.drawable.ic_remote_work)
        Column(modifier = Modifier.weight(1f)) {
            JobCategoryBox("66.8k", "Full Time", Color(0xFFB39DDB))
            Spacer(modifier = Modifier.height(12.dp))
            JobCategoryBox("38.9k", "Part Time", Color(0xFFFFAB91))
        }
    }
}

@Composable
fun RecentJobList() {
    Text(
        text = "Recent Job List",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(16.dp))
    JobListItem()
    Spacer(modifier = Modifier.height(16.dp))
    JobListItem()

}

@Composable
fun JobCategoryBox(value: String, label: String, color: Color, modifier: Modifier = Modifier, iconResId: Int? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            if (iconResId != null) {
                Image(
                    painter = painterResource(iconResId),
                    contentDescription = label,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = label, fontSize = 14.sp, color = Color.Black)
        }
    }
}

@Composable
fun JobListItem() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_job_icon),
                contentDescription = "Job Icon",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Apple",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Google Inc · USA",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$15K/Mo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1
                    )
                    Text(
                        text = "Full Time",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F61)),
                modifier = Modifier
                    .width(90.dp)
                    .height(40.dp)
            ) {
                Text(text = "Apply", fontSize = 14.sp, color = Color.White)
            }
        }
    }
}
