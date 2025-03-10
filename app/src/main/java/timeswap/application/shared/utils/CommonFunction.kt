package timeswap.application.shared.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat

import java.util.Locale
import java.util.TimeZone

object CommonFunction {
    fun formatDateFromUTC(utcDate: String, formatType: Int): String {
        return try {

            val patternUtc: String
            val patternFormatTime: String

            if (formatType == 1) {
                patternUtc = "yyyy-MM-dd'T'HH:mm:ss'Z'"
                patternFormatTime = "EEEE, dd MMMM yyyy HH:mm a"
            } else {
                patternUtc = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
                patternFormatTime = "EEEE, dd MMMM yyyy"
            }

            val utcFormat = SimpleDateFormat(patternUtc, Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }

            val localFormat = SimpleDateFormat(patternFormatTime, Locale("vi", "VN")).apply {
                timeZone = TimeZone.getDefault()
            }

            val date = utcFormat.parse(utcDate)
            localFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid date format"
        }
    }

    fun formatCurrency(amount: Double, currency: String = "VND"): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        return "${formatter.format(amount)} $currency"
    }

}