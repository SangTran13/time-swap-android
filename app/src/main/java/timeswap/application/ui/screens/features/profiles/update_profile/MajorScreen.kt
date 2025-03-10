package timeswap.application.ui.screens.features.profiles.update_profile

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import timeswap.application.data.entity.Category
import timeswap.application.data.entity.Industry
import timeswap.application.data.request.UpdateProfileRequest
import timeswap.application.network.services.UserRepository
import timeswap.application.viewmodel.IndustryCategoryViewModel
import timeswap.application.viewmodel.ProfileViewModel

@Composable
fun MajorScreen(navController: NavController, industryCategoryViewModel: IndustryCategoryViewModel) {
    val userRepository = remember { UserRepository() }
    val profileViewModel = remember { ProfileViewModel(userRepository) }
    val userProfile by profileViewModel.userProfile.collectAsState()

    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = remember {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    val accessToken = sharedPreferences.getString("accessToken", "") ?: ""
    val coroutineScope = rememberCoroutineScope()

    val industries by industryCategoryViewModel.industries.collectAsState()
    val categories by industryCategoryViewModel.categories.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var selectedIndustry by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    LaunchedEffect(accessToken) {
        if (accessToken.isNotEmpty()) {
            profileViewModel.loadUserProfile(accessToken)
        }
        industryCategoryViewModel.loadIndustries()
    }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            selectedIndustry = it.majorIndustry
            selectedCategory = it.majorCategory
        }
    }

    LaunchedEffect(selectedIndustry) {
        if (selectedIndustry.isNotEmpty()) {
            industryCategoryViewModel.loadCategories(getIndustryId(selectedIndustry, industries) ?: 0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Major",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (isEditing) {
                DropdownField(
                    label = "Industry",
                    options = industries.map { it.industryName },
                    selectedOption = selectedIndustry
                ) { industry ->
                    selectedIndustry = industry
                    selectedCategory = ""
                    industryCategoryViewModel.loadCategories(getIndustryId(industry, industries) ?: 0)
                }

                DropdownField(
                    label = "Category",
                    options = if (categories.isNotEmpty()) categories.map { it.categoryName } else emptyList(),
                    selectedOption = selectedCategory
                ) { selectedCategory = it }
            } else {
                ProfileTextField("Industry", selectedIndustry, false)
                ProfileTextField("Category", selectedCategory, false)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (isEditing) {
                        val industryId = getIndustryId(selectedIndustry, industries) ?: 0
                        val categoryId = getCategoryId(selectedCategory, categories) ?: 0

                        val updateRequest = UpdateProfileRequest(
                            majorCategoryId = categoryId,
                            majorIndustryId = industryId
                        )

                        coroutineScope.launch {
                            val success = userRepository.updateUserProfile(accessToken, updateRequest)
                            if (success) {
                                profileViewModel.loadUserProfile(accessToken)
                                isEditing = false
                            }
                        }
                    } else {
                        isEditing = true
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Red),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(300.dp)
                    .height(55.dp)
            ) {
                Text(if (isEditing) "Save" else "Edit", color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(Color.Gray),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(300.dp)
                    .height(55.dp)
            ) {
                Text("Back", color = Color.White)
            }
        }
    }
}

fun getIndustryId(industryName: String, industries: List<Industry>): Int? {
    return industries.find { it.industryName == industryName }?.id
}

fun getCategoryId(categoryName: String, categories: List<Category>): Int? {
    return categories.find { it.categoryName == categoryName }?.id
}

