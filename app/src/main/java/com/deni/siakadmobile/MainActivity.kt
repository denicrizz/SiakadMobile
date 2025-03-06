package com.deni.siakadmobile
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebViewScreen("https://siakad2.unpkediri.ac.id/")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(url: String) {
    val context = LocalContext.current
    var webView: WebView? by remember { mutableStateOf(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }

    fun printPage() {
        webView?.let { webView ->
            val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
            val printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter("WebView Print")
            printManager.print("WebView Print", printAdapter, PrintAttributes.Builder().build())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SIAKAD UNP Kediri") })
        },
        bottomBar = {
            BottomAppBar {
                TextButton(onClick = { webView?.goBack() }, enabled = canGoBack) {
                    Text("Back")
                }
                TextButton(onClick = { webView?.goForward() }, enabled = canGoForward) {
                    Text("Forward")
                }
                TextButton(onClick = { printPage() }) {
                    Text("Print Page")
                }
            }
        }
    ) { padding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = { context ->
                WebView(context).apply {
                    webView = this
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                    }
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            canGoBack = view?.canGoBack() == true
                            canGoForward = view?.canGoForward() == true
                        }
                    }
                    webChromeClient = WebChromeClient()
                    setDownloadListener { url, _, _, _, _ ->
                    }
                    loadUrl(url)
                }
            }
        )
    }
}