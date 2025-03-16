package timeswap.application.ui.screens.features.payment

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
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
                errorMessage = "Xác thực không thành công. Vui lòng đăng nhập lại."
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
                text = "Nạp Tiền",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (accessToken != null) {
                PaymentFormSection(fullName, phoneNumber, email, accessToken, paymentRepository, onBackToHome)
            }
        }
    }
}