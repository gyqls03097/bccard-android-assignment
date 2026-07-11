package test.bccard.android.assignment.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import test.bccard.android.assignment.ui.viewmodel.AccessKeyViewModel

@Composable
fun AccessKeyScreen(
    viewModel: AccessKeyViewModel,
    onSaved: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) onSaved()
    }

    AccessKeyScreenContent(
        accessKey = uiState.accessKey,
        error = uiState.error,
        onAccessKeyChange = viewModel::onAccessKeyChange,
        onSave = viewModel::save,
    )
}

@Composable
private fun AccessKeyScreenContent(
    accessKey: String,
    error: String? = null,
    onAccessKeyChange: (String) -> Unit = {},
    onSave: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Unsplash Access Key",
            fontSize = 24.sp,
        )
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = "사용자의 Unsplash Access Key 를 입력해주세요.",
            color = Color.Gray,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            value = accessKey,
            onValueChange = onAccessKeyChange,
            label = { Text(text = "Access Key") },
            singleLine = true,
            isError = error != null,
            enabled = true,
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSave() }),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .padding(top = 6.dp, start = 4.dp)
        ) {
            if (error != null) {
                Text(
                    text = error,
                    color = Color.Red,
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            onClick = onSave,
            enabled = true,
        ) {
            Text(text = "저장하고 시작하기")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccessKeyScreenPreview() {
    AccessKeyScreenContent(accessKey = "")
}

@Preview(showBackground = true)
@Composable
private fun AccessKeyScreenErrorPreview() {
    AccessKeyScreenContent(
        accessKey = "    ",
        error = "Access Key 를 입력해 주세요.",
    )
}
