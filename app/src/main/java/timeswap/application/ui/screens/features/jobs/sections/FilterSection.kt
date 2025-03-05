package timeswap.application.ui.screens.features.jobs.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import timeswap.application.R
import timeswap.application.viewmodel.IndustryCategoryViewModel
import timeswap.application.viewmodel.JobListViewModel

@Composable
fun FilterSection(viewModel: IndustryCategoryViewModel, jobListViewModel: JobListViewModel) {
    val industries by viewModel.industries.collectAsState()
    var selectedIndustryId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadIndustries()
    }

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

        FilterChip(
            text = "Tất Cả",
            selected = selectedIndustryId == null,
            onClick = {
                selectedIndustryId = null
                jobListViewModel.filterByIndustry(null)
            }
        )

        industries.forEach { industry ->
            FilterChip(
                text = industry.industryName,
                selected = selectedIndustryId == industry.id,
                onClick = {
                    selectedIndustryId = industry.id
                    jobListViewModel.filterByIndustry(industry.id)
                }
            )
        }
    }
}



@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit, color: Color = Color.LightGray) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Color(0xFFFFA726) else color)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}


