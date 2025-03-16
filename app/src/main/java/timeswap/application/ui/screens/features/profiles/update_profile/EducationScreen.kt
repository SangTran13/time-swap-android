package timeswap.application.ui.screens.features.profiles.update_profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import timeswap.application.R
import timeswap.application.data.request.UpdateProfileRequest
import timeswap.application.network.services.UserRepository
import timeswap.application.viewmodel.ProfileViewModel

@Composable
fun EducationScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    val accessToken = sharedPreferences.getString("accessToken", "") ?: ""

    val userRepository = remember { UserRepository() }
    val profileViewModel = remember { ProfileViewModel(userRepository) }
    val userProfile by profileViewModel.userProfile.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }
    var schoolName by remember { mutableStateOf("") }
    var educationHistory by remember { mutableStateOf(emptyList<String>()) }

    // Load profile data on accessToken change
    LaunchedEffect(accessToken) {
        if (accessToken.isNotEmpty()) {
            profileViewModel.loadUserProfile(accessToken)
        }
    }

    LaunchedEffect(userProfile) {
        educationHistory = userProfile?.educationHistory ?: emptyList()
    }

    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            educationHistory = educationHistory.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Học vấn",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 50.dp, bottom = 16.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            EducationList(educationHistory, reorderState, isEditing) { school ->
                educationHistory = educationHistory - school
            }
        }

        if (isEditing) {
            AddSchoolSection(
                schoolName = schoolName,
                onSchoolNameChange = { schoolName = it },
                onAddSchool = {
                    if (schoolName.isNotBlank()) {
                        educationHistory = educationHistory + schoolName
                        schoolName = ""
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        ButtonSection(
            isEditing = isEditing,
            onEditToggle = {
                if (isEditing) {
                    coroutineScope.launch {
                        val updateRequest =
                            UpdateProfileRequest(educationHistory = educationHistory)
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
            onBack = { navController.popBackStack() }
        )
    }
}

@Composable
fun EducationList(
    educationHistory: List<String>,
    reorderState: ReorderableLazyListState,
    isEditing: Boolean,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        state = reorderState.listState,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .reorderable(reorderState)
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(educationHistory.withIndex().toList(), { it.index }) { (index, school) ->
            ReorderableItem(reorderState, key = index) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .detectReorderAfterLongPress(reorderState)
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_education),
                            contentDescription = "Education Icon",
                            tint = Color(0xFFFFA726),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = school,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        if (isEditing) {
                            IconButton(onClick = { onDelete(school) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    contentDescription = "Delete",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color(0xFFFFA726),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddSchoolSection(
    schoolName: String,
    onSchoolNameChange: (String) -> Unit,
    onAddSchool: () -> Unit
) {
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = schoolName,
            onValueChange = onSchoolNameChange,
            label = { Text("Thêm trường học") },
            modifier = Modifier.weight(1f).height(56.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onAddSchool,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color.Red),
            modifier = Modifier
                .size(56.dp)
                .offset(y = 4.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Composable
fun ButtonSection(
    isEditing: Boolean,
    onEditToggle: () -> Unit,
    onBack: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onEditToggle,
            colors = ButtonDefaults.buttonColors(Color.Red),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(300.dp)
                .height(55.dp)
        ) {
            Text(if (isEditing) "Lưu" else "Chỉnh sửa", fontSize = 16.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(Color.Gray),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(300.dp)
                .height(55.dp)
        ) {
            Text("Trở về", fontSize = 16.sp, color = Color.White)
        }
    }
}