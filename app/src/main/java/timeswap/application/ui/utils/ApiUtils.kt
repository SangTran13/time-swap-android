package timeswap.application.ui.utils

import android.content.Context
import org.json.JSONObject

/**
 * Utility object to handle API-related helper functions.
 */
object ApiUtils {

    /**
     * Extracts the `statusCode` from the given error response body.
     *
     * @param errorBody The raw error body as a JSON string from an API response.
     *                  This is typically obtained from `response.errorBody()?.string()`.
     *
     * @return The extracted status code as an integer. If the `statusCode` field is not found
     *         or the input is invalid, it returns `-1` as a default value.
     */
    fun extractStatusCode(errorBody: String?): Int {
        return try {
            // Convert the errorBody string into a JSON object
            JSONObject(errorBody ?: "{}").optInt("statusCode", -1)
        } catch (e: Exception) {
            // In case of JSON parsing errors, return a default value of -1
            -1
        }
    }

    fun clearAccessToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("accessToken").apply()
    }

    fun checkExpiredToken(context: Context) : Boolean {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val expiresAt = sharedPreferences.getLong("expiresAt", 0)
        val currentTime =  System.currentTimeMillis()
        return expiresAt <= currentTime
    }

}