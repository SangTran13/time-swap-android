package timeswap.application.ui.screens.core.navigation

interface Destination {
    val route: String
    val title: String
}

object SplashDestination : Destination {
    override val route = "splash"
    override val title = "Splash"
}

object OnboardingDestination : Destination {
    override val route = "onboarding"
    override val title = "Onboarding"
}

object LoginDestination : Destination {
    override val route = "login"
    override val title = "Login"
}

object RegisterDestination : Destination {
    override val route = "register"
    override val title = "Register"
}

object ForgotPasswordDestination : Destination {
    override val route = "forgot-password"
    override val title = "Forgot Password"
}

object HomeDestination : Destination {
    override val route = "home"
    override val title = "Home"
}

object PaymentDestination : Destination {
    override val route = "payment"
    override val title = "Payment"
}