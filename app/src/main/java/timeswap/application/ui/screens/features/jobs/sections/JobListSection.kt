package timeswap.application.ui.screens.features.jobs.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

import timeswap.application.shared.utils.CommonFunction

import timeswap.application.data.entity.JobPost
import timeswap.application.viewmodel.JobListUiState
import timeswap.application.viewmodel.JobListViewModel

@Composable
fun JobListSection(
    requiredData: JobListUiState.Success,
    navController: NavController,
    viewModel: JobListViewModel
) {
    var isSwiping by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (requiredData.jobList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không có công việc nào!!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(22.dp, 22.dp, 22.dp, 120.dp),
                modifier = Modifier.pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount < -100 && requiredData.pageIndex < requiredData.totalPages) {
                            isSwiping = true
                            viewModel.nextPage()
                            isSwiping = false
                        } else if (dragAmount > 100 && requiredData.pageIndex > 1) {
                            isSwiping = true
                            viewModel.previousPage()
                            isSwiping = false
                        }
                    }
                }
            ) {
                items(requiredData.jobList) { job ->
                    JobCard(
                        job,
                        onClick = {
                            navController.navigate("jobDetail/${job.id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (requiredData.jobList.isEmpty()) "Trang 0 / 0"
                    else "Trang ${requiredData.pageIndex} / ${requiredData.totalPages}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 200.dp)
                )
            }
        }
    }
}


@Composable
fun JobCard(job: JobPost, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = job.ownerAvatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                FilterChip(
                    text = job.category.categoryName,
                    selected = false,
                    onClick = { /* TODO: Thêm logic nếu cần */ },
                    color = Color(0xFFCBC9D4)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = job.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = job.ward.fullLocation,
                fontSize = 12.sp,
                color = Color(0xFF524B6B)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    CommonFunction.formatDateFromUTC(job.createdAt, 2),
                    fontSize = 12.sp,
                    color = Color(0xFFAAA6B9)
                )
                Text(
                    text = CommonFunction.formatCurrency(job.fee),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}