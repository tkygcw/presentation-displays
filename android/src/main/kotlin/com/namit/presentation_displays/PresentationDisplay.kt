package com.namit.presentation_displays

import android.app.Presentation
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngineCache

class PresentationDisplay(context: Context, private val tag: String, display: Display) :
    Presentation(context, display) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flContainer = FrameLayout(context)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        flContainer.layoutParams = params

        setContentView(flContainer)

        //此功能作用于主屏Activity返回桌面后，副屏View仍然显示
        //This function is used to display the secondary
        // screen View after the main screen activity returns to the desktop
        if (Build.VERSION.SDK_INT >= 32) {
        } else if (Build.VERSION.SDK_INT >= 26) {
            // 画中画等详细请查看android sdk    For details such as picture in picture, please check the android sdk
            window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        } else {
            // 8.0 以下的安卓版本要实现上述功能使用以下api  Android versions below 8.0 use the following apis to achieve the above functions
            window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY)
        }

        val flutterView = FlutterView(context)
        flContainer.addView(flutterView, params)
        val flutterEngine = FlutterEngineCache.getInstance().get(tag)
        if (flutterEngine != null) {
            flutterView.attachToFlutterEngine(flutterEngine)
        } else {
            Log.e("PresentationDisplay", "Can't find the FlutterEngine with cache name $tag")
        }
    }
}
