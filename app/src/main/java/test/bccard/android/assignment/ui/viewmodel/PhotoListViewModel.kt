package test.bccard.android.assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import test.bccard.android.assignment.domain.model.Photo
import test.bccard.android.assignment.domain.usecase.GetPhotosUseCase

data class PhotoListUiState(
    val photos: List<Photo> = emptyList(),
    val likedIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false,
)

class PhotoListViewModel(
    private val getPhotos: GetPhotosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoListUiState())
    val uiState: StateFlow<PhotoListUiState> = _uiState.asStateFlow()

    private var nextPage = 1

    private var isPageLoading = false

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (isPageLoading) return
        val state = _uiState.value
        if (state.endReached || state.error != null) return
        fetch()
    }

    fun retry() {
        if (isPageLoading) return
        fetch()
    }

    private fun fetch() {
        isPageLoading = true
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getPhotos(nextPage)
                .onSuccess { page ->
                    _uiState.update { state ->
                        val existItems: Set<String> = state.photos.map { it.id }.toSet()
                        val newItems = page.filter { it.id.isNotBlank() && !existItems.contains(it.id) }
                        state.copy(
                            photos = state.photos + newItems,
                            isLoading = false,
                            error = null,
                            endReached = page.isEmpty(),
                        )
                    }
                    if (page.isNotEmpty()) nextPage++
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "사진을 불러오지 못했습니다.",
                        )
                    }
                }
            isPageLoading = false
        }
    }

    fun toggleLike(photo: Photo) {
        // todo db 추가 후 구현
    }
}
