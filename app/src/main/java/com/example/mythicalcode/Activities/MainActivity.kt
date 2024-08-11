package com.example.mythicalcode.Activities
import CodeRepository
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mythicalcode.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val repository = CodeRepository() // Initialize CodeRepository
    private lateinit var webView: WebView
    private lateinit var webViewProgressBar: ProgressBar
    private lateinit var outputProgressBar: ProgressBar
    private lateinit var runCodeButton: Button
    private lateinit var outputTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.codeEditorWebView)
        webViewProgressBar = findViewById(R.id.webViewProgressBar)
        outputProgressBar = findViewById(R.id.outputProgressBar)
        outputTextView = findViewById(R.id.outputTextView)
        runCodeButton = findViewById(R.id.runCodeButton)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)


        // WebView settings
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        // Ensure WebView is focusable and can interact with the keyboard
        webView.isFocusable = true
        webView.isFocusableInTouchMode = true
        webView.requestFocus(View.FOCUS_DOWN)

        webView.setOnTouchListener { v, event ->
            v.requestFocus()
            v.onTouchEvent(event)
            false
        }

        webView.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        // Show progress bar while WebView is loading
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                webView.visibility = View.GONE
                webViewProgressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                webView.visibility = View.VISIBLE
                webViewProgressBar.visibility = View.GONE
            }
        }

        // JavaScript interface for communication between WebView and Kotlin
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun sendCode(code: String) {
                Log.d("MainActivity", "Received code: $code")
                runOnUiThread {
                    runCodeButton.isEnabled = true // Enable the button after receiving the code
                    runCode(code, inputEditText.text.toString())
                }
            }
        }, "Android")

        // Load Monaco editor
        webView.loadUrl("file:///android_asset/monaco_editor.html")

        // Set up the Run Code button click listener
        runCodeButton.setOnClickListener {
            runCodeButton.isEnabled = false // Disable the button to prevent multiple clicks
            outputProgressBar.visibility = View.VISIBLE
            outputTextView.text = "" // Clear previous output
            webView.evaluateJavascript("sendCodeToAndroid();", null)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_code -> {
                    // Handle Home action
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_practice -> {
                    // Handle Code Editor action
                    val intent = Intent(this, PracticeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_battle -> {
                    // Handle Settings action
                    Toast.makeText(this, "Battle", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun runCode(code: String, input: String) {
        lifecycleScope.launch {
            try {
                val output = repository.sendCodeRequest("cpp", code, input)
                outputTextView.text = output
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Exception: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                outputProgressBar.visibility = View.GONE
                runCodeButton.isEnabled = true // Re-enable the button after code execution
            }
        }
    }


}
