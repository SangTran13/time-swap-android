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

object SettingsDestination : Destination {
    override val route = "settings"
    override val title = "Settings"
}

object JobDestination : Destination {
    override val route = "job"
    override val title = "Job"
}

object ChangePasswordDestination : Destination {
    override val route = "change-password"
    override val title = "Change Password"
}

object ProfileDestination : Destination {
    override val route = "profile"
    override val title = "Profile"
}

object EditAboutMeDestination : Destination {
    override val route = "edit_about_me_screen"
    override val title = "Edit About Me"
}

object EditMajorDestination : Destination {
    override val route = "edit_major_screen"
    override val title = "Edit Major"
}

object EditEducationDestination : Destination {
    override val route = "edit_education_screen"
    override val title = "Edit Education"
}

object ChatDestination : Destination {
    override val route = "chat"
    override val title = "Chat"
}

object JobPostDestination : Destination {
    override val route = "job_post"
    override val title = "Job Post"
}