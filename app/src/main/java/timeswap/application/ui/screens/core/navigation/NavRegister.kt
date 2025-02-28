package timeswap.application.ui.screens.core.navigation

import timeswap.application.ui.shared.components.BottomNavigationBar
import timeswap.application.ui.screens.features.setting.SettingsScreen

import android.content.Context

import kotlinx.coroutines.delay

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

import timeswap.application.network.services.PaymentRepository
import timeswap.application.network.services.UserRepository
import timeswap.application.ui.screens.core.authentication.forgot_password.ForgotPasswordScreen
import timeswap.application.ui.screens.core.authentication.login.LoginScreen
import timeswap.application.ui.screens.core.authentication.register.RegisterScreen
import timeswap.application.ui.screens.features.home.HomeScreen
import timeswap.application.ui.screens.features.jobs.JobScreen
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

    Scaffold(
        bottomBar = {
            if (navController.currentDestination?.route in listOf(
                    HomeDestination.route,
                    JobDestination.route,
                    SettingsDestination.route
                )
            ) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(
                    bottom = 0.dp,
                    top = 0.dp,
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                )
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(SplashDestination.route) { SplashScreen() }
                composable(OnboardingDestination.route) {
                    OnboardingScreen(onNext = { navController.navigate(LoginDestination.route) })
                }
                composable(LoginDestination.route) {
                    LoginScreen(
                        onForgotPassword = {
                            navController.navigate(ForgotPasswordDestination.route)
                        },
                        onNext = { navController.navigate(HomeDestination.route) },
                        onSignUp = { navController.navigate(RegisterDestination.route) }
                    )
                }
                composable(RegisterDestination.route) {
                    RegisterScreen(onBackToLogin = { navController.navigate(LoginDestination.route) })
                }
                composable(ForgotPasswordDestination.route) {
                    ForgotPasswordScreen(
                        onBackToLogin = { navController.navigate(LoginDestination.route) },
                        onResetSent = { navController.navigate(LoginDestination.route) }
                    )
                }
                composable(HomeDestination.route) {
                    HomeScreen(context = context, navController = navController)
                }
                composable(PaymentDestination.route) {
                    PaymentScreen(
                        sharedPreferences = sharedPreferences,
                        userRepository = UserRepository(),
                        paymentRepository = PaymentRepository(context),
                        onBackToHome = {
                            navController.navigate(HomeDestination.route) {
                                popUpTo(HomeDestination.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(SettingsDestination.route) {
                    SettingsScreen(context = context, navController = navController)
                }
                composable(JobDestination.route) {
                    JobScreen(navController = navController)
                }
            }
        }
    }
}
