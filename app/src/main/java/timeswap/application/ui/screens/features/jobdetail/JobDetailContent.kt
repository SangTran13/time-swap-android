package timeswap.application.ui.screens.features.jobdetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import timeswap.application.data.response.JobDetailResponse
import timeswap.application.shared.utils.CommonFunction

@Composable
fun JobDetailContent(jobDetail: JobDetailResponse, modifier: Modifier = Modifier) {

    val responsibilities = jobDetail.responsibilities.split(",")
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (jobDetail.startDate != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Start Date",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Start Date: ${
                        CommonFunction.formatDateFromUTC(
                            jobDetail.startDate,
                            1
                        )
                    }",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Due Date",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Due Date: ${CommonFunction.formatDateFromUTC(jobDetail.dueDate!!, 1)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(
            text = jobDetail.description,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Responsibilities", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        responsibilities.forEach { responsibility ->
            Text(
                text = "• $responsibility",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = "Category:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = jobDetail.category.categoryName,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                color = Color.Blue
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = "Industry:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = jobDetail.industry.industryName,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                color = Color.Blue
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = "Fee:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = CommonFunction.formatCurrency(jobDetail.fee, "VND"),
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                color = Color.Red,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = "Email:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = jobDetail.ownerEmail,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${jobDetail.ownerEmail}")
                    }
                    context.startActivity(intent)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = "Full Location:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = jobDetail.ward.fullLocation,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp
            )
        }
    }
}
