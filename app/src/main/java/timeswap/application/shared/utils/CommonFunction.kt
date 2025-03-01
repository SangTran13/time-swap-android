package timeswap.application.shared.utils

import java.text.SimpleDateFormat

import java.util.Locale
import java.util.TimeZone

object CommonFunction {
    fun formatDateFromUTC(utcDate: String): String {
        return try {
            val utcFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }


            val localFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("vi", "VN")).apply {
                timeZone = TimeZone.getDefault()
            }

            val date = utcFormat.parse(utcDate)
            localFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid date format"
        }
    }
}