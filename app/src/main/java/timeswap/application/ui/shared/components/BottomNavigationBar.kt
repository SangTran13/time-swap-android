package timeswap.application.ui.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(navController, HomeDestination.route, R.drawable.home, "Home", currentRoute)
                BottomNavItem(navController, JobDestination.route, R.drawable.connection, "Job List", currentRoute)

                Spacer(modifier = Modifier.width(100.dp))

                BottomNavItem(navController, ChatDestination.route, R.drawable.chat, "Chat", currentRoute)
                BottomNavItem(navController, SettingsDestination.route, R.drawable.ic_setting, "Setting", currentRoute)
            }

        }

        FloatingActionButton(
            onClick = { showDialog = true },
            shape = CircleShape,
            containerColor = Color.Red,
            modifier = Modifier
                .size(65.dp)
                .offset(y = (-40).dp),
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(30.dp)
            )
        }

        if (showDialog) {
            AddItemPopup(navController) { showDialog = false }
        }
    }
}

@Composable
fun AddItemPopup(navController: NavController, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Bạn muốn thêm gì?", fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Bạn có muốn đăng mẹo và kinh nghiệm của mình hoặc tạo việc làm không?")
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onDismiss()
                        navController.navigate(JobPostDestination.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Đăng việc làm", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onDismiss()
                        navController.navigate(HomeDestination.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDE7F6))
                ) {
                    Text("Trở về trang chủ", color = Color(0xFF12004F))
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun BottomNavItem(
    navController: NavController,
    route: String,
    iconRes: Int,
    contentDescription: String,
    currentRoute: String?
) {
    IconButton(
        onClick = {
            if (currentRoute != route) {
                navController.navigate(route) {
                    popUpTo(HomeDestination.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(32.dp),
            tint = if (currentRoute == route) Color(0xFFFFA726) else Color.Black
        )
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(navController)
}
