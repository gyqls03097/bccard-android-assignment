package test.bccard.android.assignment.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun PhotoDetailScreen(
    modifier: Modifier = Modifier,
) {
    PhotoDetailScreenContent(
        modifier
    )
}

@Composable
private fun PhotoDetailScreenContent(
    modifier: Modifier = Modifier,
) {

    Box(modifier = modifier.fillMaxSize()) {

    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoDetailScreenPreview() {
    PhotoDetailScreenContent(
    )
}
