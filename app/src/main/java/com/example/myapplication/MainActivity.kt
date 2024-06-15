package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val REQUEST_CAMERA_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                WebViewExample()
            }
        }

        // 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 권한이 승인된 경우 카메라 기능을 사용할 수 있습니다.
            } else {
                // 권한이 거부된 경우 사용자에게 알림
            }
        }
    }
}

@Composable
fun WebViewExample() {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                webChromeClient = object : WebChromeClient() {
                    override fun onPermissionRequest(request: PermissionRequest) {
                        // 권한 요청 처리
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(
                                    context as ComponentActivity,
                                    arrayOf(Manifest.permission.CAMERA),
                                    1
                                )
                            } else {
                                request.grant(request.resources)
                            }
                        } else {
                            request.grant(request.resources)
                        }
                    }
                }
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true // 로컬 스토리지 사용 허용
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                    allowFileAccess = true // 파일 액세스 허용
                    javaScriptCanOpenWindowsAutomatically = true // 자바스크립트 팝업 허용
                }
                loadUrl("https://main--coupon-moa.netlify.app/")
                setInitialScale(1)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun WebViewPreview() {
    MyApplicationTheme {
        WebViewExample()
    }
}