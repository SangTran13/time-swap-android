package timeswap.application.ui.screens.core.navigation

import android.content.Context

import kotlinx.coroutines.delay

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import timeswap.application.network.services.PaymentRepository
import timeswap.application.network.services.UserRepository
import timeswap.application.ui.screens.core.authentication.forgot_password.ForgotPasswordScreen
import timeswap.application.ui.screens.core.authentication.login.LoginScreen
import timeswap.application.ui.screens.core.authentication.register.RegisterScreen
import timeswap.application.ui.screens.features.home.HomeScreen
import timeswap.application.ui.screens.features.on_board.OnboardingScreen
import timeswap.application.ui.screens.features.payment.PaymentScreen
import timeswap.application.ui.screens.features.splash.SplashScreen

@Composable
fun NavRegister(context: Context) {
    val sharedPreferences =
        remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf(SplashDestination.route) }

    LaunchedEffect(Unit) {
        delay(3000)
        startDestination = if (sharedPreferences.getString("accessToken", null) != null) {
            HomeDestination.route
        } else {
            OnboardingDestination.route
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(SplashDestination.route) { SplashScreen() }
        composable(OnboardingDestination.route) {
            OnboardingScreen(onNext = { navController.navigate(LoginDestination.route) })
        }
        composable(LoginDestination.route) {
            LoginScreen(onForgotPassword = { navController.navigate(ForgotPasswordDestination.route) },
                onNext = { navController.navigate(HomeDestination.route) },
                onSignUp = { navController.navigate(RegisterDestination.route) })
        }
        composable(RegisterDestination.route) {
            RegisterScreen(onBackToLogin = { navController.navigate(LoginDestination.route) })
        }
        composable(ForgotPasswordDestination.route) {
            ForgotPasswordScreen(onBackToLogin = { navController.navigate(LoginDestination.route) },
                onResetSent = { navController.navigate(LoginDestination.route) })
        }
        composable(HomeDestination.route) {
            HomeScreen(context = context, onLogout = {
                navController.navigate(LoginDestination.route) {
                    popUpTo(HomeDestination.route) { inclusive = true }
                }
            }, onNavigateToPayment = { navController.navigate(PaymentDestination.route) })
        }
        composable(PaymentDestination.route) {
            PaymentScreen(sharedPreferences = sharedPreferences,
                userRepository = UserRepository(),
                paymentRepository = PaymentRepository(context),
                onBackToHome = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(HomeDestination.route) { inclusive = true }
                    }
                })
        }
    }
}
