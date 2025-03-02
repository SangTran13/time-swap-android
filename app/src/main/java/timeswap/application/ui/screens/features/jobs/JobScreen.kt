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
import timeswap.application.ui.screens.features.jobs.sections.FilterSection
import timeswap.application.ui.screens.features.jobs.sections.HeaderSearchSection
import timeswap.application.ui.screens.features.jobs.sections.JobListSection

@Composable
fun JobScreen(navController: NavController, viewModel: JobListViewModel) {
    val uiState by viewModel.uiState.collectAsState()

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
            HeaderSearchSection()

            // Filter Section
            FilterSection()

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
                    JobListSection(successState, viewModel)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewJobScreen() {
    JobScreen(navController = NavController(LocalContext.current), viewModel = JobListViewModel())
}