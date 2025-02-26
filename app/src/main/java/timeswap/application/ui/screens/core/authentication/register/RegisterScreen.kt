package timeswap.application.ui.screens.core.authentication.register

import android.content.Context

import android.widget.Toast

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import timeswap.application.network.services.AuthServices

@Composable
fun RegisterScreen(onBackToLogin: () -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sharedPreferences =
        remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
    val registerService = remember { AuthServices(context, sharedPreferences) }

    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val phoneFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    val registerButtonFocusRequester = remember { FocusRequester() }

    fun isValidVietnamPhoneNumber(phone: String): Boolean {
        val regex = Regex("^(\\+84|0)[3|5789][0-9]{8}$")
        return regex.matches(phone)
    }

    fun handleRegister() {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidVietnamPhoneNumber(phoneNumber)) {
            Toast.makeText(context, "Invalid phone number format!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            return
        }

        registerService.register(firstName,
            lastName,
            email,
            phoneNumber,
            password,
            confirmPassword,
            onSuccess = {
                onBackToLogin()
            },
            onError = { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            })
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
                text = "Create an Account",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            RegisterTextField(
                "First Name",
                firstName,
                { firstName = it },
                firstNameFocusRequester,
                lastNameFocusRequester
            )
            RegisterTextField(
                "Last Name",
                lastName,
                { lastName = it },
                lastNameFocusRequester,
                emailFocusRequester
            )
            RegisterTextField(
                "Email", email, { email = it }, emailFocusRequester, phoneFocusRequester
            )
            RegisterTextField(
                "Phone Number",
                phoneNumber,
                { phoneNumber = it },
                phoneFocusRequester,
                passwordFocusRequester
            )
            RegisterTextField(
                "Password",
                password,
                { password = it },
                passwordFocusRequester,
                confirmPasswordFocusRequester,
                true
            )
            RegisterTextField(
                "Confirm Password",
                confirmPassword,
                { confirmPassword = it },
                confirmPasswordFocusRequester,
                registerButtonFocusRequester,
                true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { handleRegister() },
                colors = ButtonDefaults.buttonColors(Color.Red),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(300.dp)
                    .height(55.dp)
                    .focusRequester(registerButtonFocusRequester)
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                            handleRegister()
                            true
                        } else false
                    }) {
                Text(text = "SIGN UP", color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text(text = "Already have an account?", color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Sign in",
                    color = Color(0xFFFFA500),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onBackToLogin() })
            }
        }
    }
}

@Composable
fun RegisterTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    nextFocusRequester: FocusRequester,
    isPassword: Boolean = false
) {

    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                val image =
                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Tab) {
                    nextFocusRequester.requestFocus()
                    true
                } else false
            })
    Spacer(modifier = Modifier.height(12.dp))
}
