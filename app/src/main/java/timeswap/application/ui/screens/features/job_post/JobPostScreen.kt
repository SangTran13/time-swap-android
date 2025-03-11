package timeswap.application.ui.screens.features.job_post

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import timeswap.application.data.entity.Category
import timeswap.application.data.entity.City
import timeswap.application.data.entity.Industry
import timeswap.application.data.entity.Ward
import timeswap.application.data.request.JobPostRequest
import timeswap.application.network.services.JobPostService
import timeswap.application.ui.screens.core.navigation.HomeDestination
import timeswap.application.ui.screens.features.profiles.update_profile.ProfileTextArea
import timeswap.application.ui.screens.features.profiles.update_profile.ProfileTextField
import timeswap.application.viewmodel.IndustryCategoryViewModel
import timeswap.application.viewmodel.LocationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun JobPostScreen(
    navController: NavController,
    industryCategoryViewModel: IndustryCategoryViewModel,
    locationViewModel: LocationViewModel
) {
    val jobService = remember { JobPostService() }
    val context = LocalContext.current
    val sharedPreferences =
        remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
    val accessToken = sharedPreferences.getString("accessToken", "") ?: ""
    val coroutineScope = rememberCoroutineScope()

    val industries by industryCategoryViewModel.industries.collectAsState()
    val categories by industryCategoryViewModel.categories.collectAsState()
    val cities by locationViewModel.cities.collectAsState()
    val wards by locationViewModel.wards.collectAsState()

    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var responsibilities by remember { mutableStateOf("") }
    var fee by remember { mutableStateOf("") }
    var selectedIndustry by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var selectedWard by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }

    LaunchedEffect(accessToken) {
        if (accessToken.isNotEmpty()) {
            locationViewModel.loadCities()
            industryCategoryViewModel.loadIndustries()
        }
    }

    LaunchedEffect(selectedCity) {
        locationViewModel.loadWards(getCityId(selectedCity, cities))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 50.dp, start = 20.dp, end = 20.dp, bottom = 80.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Post a Job", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        ProfileTextField("Job Title", jobTitle, true) { jobTitle = it }
        ProfileTextField("Fee", fee, true) { fee = it }
        ProfileTextField("Responsibilities", responsibilities, true) { responsibilities = it }

        DatePickerField("Start Date", startDate) { startDate = it }
        DatePickerField("Due Date", dueDate) { dueDate = it }

        ProfileTextArea("Job Description", jobDescription, true) { jobDescription = it }

        DropdownField("Industry", industries.map { it.industryName }, selectedIndustry) {
            selectedIndustry = it
            industryCategoryViewModel.loadCategories(getIndustryId(it, industries))
        }
        DropdownField(
            "Category",
            categories.map { it.categoryName },
            selectedCategory
        ) { selectedCategory = it }

        DropdownField("City", cities.map { it.name }, selectedCity) { selectedCity = it }
        DropdownField("Ward", wards.map { it.name }, selectedWard) { selectedWard = it }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    isPosting = true
                    val request = JobPostRequest(
                        title = jobTitle,
                        description = jobDescription,
                        responsibilities = responsibilities,
                        fee = fee.toDoubleOrNull() ?: 0.0,
                        categoryId = getCategoryId(selectedCategory, categories),
                        industryId = getIndustryId(selectedIndustry, industries),
                        cityId = getCityId(selectedCity, cities),
                        wardId = getWardId(selectedWard, wards),
                        startDate = formatDateForRequest(startDate),
                        dueDate = formatDateForRequest(dueDate)
                    )
                    val response = jobService.postJob(accessToken, request)
                    isPosting = false
                    if (response != null) {
                        Toast.makeText(context, "Job posted successfully", Toast.LENGTH_SHORT)
                            .show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Failed to post job", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(Color.Red),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(300.dp)
                .height(55.dp),
            enabled = !isPosting
        ) {
            Text(if (isPosting) "Posting..." else "Post Job", color = Color.White)
        }
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = { navController.navigate(HomeDestination.route) },
            modifier = Modifier
                .width(300.dp)
                .height(55.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(Color.Gray)
        ) {
            Text(text = "Back", color = Color.White)
        }
    }
}

fun formatDateForRequest(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        parsedDate?.let { outputFormat.format(it) } ?: ""
    } catch (e: Exception) {
        ""
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Default.DateRange, contentDescription = "Select Date")
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
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

fun getIndustryId(name: String, industries: List<Industry>): Int {
    return industries.find { it.industryName == name }?.id ?: 0
}

fun getCategoryId(name: String, categories: List<Category>): Int {
    return categories.find { it.categoryName == name }?.id ?: 0
}

fun getCityId(name: String, cities: List<City>): String {
    return cities.find { it.name == name }?.id ?: ""
}

fun getWardId(name: String, wards: List<Ward>): String {
    return wards.find { it.name == name }?.id ?: ""
}

