package timeswap.application.ui.screens.features.home.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import timeswap.application.R

@Composable
fun JobCategorySection() {

    Text(
        text = "Tìm việc làm",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        JobCategoryBox(
            "44.5k",
            "Remote Job",
            Color(0xFF89CFF0),
            Modifier
                .weight(1f)
                .height(172.dp),
            R.drawable.ic_remote_work
        )
        Column(modifier = Modifier.weight(1f)) {
            JobCategoryBox("66.8k", "Full Time", Color(0xFFB39DDB))
            Spacer(modifier = Modifier.height(12.dp))
            JobCategoryBox("38.9k", "Part Time", Color(0xFFFFAB91))
        }
    }

}

@Composable
fun JobCategoryBox(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    iconResId: Int? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            if (iconResId != null) {
                Image(
                    painter = painterResource(iconResId),
                    contentDescription = label,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = label, fontSize = 14.sp, color = Color.Black)
        }
    }

}