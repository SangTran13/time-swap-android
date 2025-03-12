package timeswap.application.ui.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import timeswap.application.R
import timeswap.application.ui.screens.core.navigation.ChatDestination
import timeswap.application.ui.screens.core.navigation.HomeDestination
import timeswap.application.ui.screens.core.navigation.JobDestination
import timeswap.application.ui.screens.core.navigation.JobPostDestination
import timeswap.application.ui.screens.core.navigation.SettingsDestination


@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showDialog by remember { mutableStateOf(false) }

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
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
                contentDescription = "Job List",
                isSelected = currentRoute == JobDestination.route
            )

            Button(
                onClick = { showDialog = true },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .size(58.dp)
                    .offset(y = (-14).dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            BottomNavItem(
                navController = navController,
                route = ChatDestination.route,
                iconRes = R.drawable.chat,
                contentDescription = "Chat",
                isSelected = currentRoute == "chat"
            )

            BottomNavItem(
                navController = navController,
                route = SettingsDestination.route,
                iconRes = R.drawable.ic_setting,
                contentDescription = "Setting",
                isSelected = currentRoute == SettingsDestination.route
            )
        }
        if (showDialog) {
            AddItemPopup(navController, onDismiss = { showDialog = false })
        }
    }
}

@Composable
fun AddItemPopup(navController: NavController, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, containerColor = Color.White, title = {
        Text("What would you like to add?", fontWeight = FontWeight.Bold)
    }, text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Would you like to post your tips and experiences or create a job?")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onDismiss()
                    navController.navigate(JobPostDestination.route)
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("POST A JOB", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    onDismiss()
                    navController.navigate(HomeDestination.route)
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDE7F6))
            ) {
                Text("Back To Home", color = Color(0xFF12004F))
            }
        }
    }, confirmButton = { })
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
        }, modifier = Modifier.size(38.dp)
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


