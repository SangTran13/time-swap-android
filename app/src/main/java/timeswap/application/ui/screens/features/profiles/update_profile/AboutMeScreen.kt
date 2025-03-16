package timeswap.application.ui.screens.features.profiles.update_profile

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import timeswap.application.data.entity.City
import timeswap.application.data.entity.Ward
import timeswap.application.data.request.UpdateProfileRequest
import timeswap.application.network.services.UserRepository
import timeswap.application.shared.utils.CommonFunction
import timeswap.application.ui.screens.core.navigation.ProfileDestination
import timeswap.application.viewmodel.LocationViewModel
import timeswap.application.viewmodel.ProfileViewModel

@Suppress("USELESS_ELVIS")
@Composable
fun AboutMeScreen(navController: NavController, locationViewModel: LocationViewModel) {
    val userRepository = remember { UserRepository() }
    val profileViewModel = remember { ProfileViewModel(userRepository) }
    val userProfile by profileViewModel.userProfile.collectAsState()

    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = remember {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    val accessToken = sharedPreferences.getString("accessToken", "") ?: ""
    val coroutineScope = rememberCoroutineScope()


    val cities by locationViewModel.cities.collectAsState()
    val wards by locationViewModel.wards.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var selectedWard by remember { mutableStateOf("") }

    LaunchedEffect(accessToken) {
        if (accessToken.isNotEmpty()) {
            profileViewModel.loadUserProfile(accessToken)
        }
        if (selectedCity.isEmpty()) {
            locationViewModel.loadCities()
        }
    }

    LaunchedEffect(selectedWard) {
        if (selectedWard.isEmpty() && selectedCity.isNotEmpty()) {
            locationViewModel.loadWards(getCityId(selectedCity, cities))
        }
    }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            firstName = it.firstName ?: ""
            lastName = it.lastName ?: ""
            phone = it.phoneNumber ?: ""
            description = it.description ?: ""
            selectedWard = it.fullLocation ?: ""
            selectedCity = extractCity(it.fullLocation)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Thông tin cá nhân",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (isEditing) {
                ProfileTextField("Họ", lastName, isEditing) { lastName = it }
                ProfileTextField("Tên", firstName, isEditing) { firstName = it }
            } else {
                ProfileTextField("Họ và tên", "$lastName $firstName", false)
            }
            ProfileTextField("Email", userProfile?.email ?: "No Email Address", false)
            ProfileTextField("Số điện thoại", phone, isEditing) { phone = it }
            ProfileTextArea("Mô tả", description, isEditing) { description = it }

            if (isEditing) {
                DropdownField(
                    label = "Tỉnh/Thành phố",
                    options = cities.map { it.name }.ifEmpty { listOf("Loading...") },
                    selectedOption = selectedCity
                ) { city ->
                    selectedCity = city
                    selectedWard = ""
                    locationViewModel.loadWards(getCityId(city, cities))
                }

                DropdownField(
                    label = "Địa chỉ",
                    options = if (wards.isNotEmpty()) wards.map { it.name } else listOf("Loading..."),
                    selectedOption = selectedWard
                ) { selectedWard = it }
            } else {
                ProfileTextField("Location", selectedWard, false)
            }

            ProfileTextField("Ngày hết hạn đăng ký gói", userProfile?.subscriptionExpiryDate?.let {
                CommonFunction.formatDateFromUTC(
                    it, 2
                )
            }
                ?: "Không giới hạn", false)

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (isEditing) {
                        val cityId = getCityId(selectedCity, cities)
                        val wardId = getWardId(selectedWard, wards)

                        val updateRequest = UpdateProfileRequest(
                            firstName = firstName,
                            lastName = lastName,
                            phoneNumber = phone,
                            description = description,
                            cityId = cityId,
                            wardId = wardId
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
                Text(if (isEditing) "Lưu" else "Chỉnh sửa", fontSize = 16.sp, color = Color.White)
            }


            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { navController.navigate(ProfileDestination.route) },
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
}

@Composable
fun ProfileTextField(label: String, value: String, isEditable: Boolean, onValueChange: (String) -> Unit = {}) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = isEditable,
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledBorderColor = Color.Gray,
            disabledLabelColor = Color.DarkGray
        ),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun DropdownField(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            Column(modifier = Modifier
                .heightIn(max = 200.dp)
                .verticalScroll(rememberScrollState())
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun ProfileTextArea(label: String, value: String, isEditable: Boolean, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = isEditable,
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledBorderColor = Color.Gray,
            disabledLabelColor = Color.DarkGray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        maxLines = 5,
        singleLine = false
    )
    Spacer(modifier = Modifier.height(12.dp))
}


fun extractCity(fullLocation: String?): String {
    return fullLocation?.split(",")?.last()?.trim() ?: ""
}

fun getCityId(cityName: String, cities: List<City>): String {
    return cities.find { it.name == cityName }?.id ?: ""
}

fun getWardId(wardName: String, wards: List<Ward>): String {
    return wards.firstOrNull { it.name.equals(wardName, ignoreCase = true) }?.id ?: ""
}