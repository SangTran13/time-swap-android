package timeswap.application.ui.screens.core.authentication.forgot_password

import android.widget.Toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import timeswap.application.R.drawable
import timeswap.application.network.services.ForgotPasswordService

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit, onResetSent: () -> Unit
) {

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val emailFocusRequester = remember { FocusRequester() }
    val resetButtonFocusRequester = remember { FocusRequester() }
    val forgotPasswordService = remember { ForgotPasswordService() }

    fun handleForgotPassword() {
        if (email.isBlank()) {
            Toast.makeText(context, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true
        forgotPasswordService.forgotPassword(
            email = email,
            onSuccess = {
                isLoading = false
                Toast.makeText(context, "Kiểm tra email để đặt lại mật khẩu!", Toast.LENGTH_LONG).show()
                onResetSent()
            },
            onError = { errorMessage ->
                isLoading = false
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quên mật khẩu",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Để đặt lại mật khẩu, vui lòng nhập địa chỉ email liên quan",
                fontSize = 18.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = drawable.forgot),
                contentDescription = "Illustration",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
            )
            Spacer(modifier = Modifier.height(35.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester)
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Tab) {
                            resetButtonFocusRequester.requestFocus()
                            true
                        } else false
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { handleForgotPassword() },
                colors = ButtonDefaults.buttonColors(Color.Red),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(300.dp)
                    .height(55.dp)
                    .focusRequester(resetButtonFocusRequester)
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                            handleForgotPassword()
                            true
                        } else false
                    }
            ) {
                Text(text = "Gửi yêu cầu", color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text(text = "Bạn đã nhớ mật khẩu?", color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Đăng nhập",
                    color = Color(0xFFFFA500),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onBackToLogin() }
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewForgotPasswordScreen() {
    ForgotPasswordScreen(onBackToLogin = {}, onResetSent = {})
}