package timeswap.application.ui.screens.features.jobdetail

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import timeswap.application.network.services.JobPostService
import timeswap.application.viewmodel.JobDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    jobId: String?
) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = remember {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    val accessToken = sharedPreferences.getString("accessToken", "") ?: ""


    val jobPostService = remember { JobPostService() }

    val jobDetailViewModel = remember { JobDetailViewModel(jobPostService) }

    val jobDetail by jobDetailViewModel.jobDetail.collectAsState()

    LaunchedEffect(jobId) {
        if (accessToken.isNotEmpty()) {
            jobId?.let { id ->
                jobDetailViewModel.fetchJobDetail(id, accessToken)
            }
        }
    }

    if (jobDetail != null) {

        val userCity = jobDetail!!.ownerLocation.split(",").last().trim()

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = { },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        AsyncImage(
                            model = jobDetail!!.ownerAvatarUrl,
                            contentDescription = "User Logo",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = jobDetail!!.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "${jobDetail!!.ownerName} • $userCity", fontSize = 14.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /* TODO: Apply action */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB2B2))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Apply",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFFFC4646),
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "Apply", color = Color(0xFFFC4646), fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { /* TODO: View Profile */ },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color(0xFFFFA6A6))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowOutward,
                            contentDescription = "Apply",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFFFC4646)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "View Profile", color = Color(0xFFFC4646), fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                var selectedTabIndex by remember { mutableIntStateOf(0) }
                val tabs = listOf("Job Detail", "Applicants")

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    contentColor = Color.Black
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                when (selectedTabIndex) {
                    0 -> JobDetailContent(jobDetail!!)
                    1 -> ApplicantsContent()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJobDetailScreen() {
    JobDetailScreen(navController = rememberNavController(), jobId = "1")
}