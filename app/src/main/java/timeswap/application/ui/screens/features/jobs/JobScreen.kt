package timeswap.application.ui.screens.features.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import timeswap.application.ui.screens.features.jobs.sections.FilterSection
import timeswap.application.ui.screens.features.jobs.sections.HeaderSearchSection
import timeswap.application.ui.screens.features.jobs.sections.JobListSection
import timeswap.application.ui.shared.components.BottomNavigationBar
import timeswap.application.viewmodel.IndustryCategoryViewModel
import timeswap.application.viewmodel.JobListUiState
import timeswap.application.viewmodel.JobListViewModel

@Composable
fun JobScreen(navController: NavController, viewModel: JobListViewModel, industryCategoryViewModel: IndustryCategoryViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(
                    bottom = 0.dp,
                    top = 0.dp,
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                )
        ) {
            HeaderSearchSection(viewModel)

            FilterSection(viewModel = industryCategoryViewModel, jobListViewModel = viewModel)

            when (uiState) {
                is JobListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Đang tải...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                    JobListSection(successState, navController, viewModel)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewJobScreen() {
    JobScreen(navController = NavController(LocalContext.current), viewModel = JobListViewModel(), industryCategoryViewModel = IndustryCategoryViewModel())
}