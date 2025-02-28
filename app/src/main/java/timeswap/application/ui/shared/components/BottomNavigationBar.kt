package timeswap.application.ui.shared.components

import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import timeswap.application.R
import timeswap.application.ui.screens.core.navigation.HomeDestination
import timeswap.application.ui.screens.core.navigation.JobDestination
import timeswap.application.ui.screens.core.navigation.SettingsDestination


@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        modifier = Modifier.fillMaxWidth().height(120.dp),
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                navController = navController,
                route = HomeDestination.route,
                iconRes = R.drawable.home,
                contentDescription = "Home",
                isSelected = currentRoute == HomeDestination.route
            )

            BottomNavItem(
                navController = navController,
                route = JobDestination.route,
                iconRes = R.drawable.connection,
                contentDescription = "Search",
                isSelected = currentRoute == JobDestination.route
            )

            Button(
                onClick = { },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.size(54.dp).offset(y = (-12).dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            BottomNavItem(
                navController = navController,
                route = HomeDestination.route,
                iconRes = R.drawable.chat,
                contentDescription = "Notifications",
                isSelected = currentRoute == "notifications"
            )

            BottomNavItem(
                navController = navController,
                route = SettingsDestination.route,
                iconRes = R.drawable.ic_setting,
                contentDescription = "Setting",
                isSelected = currentRoute == SettingsDestination.route
            )
        }
    }
}

@Composable
fun BottomNavItem(
    navController: NavController,
    route: String,
    iconRes: Int,
    contentDescription: String,
    isSelected: Boolean
) {
    IconButton(
        onClick = {
            if (navController.currentDestination?.route != route) {
                navController.navigate(route) {
                    popUpTo(HomeDestination.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
        modifier = Modifier.size(38.dp)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(32.dp),
            tint = if (isSelected) Color(0xFFFFA726) else Color.Black
        )
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    val navController = NavController(LocalContext.current)
    BottomNavigationBar(navController)
}


