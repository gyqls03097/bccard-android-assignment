package test.bccard.android.assignment.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import test.bccard.android.assignment.R


@Composable
fun PhotoExpandScreen(
    url: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    PhotoExpandScreenContent(
        url = url,
        modifier = modifier,
        onBackClick = onBackClick
    )
}

@Composable
private fun PhotoExpandScreenContent(
    url: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_back),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .align(Alignment.BottomCenter)
            )
        }

        val imageState = rememberZoomableImageState()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            ZoomableAsyncImage(
                model = url.takeIf { it.isNotBlank() },
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                state = imageState,
            )
            if (url.isNotBlank() && !imageState.isImageDisplayed) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PhotoExpandScreenPreview() {
    PhotoExpandScreenContent("https://sample.jpg")
}
