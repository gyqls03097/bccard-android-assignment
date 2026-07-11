package test.bccard.android.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import test.bccard.android.assignment.ui.theme.BccardTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val di = AppDI(applicationContext)

        setContent {
            BccardTheme(darkTheme = false) {
                AppNavHost(
                    di = di,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
