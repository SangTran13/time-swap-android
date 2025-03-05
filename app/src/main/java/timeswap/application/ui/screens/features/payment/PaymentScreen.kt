package timeswap.application.ui.screens.features.payment

import android.content.SharedPreferences

import kotlinx.coroutines.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import timeswap.application.R
import timeswap.application.network.services.PaymentRepository
import timeswap.application.network.services.UserRepository
import timeswap.application.ui.screens.features.payment.sections.PaymentFormSection

@Composable
fun PaymentScreen(
    sharedPreferences: SharedPreferences,
    userRepository: UserRepository,
    paymentRepository: PaymentRepository,
    onBackToHome: () -> Unit
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val accessToken = sharedPreferences.getString("accessToken", null)

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            if (!accessToken.isNullOrEmpty()) {
                val user = userRepository.getUserProfile(accessToken)
                user?.let {
                    fullName = it.fullName
                    phoneNumber = it.phoneNumber
                    email = it.email
                }
            } else {
                errorMessage = "Authentication token is missing"
            }
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
                text = "Deposit Money",
                fontSize = 26.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (accessToken != null) {
                PaymentFormSection(fullName, phoneNumber, email, accessToken, paymentRepository, onBackToHome)
            }
        }
    }
}


