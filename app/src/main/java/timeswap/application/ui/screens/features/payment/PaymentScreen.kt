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

@Composable
fun PaymentScreen(
    sharedPreferences: SharedPreferences,
    userRepository: UserRepository,
    paymentRepository: PaymentRepository,
    onBackToHome: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableIntStateOf(1) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val fullNameFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val amountFocus = remember { FocusRequester() }
    val noteFocus = remember { FocusRequester() }
    val payButtonFocus = remember { FocusRequester() }
    val backButtonFocus = remember { FocusRequester() }

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

    fun onPayClick() {
        if (amount.isNotEmpty()) {
            coroutineScope.launch {
                paymentRepository.handlePayment(
                    token = accessToken,
                    amount = amount.toInt(),
                    note = note,
                    paymentMethod = paymentMethod,
                    onSuccess = onBackToHome,
                    onError = { errorMsg -> errorMessage = errorMsg }
                )
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

            PaymentTextField("Full Name", fullName, { fullName = it }, fullNameFocus, phoneFocus)
            PaymentTextField("Phone Number", phoneNumber, { phoneNumber = it }, phoneFocus, emailFocus)
            PaymentTextField("Email", email, { email = it }, emailFocus, amountFocus)
            PaymentTextField("Deposit Amount", amount, { if (it.all { char -> char.isDigit() }) amount = it }, amountFocus, noteFocus)

            Spacer(modifier = Modifier.height(12.dp))
            TransactionNoteField(note, { note = it }, noteFocus, payButtonFocus)

            Spacer(modifier = Modifier.height(12.dp))
            PaymentMethodSelection(paymentMethod) { paymentMethod = it }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onPayClick() },
                modifier = Modifier
                    .width(280.dp)
                    .height(50.dp)
                    .focusRequester(payButtonFocus),
                shape = RoundedCornerShape(10.dp),
                enabled = amount.isNotEmpty() && note.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(text = "DEPOSIT MONEY", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBackToHome,
                modifier = Modifier
                    .width(280.dp)
                    .height(50.dp)
                    .focusRequester(backButtonFocus),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text(text = "BACK TO HOME", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun PaymentTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    nextFocusRequester: FocusRequester
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Tab) {
                    nextFocusRequester.requestFocus()
                    true
                } else false
            }
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun TransactionNoteField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    nextFocusRequester: FocusRequester
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Transaction Note") },
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .focusRequester(focusRequester)
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Tab) {
                    nextFocusRequester.requestFocus()
                    true
                } else false
            }
    )
}

@Composable
fun PaymentMethodSelection(selectedMethod: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PaymentMethodItem(R.drawable.payos_logo, "PayOS", selectedMethod == 1) { onSelect(1) }
        PaymentMethodItem(R.drawable.vnpay_logo, "VNPay", selectedMethod == 2) { onSelect(2) }
    }
}

@Composable
fun PaymentMethodItem(imageResId: Int, contentDescription: String, isSelected: Boolean, onSelect: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(width = if (isSelected) 0.5.dp else 0.dp, color = if (isSelected) Color.Gray else Color.Transparent, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        IconButton(
            onClick = onSelect,
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                painter = painterResource(id = imageResId),
                contentDescription = contentDescription,
                modifier = Modifier.size(60.dp),
                tint = Color.Unspecified
            )
        }
    }
}

