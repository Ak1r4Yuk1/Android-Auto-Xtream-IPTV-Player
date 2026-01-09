package com.akira.aaplayer

import android.annotation.SuppressLint
import android.app.Presentation
import android.content.Context
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.os.Bundle
import android.view.Display
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.car.app.AppManager
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.SurfaceCallback
import androidx.car.app.SurfaceContainer
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template

@SuppressLint("SetJavaScriptEnabled")
class AAPlayerScreen(carContext: CarContext) : Screen(carContext), SurfaceCallback {

    private var virtualDisplay: VirtualDisplay? = null
    private var presentation: CarScreenPresentation? = null
    private var webView: WebView? = null

    init {
        (carContext.getCarService(AppManager::class.java) as AppManager).setSurfaceCallback(this)
    }

    override fun onGetTemplate(): Template {
        return PaneTemplate.Builder(
            Pane.Builder()
                .addRow(
                    Row.Builder()
                        .setTitle("Starting Player...")
                        .build()
                )
                .build()
        ).build()
    }

    override fun onSurfaceAvailable(surfaceContainer: SurfaceContainer) {
        createVirtualDisplay(surfaceContainer)
    }

    override fun onSurfaceDestroyed(surfaceContainer: SurfaceContainer) {
        tearDownPresentation()
    }

    override fun onVisibleAreaChanged(visibleArea: Rect) {
        // Not used
    }

    override fun onStableAreaChanged(stableArea: Rect) {
        // Not used
    }

    private fun createVirtualDisplay(surfaceContainer: SurfaceContainer) {
        tearDownPresentation()
        val displayManager = carContext.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        virtualDisplay = displayManager.createVirtualDisplay(
            "AAPlayer",
            surfaceContainer.width,
            surfaceContainer.height,
            surfaceContainer.dpi,
            surfaceContainer.surface,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION
        )

        val display = virtualDisplay?.display
        if (display != null) {
            presentation = CarScreenPresentation(carContext, display).apply {
                show()
                this@AAPlayerScreen.webView = this.webView
            }
        }
    }

    private fun tearDownPresentation() {
        webView?.destroy()
        webView = null
        presentation?.dismiss()
        presentation = null
        virtualDisplay?.release()
        virtualDisplay = null
    }

    private class CarScreenPresentation(context: Context, display: Display) : Presentation(context, display) {
        var webView: WebView? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.presentation_browser)
            webView = findViewById(R.id.presentationWebView)
            webView?.apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        return false
                    }
                }
                loadUrl("file:///android_asset/player.html")
            }
        }
    }
}
