package timeswap.application.ui.screens.features.payment.sections

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch
import timeswap.application.R
import timeswap.application.network.services.PaymentRepository

private suspend fun onPayClick(
    amount: String,
    note: String,
    paymentMethod: Int,
    accessToken: String,
    paymentRepository: PaymentRepository,
    onBackToHome: () -> Unit
) {
    if (amount.isNotEmpty()) {
        paymentRepository.handlePayment(
            token = accessToken,
            amount = amount.toInt(),
            note = note,
            paymentMethod = paymentMethod,
            onSuccess = onBackToHome,
            onError = { error -> Log.e("PaymentError", "Thanh toán thất bại: $error") }
        )
    }
}

@Composable
fun PaymentFormSection(
    fullNameState: String,
    phoneNumberState: String,
    emailState: String,
    accessToken: String,
    paymentRepository: PaymentRepository,
    onBackToHome: () -> Unit

) {
    var fullName = fullNameState
    var phoneNumber = phoneNumberState
    var email = emailState
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableIntStateOf(1) }

    val fullNameFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val amountFocus = remember { FocusRequester() }
    val noteFocus = remember { FocusRequester() }
    val payButtonFocus = remember { FocusRequester() }
    val backButtonFocus = remember { FocusRequester() }

    val coroutineScope = rememberCoroutineScope()

    PaymentTextField("Họ tên", fullName, { fullName = it }, fullNameFocus, phoneFocus)
    PaymentTextField("Số điện thoại", phoneNumber, { phoneNumber = it }, phoneFocus, emailFocus)
    PaymentTextField("Email", email, { email = it }, emailFocus, amountFocus)
    PaymentTextField(
        "Số tiền nạp",
        amount,
        { if (it.all { char -> char.isDigit() }) amount = it },
        amountFocus,
        noteFocus
    )

    Spacer(modifier = Modifier.height(12.dp))
    TransactionNoteField(note, { note = it }, noteFocus, payButtonFocus)

    Spacer(modifier = Modifier.height(12.dp))
    PaymentMethodSelection(paymentMethod) { paymentMethod = it }

    Spacer(modifier = Modifier.height(20.dp))
    Button(
        onClick = {
            coroutineScope.launch {
                onPayClick(
                    amount,
                    note,
                    paymentMethod,
                    accessToken,
                    paymentRepository,
                    onBackToHome
                )
            }
        },
        modifier = Modifier
            .width(300.dp)
            .height(55.dp)
            .focusRequester(payButtonFocus),
        shape = RoundedCornerShape(10.dp),
        enabled = amount.isNotEmpty() && note.isNotEmpty(),
        colors = ButtonDefaults.buttonColors(Color.Red)
    ) {
        Text(text = "Nạp tiền", fontSize = 16.sp, color = Color.White)
    }
    Spacer(modifier = Modifier.height(10.dp))

    Button(
        onClick = onBackToHome,
        modifier = Modifier
            .width(300.dp)
            .height(55.dp)
            .focusRequester(backButtonFocus),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(Color.Gray)
    ) {
        Text(text = "Trở về", fontSize = 16.sp, color = Color.White)
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
        label = { Text("Nội dung chuyển khoản") },
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
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {
        PaymentMethodItem(R.drawable.payos_logo, "PayOS", selectedMethod == 1) { onSelect(1) }
        PaymentMethodItem(R.drawable.vnpay_logo, "VNPay", selectedMethod == 2) { onSelect(2) }
    }
}

@Composable
fun PaymentMethodItem(
    imageResId: Int,
    contentDescription: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) Color(0xFFD6CDFE) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        IconButton(
            onClick = onSelect,
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                painter = painterResource(id = imageResId),
                contentDescription = contentDescription,
                modifier = Modifier.size(50.dp),
                tint = Color.Unspecified
            )
        }
    }
}