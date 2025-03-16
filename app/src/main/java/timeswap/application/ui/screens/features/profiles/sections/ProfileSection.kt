package timeswap.application.ui.screens.features.profiles.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import timeswap.application.R

@Composable
fun ProfileSection(title: String, iconRes: Int, details: Any, navController: NavController?, destinationRoute: String? = null) {
    var expanded by remember { mutableStateOf(false) }

    val detailsList: List<String> = when (details) {
        is String -> listOf(details)
        is List<*> -> details.filterIsInstance<String>()
        else -> emptyList()
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(25.dp),
                    tint = Color(0xFFFFA726)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E1E1E)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        painter = painterResource(id = if (expanded) R.drawable.ic_eye_off else R.drawable.ic_eye),
                        contentDescription = "Toggle Details",
                        modifier = Modifier.size(25.dp),
                        tint = Color(0xFFFFA726)
                    )
                }

                if (destinationRoute != null) {
                    IconButton(onClick = { navController?.navigate(destinationRoute) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_next_page),
                            contentDescription = "Edit",
                            modifier = Modifier.size(25.dp),
                            tint = Color(0xFFFFA726)
                        )
                    }
                }
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                        .fillMaxWidth()
                ) {
                    if (detailsList.isNotEmpty()) {
                        detailsList.forEach { item ->
                            Text(
                                text = item,
                                fontSize = 16.sp,
                                color = Color(0xFF424242),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "Không có thông tin chi tiết.",
                            fontSize = 16.sp,
                            color = Color(0xFF424242)
                        )
                    }
                }
            }
        }
    }
}