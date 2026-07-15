package test.bccard.android.assignment.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

data class DialogButton(val text: String, val onClick: () -> Unit)

@Composable
fun SimpleDialog(
    title: String,
    text: String,
    onConfirm: DialogButton,
    onDismiss: DialogButton,
) {
    AlertDialog(
        onDismissRequest = onDismiss.onClick,
        title = { Text(text = title) },
        text = { Text(text = text) },
        confirmButton = {
            TextButton(onClick = onConfirm.onClick) {
                Text(text = onConfirm.text)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss.onClick) {
                Text(text = onDismiss.text)
            }
        },
    )
}
