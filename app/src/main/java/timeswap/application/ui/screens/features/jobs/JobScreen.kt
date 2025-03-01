package timeswap.application.ui.screens.features.jobs

import timeswap.application.ui.shared.components.BottomNavigationBar

import androidx.navigation.NavController

import timeswap.application.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.AsyncImage

import timeswap.application.data.entity.JobList
import timeswap.application.viewmodel.JobListUiState
import timeswap.application.viewmodel.JobListViewModel
import timeswap.application.shared.utils.CommonFunction

@Composable
fun JobScreen(navController: NavController, viewModel: JobListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var isSwiping by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(
                    bottom = 0.dp,
                    top = 0.dp,
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                )
        ) {
            // Header Search Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background_job),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SearchBar()
                    Spacer(modifier = Modifier.height(15.dp))
                    LocationSearchBar()
                }
            }

            // Filter Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFA726)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
                FilterChip("Tất Cả Danh Mục", true)
                FilterChip("Công Nghệ", false)
                FilterChip("Dịch Vụ", false)
            }

            // Job List Section with Swipe Gesture
            when (uiState) {
                is JobListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Loading...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                is JobListUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            (uiState as JobListUiState.Error).message,
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    }
                }

                is JobListUiState.Success -> {
                    val successState = uiState as JobListUiState.Success
                    val jobList = successState.jobList
                    val currentPage = successState.pageIndex
                    val totalPages = successState.totalPages

                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            contentPadding = PaddingValues(22.dp),
                            modifier = Modifier.pointerInput(Unit) {
                                detectHorizontalDragGestures { _, dragAmount ->
                                    if (dragAmount < -100 && currentPage < totalPages) {
                                        isSwiping = true
                                        viewModel.nextPage()
                                        isSwiping = false
                                    } else if (dragAmount > 100 && currentPage > 1) {
                                        isSwiping = true
                                        viewModel.previousPage()
                                        isSwiping = false
                                    }
                                }
                            }
                        ) {
                            items(jobList) { job ->
                                JobCard(job)
                                Spacer(modifier = Modifier.height(25.dp))
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Page $currentPage / $totalPages",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Search",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = textState,
            onValueChange = { textState = it },
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LocationSearchBar() {
    var locationState by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = "Location",
            modifier = Modifier.size(20.dp),
            tint = Color(0xFFFFA726)
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = locationState,
            onValueChange = { locationState = it },
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean, color: Color = Color.LightGray) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Color(0xFFFFA726) else color)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun JobCard(job: JobList) {
    Card(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = job.ownerAvatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = job.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(job.category.categoryName, false, color = Color(0xFF2196F3))
                FilterChip(job.ward.name, false, color = Color(0xFF4CAF50))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(CommonFunction.formatDateFromUTC(job.createdAt), fontSize = 12.sp, color = Color.Gray)
                Text("${job.fee}VNĐ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}


@Preview
@Composable
fun PreviewJobScreen() {
    JobScreen(navController = NavController(LocalContext.current), viewModel = JobListViewModel())
}