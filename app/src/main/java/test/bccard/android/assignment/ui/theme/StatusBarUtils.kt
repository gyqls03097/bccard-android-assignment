package test.bccard.android.assignment.ui.theme

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 임시로 상태바의 글자와 아이콘을 밝게 만든다
@Composable
fun StatusBarIconToLight() {
    val activity = LocalActivity.current
    val view = LocalView.current

    DisposableEffect(activity) {
        val window = activity?.window
        if (window == null) {
            onDispose { }
        } else {
            val controller = WindowCompat.getInsetsController(window, view)
            val wasLightStatusBars = controller.isAppearanceLightStatusBars
            controller.isAppearanceLightStatusBars = false
            onDispose {
                controller.isAppearanceLightStatusBars = wasLightStatusBars
            }
        }
    }
}