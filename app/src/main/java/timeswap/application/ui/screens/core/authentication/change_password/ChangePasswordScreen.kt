package timeswap.application.ui.screens.core.authentication.change_password

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.launch

import timeswap.application.network.services.AuthServices

@Composable
fun ChangePasswordScreen(onPasswordChanged: () -> Unit, onBackToHome: () -> Unit) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPreferences =
        remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
    val authServices = remember { AuthServices(context, sharedPreferences) }
    val coroutineScope = rememberCoroutineScope()

    fun handleChangePassword() {
        if (currentPassword.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
            Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }
        if (newPassword != confirmNewPassword) {
            Toast.makeText(context, "New passwords do not match!", Toast.LENGTH_SHORT).show()
            return
        }

        coroutineScope.launch {
            isLoading = true
            val result = authServices.changePassword(currentPassword, newPassword)
            isLoading = false

            result.onSuccess {
                Toast.makeText(context, "Password changed successfully!", Toast.LENGTH_SHORT).show()
                onPasswordChanged()
            }.onFailure { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
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
            modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Change Password",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            PasswordTextField("Current Password", currentPassword) { currentPassword = it }
            PasswordTextField("New Password", newPassword) { newPassword = it }
            PasswordTextField("Confirm New Password", confirmNewPassword) {
                confirmNewPassword = it
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { handleChangePassword() },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(Color.Red),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(300.dp)
                    .height(55.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Change Password", fontSize = 16.sp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { onBackToHome() },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(Color.Gray),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(300.dp)
                    .height(55.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Back", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun PasswordTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))
}
