package timeswap.application

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timeswap.application.network.services.PaymentRepository


class WebViewActivity : ComponentActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowContentAccess = true
            settings.allowFileAccess = true

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val url = request?.url.toString()
                    if (url.contains("payos-return")) {
                        handleReturnUrl(url)
                        return true
                    }
                    return false
                }
            }

            webChromeClient = WebChromeClient()
        }

        setContentView(webView)

        val url = intent.getStringExtra("payment_url")
        if (!url.isNullOrEmpty()) {
            webView.loadUrl(url)
        } else {
            Toast.makeText(this, "Not found payment url", Toast.LENGTH_LONG).show()
            navigateToHomeScreen()
        }
    }

    private fun handleReturnUrl(url: String) {
        Log.d("WebViewActivity", "Handling PayOS return URL: $url")

        val uri = Uri.parse(url)
        val status = uri.getQueryParameter("status")
        val code = uri.getQueryParameter("code")
        val paymentId = uri.getQueryParameter("id")
        val orderCode = uri.getQueryParameter("orderCode")
        val cancel = uri.getQueryParameter("cancel")?.toBoolean() ?: false

        if (status.isNullOrEmpty() || code.isNullOrEmpty() || paymentId.isNullOrEmpty() || orderCode.isNullOrEmpty()) {
            Log.d("WebViewActivity", "Require to feedback from PayOS")
            Toast.makeText(this, "Error payment!", Toast.LENGTH_LONG).show()
            navigateToHomeScreen()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            PaymentRepository(this@WebViewActivity).verifyPayment(
                status, code, paymentId, orderCode, cancel
            ) { isSuccess, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        this@WebViewActivity,
                        if (isSuccess) "Payment success!" else "Payment failed!",
                        Toast.LENGTH_LONG
                    ).show()
                    navigateToHomeScreen()
                }
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
