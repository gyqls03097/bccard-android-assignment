package test.bccard.android.assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccessKeyUiState(
    val accessKey: String = "",
    val error: String? = null,
    val saved: Boolean = false,
)

class AccessKeyViewModel(
    private val saveAccessKey: (String) -> Unit,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccessKeyUiState())
    val uiState: StateFlow<AccessKeyUiState> = _uiState.asStateFlow()

    fun onAccessKeyChange(accessKey: String) {
        _uiState.update { it.copy(accessKey = accessKey, error = null) }
    }

    fun save() {
        viewModelScope.launch {
            if (_uiState.value.accessKey.trim().isEmpty()) {
                _uiState.update { it.copy(error = "Access Key 를 확인해주세요") }
            } else {
                saveAccessKey(_uiState.value.accessKey)
                _uiState.update { it.copy(saved = true) }
            }
        }
    }
}
